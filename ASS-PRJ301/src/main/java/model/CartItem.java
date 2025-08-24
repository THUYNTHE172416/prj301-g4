package model;

import java.math.BigDecimal;

public class CartItem {

    private Long bookId;
    private String code;
    private String title;
    private BigDecimal unitPrice;  // đơn giá
    private int qty;               // số lượng
    private BigDecimal lineTotal;  // tổng tiền cho sản phẩm (unitPrice * qty)

    public CartItem(Long bookId, String code, String title, BigDecimal unitPrice, int qty) {
        this.bookId = bookId;
        this.code = code;
        this.title = title;
        this.unitPrice = unitPrice;
        this.qty = qty;
        recalcLineTotal(); // tính ngay khi tạo
    }

    /**
     * Tính lại tổng tiền = đơn giá * số lượng
     */
    public void recalcLineTotal() {
        if (unitPrice != null) {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
        } else {
            this.lineTotal = BigDecimal.ZERO;
        }
    }

    // --- getters & setters ---
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        recalcLineTotal(); // cập nhật lại
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
        recalcLineTotal(); // cập nhật lại
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
