
package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Categories")//nếu không có tự động là Category
public class Categories {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //nếu không viết tự động tên thuộc tính
    @Column(name = "name", columnDefinition = "NVARCHAR(50)")//tiếng việt
    private String name;
    @Column(name = "slug", columnDefinition = "NVARCHAR(50)")
    private String slug;
    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;
    @Column(name = "createdAt", columnDefinition = "DATETIME2(0)")
    private Date createdAt;
    @Column(name = "updatedAt", columnDefinition = "DATETIME2(0)")
    private Date updatedAt;
    
    public Categories() {
    }

    public Categories(int id, String name, String slug, String description, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    
}
