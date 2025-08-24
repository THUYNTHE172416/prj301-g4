package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "OrderCode", length = 50, nullable = false)
    private String orderCode;

    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Column(name = "PaymentStatus", length = 50)
    private String paymentStatus;

    @Column(name = "Note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(name = "Total")
    private Float total;

    @Column(name = "Discount")
    private Float discount;

    @Column(name = "GrandTotal")
    private Float grandTotal;

    @Column(name = "CashierUserId")
    private Long cashierUserId;

    @Column(name = "CustomerId")
    private Long customerId;

    // Quan hệ tới bảng nối
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPromotion> orderPromotions = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, String orderCode, LocalDateTime orderDate, String status,
                 String paymentMethod, String paymentStatus, String note,
                 Float total, Float discount, Float grandTotal,
                 Long cashierUserId, Long customerId) {
        this.id = id;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.note = note;
        this.total = total;
        this.discount = discount;
        this.grandTotal = grandTotal;
        this.cashierUserId = cashierUserId;
        this.customerId = customerId;
    }

    // Helper để đồng bộ 2 chiều
// Helper để đồng bộ 2 chiều
    public void addPromotion(Promotion p) {
        OrderPromotion op = new OrderPromotion();
        op.setOrder(this);
        op.setPromotion(p);
        this.orderPromotions.add(op);
        p.getOrderPromotions().add(op);
    }

    public void removePromotion(Promotion p) {
        orderPromotions.removeIf(op -> {
            boolean match = op.getPromotion() != null && op.getPromotion().equals(p);
            if (match) {
                p.getOrderPromotions().remove(op);
                op.setOrder(null);
                op.setPromotion(null);
            }
            return match;
        });
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Long getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(Long cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderPromotion> getOrderPromotions() {
        return orderPromotions;
    }

    public void setOrderPromotions(List<OrderPromotion> orderPromotions) {
        this.orderPromotions = orderPromotions;
    }
}
