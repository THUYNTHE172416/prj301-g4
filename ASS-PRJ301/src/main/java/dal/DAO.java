
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import model.Category;


public class DAO {
    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SUMMER2025");
    
    public void addStudent(Category c) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    public List<Category> getCategories() {
        EntityManager em = emf.createEntityManager();
        List<Category> list = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        em.close();
        return list;
    }
}
