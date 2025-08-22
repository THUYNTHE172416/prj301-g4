/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    // Khóa ngoại sang Users (thu ngân)
    @Column(name = "CashierUserId")
    private Long cashierUserId;

    // Khóa ngoại sang Customers
    @Column(name = "CustomerId")
    private Long customerId;

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
}
