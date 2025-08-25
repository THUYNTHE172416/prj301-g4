package model;

import java.io.Serializable;
import java.math.BigDecimal; // Thêm import này

public class ReportDto implements Serializable {

    private String title;
    private long quantity;
    private BigDecimal lineTotal; // Thay đổi kiểu dữ liệu thành BigDecimal

    public ReportDto() {
    }

    public ReportDto(String title, long quantity, BigDecimal lineTotal) {
        this.title = title;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}