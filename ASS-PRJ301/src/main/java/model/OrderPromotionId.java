package model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderPromotionId implements Serializable {

    private Long orderId;
    private Long promotionId;

    public OrderPromotionId() {}

    public OrderPromotionId(Long orderId, Long promotionId) {
        this.orderId = orderId;
        this.promotionId = promotionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderPromotionId)) return false;
        OrderPromotionId that = (OrderPromotionId) o;
        return Objects.equals(orderId, that.orderId) &&
               Objects.equals(promotionId, that.promotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, promotionId);
    }
}
