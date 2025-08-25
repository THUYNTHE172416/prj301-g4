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
            // Lấy OrderDetails trước
            List<OrderDetail> details = em.createQuery(
                    "SELECT od FROM OrderDetail od "
                    + "WHERE od.order.id = :oid "
                    + "ORDER BY od.id",
                    OrderDetail.class
            ).setParameter("oid", orderId)
                    .getResultList();
            
            // Load Book cho từng OrderDetail
            for (OrderDetail detail : details) {
                if (detail.getBook() != null) {
                    // Force load Book entity bằng cách gọi getter
                    Book book = em.find(Book.class, detail.getBook().getId());
                    if (book != null) {
                        // Tạo Book mới và set vào detail
                        Book loadedBook = new Book();
                        loadedBook.setId(book.getId());
                        loadedBook.setCode(book.getCode());
                        loadedBook.setTitle(book.getTitle());
                        detail.setBook(loadedBook);
                    }
                }
            }
            
            return details;
        } finally {
            em.close();
        }
    }
    

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
    public List<Order> getAllOrders() {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("DEBUG: getAllOrders - EntityManager created");
            List<Order> result = em.createQuery(
                    "SELECT o FROM Order o ORDER BY o.orderDate DESC",
                    Order.class
            ).getResultList();
            System.out.println("DEBUG: getAllOrders - Query executed, result size: " + (result != null ? result.size() : "null"));
            if (result != null && !result.isEmpty()) {
                System.out.println("DEBUG: getAllOrders - First order: " + result.get(0));
                System.out.println("DEBUG: getAllOrders - First orderDate: " + result.get(0).getOrderDate());
            }
            return result;
        } catch (Exception e) {
            System.err.println("ERROR in getAllOrders: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

// Tìm kiếm đơn hàng theo nhiều tiêu chí
public List<Order> searchOrders(String keyword, String status, String dateFrom, String dateTo) {
    EntityManager em = emf.createEntityManager();
    try {
        StringBuilder jpql = new StringBuilder("SELECT o FROM Order o WHERE 1=1");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            jpql.append(" AND (o.orderCode LIKE :keyword OR o.note LIKE :keyword)");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            jpql.append(" AND o.status = :status");
        }
        
        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            jpql.append(" AND o.orderDate >= :dateFrom");
        }
        
        if (dateTo != null && !dateTo.trim().isEmpty()) {
            jpql.append(" AND o.orderDate <= :dateTo");
        }
        
        jpql.append(" ORDER BY o.orderDate DESC");
        
        jakarta.persistence.TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("keyword", "%" + keyword.trim() + "%");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            query.setParameter("status", status.trim());
        }
        
        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            query.setParameter("dateFrom", LocalDate.parse(dateFrom).atStartOfDay());
        }
        
        if (dateTo != null && !dateTo.trim().isEmpty()) {
            query.setParameter("dateTo", LocalDate.parse(dateTo).atTime(23, 59, 59));
        }
        
        return query.getResultList();
        
    } finally {
        em.close();
    }
}

// Thống kê tổng số đơn hàng theo ngày
public int getTotalOrdersByDate(LocalDate date) {
    EntityManager em = emf.createEntityManager();
    try {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        Long count = em.createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :start AND o.orderDate <= :end",
                Long.class
        )
        .setParameter("start", startOfDay)
        .setParameter("end", endOfDay)
        .getSingleResult();
        
        return count != null ? count.intValue() : 0;
        
    } finally {
        em.close();
    }
}

// Thống kê tổng số sách bán ra theo ngày
public int getTotalBooksSoldByDate(LocalDate date) {
    EntityManager em = emf.createEntityManager();
    try {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        Long totalQuantity = em.createQuery(
                "SELECT SUM(od.quantity) FROM OrderDetail od " +
                "JOIN od.order o " +
                "WHERE o.orderDate >= :start AND o.orderDate <= :end",
                Long.class
        )
        .setParameter("start", startOfDay)
        .setParameter("end", endOfDay)
        .getSingleResult();
        
        return totalQuantity != null ? totalQuantity.intValue() : 0;
        
    } finally {
        em.close();
    }
}

// Thống kê tổng doanh thu theo ngày
public double getTotalRevenueByDate(LocalDate date) {
    EntityManager em = emf.createEntityManager();
    try {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        Double totalRevenue = em.createQuery(
                "SELECT SUM(o.grandTotal) FROM Order o " +
                "WHERE o.orderDate >= :start AND o.orderDate <= :end",
                Double.class
        )
        .setParameter("start", startOfDay)
        .setParameter("end", endOfDay)
        .getSingleResult();
        
        return totalRevenue != null ? totalRevenue : 0.0;
        
    } finally {
        em.close();
    }
}

// Method test để kiểm tra dữ liệu
public void testOrderDetails(Long orderId) {
    EntityManager em = emf.createEntityManager();
    try {
        // Kiểm tra Order có tồn tại không
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            System.out.println("Order " + orderId + " không tồn tại!");
            return;
        }
        System.out.println("Order found: " + order.getOrderCode());
        
        // Kiểm tra OrderDetails
        List<OrderDetail> details = em.createQuery(
                "SELECT od FROM OrderDetail od WHERE od.order.id = :oid",
                OrderDetail.class
        ).setParameter("oid", orderId).getResultList();
        
        System.out.println("Found " + details.size() + " order details");
        
        for (OrderDetail detail : details) {
            System.out.println("Detail ID: " + detail.getId());
            System.out.println("Book ID from detail: " + detail.getBook().getId());
            
            // Kiểm tra Book riêng biệt
            Book book = em.find(Book.class, detail.getBook().getId());
            if (book != null) {
                System.out.println("Book found: " + book.getTitle() + " (" + book.getCode() + ")");
            } else {
                System.out.println("Book not found!");
            }
        }
        
    } finally {
        em.close();
    }
}

}
