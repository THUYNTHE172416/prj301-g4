package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "InventoryMovements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    // FK -> Books(Id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookId", nullable = false)
    private Book book;

    @Column(name = "Type", nullable = false, length = 50)
    private String type;   // ví dụ: "IMPORT", "EXPORT", "ADJUSTMENT"

    @Column(name = "QuantityChange", nullable = false)
    private Integer quantityChange;

    @Column(name = "Reason", length = 255)
    private String reason;

    // FK -> Orders(Id), có thể null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId")
    private Order order;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    public InventoryMovement() {
    }

    public InventoryMovement(Long id, Book book, String type,
            Integer quantityChange, String reason,
            Order order, LocalDateTime createdAt) {
        this.id = id;
        this.book = book;
        this.type = type;
        this.quantityChange = quantityChange;
        this.reason = reason;
        this.order = order;
        this.createdAt = createdAt;
    }

    // ----- Getters / Setters -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
