package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.ReportDto;

public class ReportDAO {

    private final EntityManagerFactory emf;

    public ReportDAO() {
        this.emf = Persistence.createEntityManagerFactory("SUMMER2025");
    }

    public List<ReportDto> getTopSellingBooks() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ReportDto> query = em.createQuery(
                "SELECT new model.ReportDto(od.book.title, SUM(od.quantity), SUM(od.lineTotal)) " +
                "FROM OrderDetail od " +
                "WHERE od.order.status = 'PAID' " +
                "GROUP BY od.book.title " +
                "ORDER BY SUM(od.quantity) DESC", ReportDto.class);
            query.setMaxResults(5);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Object[]> getDailyRevenues() {
        EntityManager em = emf.createEntityManager();
        try {
            // Thay đổi hàm JPQL để sử dụng FUNCTION('FORMAT', ...)
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT FUNCTION('FORMAT', o.orderDate, 'yyyy-MM-dd'), SUM(o.grandTotal) " +
                "FROM Order o WHERE o.status = 'PAID' " +
                "GROUP BY FUNCTION('FORMAT', o.orderDate, 'yyyy-MM-dd') " +
                "ORDER BY FUNCTION('FORMAT', o.orderDate, 'yyyy-MM-dd') DESC", Object[].class);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    
    public List<Object[]> getMonthlyRevenues() {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT FUNCTION('FORMAT', o.orderDate, 'yyyy-MM'), SUM(o.grandTotal) " +
            "FROM Order o WHERE o.status = 'PAID' " +
            "GROUP BY FUNCTION('FORMAT', o.orderDate, 'yyyy-MM') " +
            "ORDER BY FUNCTION('FORMAT', o.orderDate, 'yyyy-MM') DESC", Object[].class);
        return query.getResultList();
    } finally {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

    public List<Object[]> getSalesByEmployeeAndBook() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT od.order.cashierUser.fullName, od.book.title, SUM(od.quantity), SUM(od.lineTotal) " +
                "FROM OrderDetail od " +
                "WHERE od.order.status = 'PAID' " +
                "GROUP BY od.order.cashierUser.fullName, od.book.title " +
                "ORDER BY od.order.cashierUser.fullName, SUM(od.lineTotal) DESC", Object[].class);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}