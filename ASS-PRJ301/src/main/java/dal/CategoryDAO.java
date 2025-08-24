package dal;

import jakarta.persistence.*;
import java.util.List;
import model.Book;
import model.Category;

public class CategoryDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public List<Category> getAllCategory() {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Category> data = em.createQuery(
                "SELECT c FROM Category c", Category.class)
                .getResultList();
        em.close();
        return data;
    }
    
    public void addNewCategory(Category c) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    public Category getCategoryById(int categoryId) {
        EntityManager em = emf.createEntityManager();
        Category category = em.find(Category.class, categoryId);
        em.close();
        return category;
    }
    
    public void updateCategory(Category category) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(category);
        em.getTransaction().commit();
        em.close();
    }
}
