package model;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderDetails")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    // FK -> Orders(Id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderId", nullable = false)
    private Order order;

    // FK -> Books(Id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookId", nullable = false)
    private Book book;

    @Column(name = "UnitPrice", nullable = false)
    private Float unitPrice;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "LineTotal", nullable = false)
    private Float lineTotal;

    public OrderDetail() {
    }

    public OrderDetail(Long id, Order order, Book book,
            Float unitPrice, Integer quantity, Float lineTotal) {
        this.id = id;
        this.order = order;
        this.book = book;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    // ----- Convenience: tự tính lineTotal nếu chưa set -----
    @PrePersist
    @PreUpdate
    private void calcLineTotal() {
        if (unitPrice != null && quantity != null && (lineTotal == null)) {
            this.lineTotal = unitPrice * quantity;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(Float lineTotal) {
        this.lineTotal = lineTotal;
    }
}
