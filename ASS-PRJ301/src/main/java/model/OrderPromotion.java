package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "OrderPromotions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"OrderId", "PromotionId"})
)
public class OrderPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id; // khóa thay thế (surrogate key)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderId", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PromotionId", nullable = false)
    private Promotion promotion;

    // cột bổ sung (tùy bạn)
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    public OrderPromotion() {
    }

    public OrderPromotion(Order order, Promotion promotion) {
        this.order = order;
        this.promotion = promotion;
    }

    // Getters / Setters
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

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
