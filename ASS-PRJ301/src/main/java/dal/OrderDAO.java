// dal/OrderDAO.java
package dal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import model.OrderItemInput;
import model.Book;
import model.Order;
import model.OrderDetail;

public class OrderDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SUMMER2025");

    public Order checkout(Order order, List<OrderItemInput> items) {
        // 1) Kiểm tra đầu vào
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống.");
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 2) Điền mặc định để tránh NULL khi INSERT
            if (order.getOrderDate() == null) {
                order.setOrderDate(LocalDateTime.now());
            }
            if (order.getStatus() == null) {
                order.setStatus("PENDING");
            }
            if (order.getPaymentStatus() == null) {
                order.setPaymentStatus("UNPAID");
            }
            if (order.getTotal() == null) {
                order.setTotal(0f);
            }
            if (order.getDiscount() == null) {
                order.setDiscount(0f);
            }
            if (order.getGrandTotal() == null) {
                order.setGrandTotal(0f);
            }

            // 3) Lưu Order trước để có Id
            em.persist(order);

            BigDecimal total = BigDecimal.ZERO;

            // 4) Duyệt từng item: khóa sách → kiểm tồn → tính tiền → lưu chi tiết → trừ kho
            for (OrderItemInput it : items) {
                // 4.1) Lấy & khóa ghi
                Book book = em.find(Book.class, it.getBookId(), LockModeType.PESSIMISTIC_WRITE);
                if (book == null) {
                    throw new IllegalStateException("Book không tồn tại: " + it.getBookId());
                }
                if (!"ACTIVE".equalsIgnoreCase(book.getStatus())) {
                    throw new IllegalStateException("Book INACTIVE: " + it.getBookId());
                }
                if (book.getStockQty() < it.getQuantity()) {
                    throw new IllegalStateException("Không đủ tồn kho, còn " + book.getStockQty());
                }

                // 4.2) Xác định đơn giá (ưu tiên giá truyền vào; nếu không có thì lấy giá trong Book)
                BigDecimal unitPrice = (it.getUnitPrice() != null)
                        ? it.getUnitPrice()
                        : (book.getPrice() != null ? new BigDecimal(String.valueOf(book.getPrice())) : BigDecimal.ZERO);
                unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);

                // 4.3) Tính thành tiền dòng & tạo OrderDetail
                BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(it.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP);

                OrderDetail od = new OrderDetail();
                od.setOrder(order);
                od.setBook(book);
                od.setUnitPrice(unitPrice);
                od.setQuantity(it.getQuantity());
                od.setLineTotal(lineTotal);
                em.persist(od);

                // 4.4) Trừ tồn kho & cộng dồn tổng
                book.setStockQty(book.getStockQty() - it.getQuantity());
                em.merge(book);

                total = total.add(lineTotal);
            }

            // 5) Tính tổng/giảm giá/thanh toán & cập nhật Order
            BigDecimal discount = (order.getDiscount() == null)
                    ? BigDecimal.ZERO
                    : new BigDecimal(String.valueOf(order.getDiscount())).setScale(2, RoundingMode.HALF_UP);

            BigDecimal grand = total.subtract(discount).setScale(2, RoundingMode.HALF_UP);

            order.setTotal(total.setScale(2, RoundingMode.HALF_UP).floatValue());
            order.setDiscount(discount.floatValue());
            order.setGrandTotal(grand.floatValue());
            em.merge(order);

            tx.commit();
            return order;

        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Book> searchByTitle(String keyword) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Book> data = em.createQuery(
                    "SELECT b FROM Book b "
                    + "WHERE b.status = :status AND b.title LIKE :title",
                    Book.class
            )
                    .setParameter("status", "ACTIVE")
                    .setParameter("title", "%" + keyword + "%")
                    .getResultList();

            return data;
        } finally {
            em.close();
        }
    }

    public Book findByCode(String code) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Book b "
                    + "WHERE b.status = :status AND b.code = :code",
                    Book.class
            )
                    .setParameter("status", "ACTIVE")
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // không tìm thấy
        } finally {
            em.close();
        }
    }

//    public static void main(String[] args) {
//        OrderDAO dao = new OrderDAO();
//        Book list = dao.findByCode("BK-001");
//
//        System.out.println(list);
//
//    }
    // Trong dal/OrderDAO.java
    public Order findOrderById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public List<OrderDetail> findOrderDetails(Long orderId) {
        EntityManager em = emf.createEntityManager();
        try {
            // Lấy kèm Book để hiển thị (JOIN FETCH)
            return em.createQuery(
                    "SELECT od FROM OrderDetail od "
                    + "JOIN FETCH od.book "
                    + "WHERE od.order.id = :oid "
                    + "ORDER BY od.id",
                    OrderDetail.class
            ).setParameter("oid", orderId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    

public List<Order> getAllOrders() {
    EntityManager em = emf.createEntityManager();
    try {
        return em.createQuery(
                "SELECT o FROM Order o ORDER BY o.orderDate DESC",
                Order.class
        ).getResultList();
    } finally {
        em.close();
    }
}


}
