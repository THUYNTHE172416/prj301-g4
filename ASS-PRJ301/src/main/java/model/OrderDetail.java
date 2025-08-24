package model;

import jakarta.persistence.*;
import java.math.BigDecimal;

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
    private BigDecimal unitPrice;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "LineTotal", nullable = false)
    private BigDecimal lineTotal;

    public OrderDetail() {
    }

    public OrderDetail(Long id, Order order, Book book,
            BigDecimal unitPrice, Integer quantity, BigDecimal lineTotal) {
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
    if (unitPrice != null && quantity != null && lineTotal == null) {
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
