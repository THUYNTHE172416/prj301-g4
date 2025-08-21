
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import model.Categories;


public class DAO {
    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SUMMER2025");
    
    public void addStudent(Categories c) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    public List<Categories> getCategories() {
        EntityManager em = emf.createEntityManager();
        List<Categories> list = em.createQuery("SELECT c FROM Categories c", Categories.class).getResultList();
        em.close();
        return list;
    }
}
