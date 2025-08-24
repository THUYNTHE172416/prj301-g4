package dal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import model.Promotion; // p.getType(): "PERCENT" | "FIXED"

public class PromotionDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SUMMER2025");

    /* ========== CRUD ĐƠN GIẢN ========== */
    public Promotion create(Promotion p) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (p.getCreatedAt() == null) {
                p.setCreatedAt(LocalDateTime.now());
            }
            p.setUpdatedAt(LocalDateTime.now());
            em.persist(p);
            tx.commit();
            return p;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Promotion getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Promotion.class, id);
        } finally {
            em.close();
        }
    }

    public Promotion update(Promotion p) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            p.setUpdatedAt(LocalDateTime.now());
            Promotion merged = em.merge(p);
            tx.commit();
            return merged;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Promotion p = em.find(Promotion.class, id);
            if (p != null) {
                em.remove(p);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public void setActive(Long id, boolean active) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Promotion p = em.find(Promotion.class, id, LockModeType.PESSIMISTIC_WRITE);
            if (p != null) {
                p.setActive(active);
                p.setUpdatedAt(LocalDateTime.now());
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
    public List<Promotion> getAll() {//lấy all cả promotion
    EntityManager em = emf.createEntityManager();
    try {
        return em.createQuery(
            "SELECT p FROM Promotion p ORDER BY p.updatedAt DESC",
            Promotion.class
        ).getResultList();
    } finally {
        em.close();
    }
}


    /**
     * Liệt kê có tìm kiếm theo code/name (không cần Optional/phân trang phức
     * tạp)
     */
    public List<Promotion> list(String keyword, int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            String kw = (keyword == null ? "" : keyword.trim().toLowerCase());
            return em.createQuery(
                    "SELECT p FROM Promotion p "
                    + "WHERE lower(p.code) LIKE :kw OR lower(p.name) LIKE :kw "
                    + "ORDER BY p.updatedAt DESC", Promotion.class)
                    .setParameter("kw", "%" + kw + "%")
                    .setMaxResults(Math.max(1, limit))
                    .getResultList();
        } finally {
            em.close();
        }
    }
    /* ========== GỢI Ý VOUCHER THEO TỔNG CHI TIÊU KHÁCH ========== */

    // Tier mẫu: ≥1.000.000 → 10%, ≥3.000.000 → 15%, ≥5.000.000 → 20%
    private static final BigDecimal T1 = new BigDecimal("1000000");
    private static final BigDecimal T2 = new BigDecimal("3000000");
    private static final BigDecimal T3 = new BigDecimal("5000000");

    /**
     * Tổng chi tiêu đã thanh toán của KH (dựa vào Orders.PAID)
     */
    public BigDecimal getCustomerTotalSpent(Long customerId) {
        if (customerId == null) {
            return BigDecimal.ZERO;
        }
        EntityManager em = emf.createEntityManager();
        try {
            // Nếu Order.grandTotal là Float: trả về Double -> BigDecimal
            Double sum = em.createQuery(
                    "SELECT COALESCE(SUM(o.grandTotal), 0) FROM Order o "
                    + "WHERE o.customerId = :cid AND o.paymentStatus = 'PAID'",
                    Double.class)
                    .setParameter("cid", customerId)
                    .getSingleResult();
            return BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP);
        } finally {
            em.close();
        }
    }

    /**
     * Trả về % giảm theo tổng chi tiêu (0 nếu chưa đạt)
     */
    public BigDecimal suggestPercentBySpent(Long customerId) {
        BigDecimal spent = getCustomerTotalSpent(customerId);
        if (spent.compareTo(T3) >= 0) {
            return new BigDecimal("20");
        }
        if (spent.compareTo(T2) >= 0) {
            return new BigDecimal("15");
        }
        if (spent.compareTo(T1) >= 0) {
            return new BigDecimal("10");
        }
        return BigDecimal.ZERO;
    }
}
