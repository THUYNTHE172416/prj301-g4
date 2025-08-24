// dal/dto/OrderItemInput.java
package model;

import java.math.BigDecimal;

public class OrderItemInput {

    private Long bookId;
    private int quantity;
    private BigDecimal unitPrice; // nếu null sẽ lấy giá từ Book

    public OrderItemInput() {
    }

    public OrderItemInput(Long bookId, int quantity, BigDecimal unitPrice) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
