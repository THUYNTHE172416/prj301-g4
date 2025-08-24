package model;

import java.io.Serializable;

public class ReportDto implements Serializable {

    private String title;
    // Sửa: Thay Long bằng long (kiểu nguyên thủy)
    private long quantity;
    // Sửa: Thay Double bằng double (kiểu nguyên thủy)
    private double lineTotal;

    public ReportDto() {
    }

    // Constructor để ánh xạ kết quả truy vấn JPQL
    public ReportDto(String title, long quantity, double lineTotal) {
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

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}