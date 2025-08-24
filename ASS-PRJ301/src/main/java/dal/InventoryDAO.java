package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import model.Book;
import model.InventoryMovement;

public class InventoryDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("SUMMER2025");

    /* =========================
       1) BOOK QUERIES (Inventory list + filter)
       ========================= */

    /** Lấy tất cả sách để hiển thị tồn kho (sắp theo code). */
    public List<Book> findAllBooks() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Book b ORDER BY b.code ASC", Book.class
            ).getResultList();
        } finally {
            em.close();
        }
        }

    /** Lọc theo tên hoặc mã sách (LIKE %q%). */
    public List<Book> findBooksByNameOrCode(String q) {
        String kw = (q == null ? "" : q.trim().toLowerCase());
        kw = "%" + kw + "%";
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Book b " +
                    "WHERE LOWER(b.title) LIKE :kw OR LOWER(b.code) LIKE :kw " +
                    "ORDER BY b.code ASC", Book.class)
                    .setParameter("kw", kw)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lọc sách theo name/status (status: "", "low", "ok")
     * name: tìm theo title/code (LIKE, không phân biệt hoa thường)
     */
    public List<Book> findBooks(String name, String status) {
        EntityManager em = emf.createEntityManager();
        try {
            String n = (name == null) ? null : name.trim();
            String s = (status == null) ? "" : status.trim().toLowerCase();

            StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");

            if (n != null && !n.isEmpty()) {
                jpql.append(" AND (LOWER(b.title) LIKE :kw OR LOWER(b.code) LIKE :kw)");
            }
            if ("low".equals(s)) {
                jpql.append(" AND COALESCE(b.stockQty,0) < COALESCE(b.minStock,0)");
            } else if ("ok".equals(s)) {
                jpql.append(" AND COALESCE(b.stockQty,0) >= COALESCE(b.minStock,0)");
            }

            jpql.append(" ORDER BY b.code");

            TypedQuery<Book> q = em.createQuery(jpql.toString(), Book.class);
            if (n != null && !n.isEmpty()) {
                q.setParameter("kw", "%" + n.toLowerCase() + "%");
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /* =========================
       2) MOVEMENT QUERIES (recent)
       ========================= */

    /** Lấy N movement gần nhất (kèm book). */
    public List<InventoryMovement> findRecentMovements(int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM InventoryMovement m " +
                    "JOIN FETCH m.book b " +
                    "LEFT JOIN FETCH m.order o " +
                    "ORDER BY m.createdAt DESC", InventoryMovement.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /* =========================
       3) MOVEMENT COMMANDS (Add/Delete)
       Chiến lược A: cập nhật Books.stockQty ngay lập tức
       ========================= */

    /**
     * Thêm 1 movement (IMPORT/ADJUST/SALE). Tự cộng/trừ tồn trong Books.
     * (chặn âm kho khi quantityChange < 0)
     */
    public void addMovement(Long bookId, String type, int quantityChange, String reason) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Book book = em.find(Book.class, bookId, LockModeType.PESSIMISTIC_WRITE);
            if (book == null) {
                throw new IllegalArgumentException("Book not found: id=" + bookId);
            }

            Integer cur = (book.getStockQty() == null ? 0 : book.getStockQty());
            int next = cur + quantityChange;
            if (next < 0) {
                throw new IllegalStateException("Kho không đủ hàng để xuất!");
            }

            InventoryMovement m = new InventoryMovement();
            m.setBook(book);
            m.setType(type);
            m.setQuantityChange(quantityChange);
            m.setReason(reason);
            m.setCreatedAt(LocalDateTime.now());
            em.persist(m);

            // cập nhật tồn hiện tại
            book.setStockQty(next);
            em.merge(book);

            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Xóa movement. Không cho xóa nếu là SALE gắn với Order. Tự rollback tồn.
     */
    public void deleteMovement(Long movementId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            InventoryMovement m = em.find(InventoryMovement.class, movementId, LockModeType.PESSIMISTIC_WRITE);
            if (m == null) {
                tx.commit();
                return;
            }
            if ("SALE".equalsIgnoreCase(m.getType()) && m.getOrder() != null) {
                throw new IllegalStateException("Cannot delete SALE movement linked to an Order.");
            }

            Book book = em.find(Book.class, m.getBook().getId(), LockModeType.PESSIMISTIC_WRITE);
            if (book == null) {
                throw new IllegalArgumentException("Book not found for movement.");
            }

            // rollback tồn: trừ ngược lại thay đổi đã áp dụng
            Integer cur = (book.getStockQty() == null ? 0 : book.getStockQty());
            int next = cur - m.getQuantityChange();
            book.setStockQty(next);
            em.merge(book);

            em.remove(m);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
