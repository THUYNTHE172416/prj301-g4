package dal;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;

public class CategoryDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public List<Category> getAllCategory() {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Category> data = em.createQuery(
                "SELECT c FROM Category c WHERE c.status = :status", Category.class)
                .setParameter("status", "ACTIVE")
                .getResultList();
        em.close();
        return data;
    }

    private boolean checkValid(Category c) {
        List<Category> data = new ArrayList<>();

        try (EntityManager em = emf.createEntityManager()) {
            data = em.createQuery(
                    "SELECT c FROM Category c "
                    + "WHERE (c.name = :name OR c.slug = :slug) "
                    + "AND c.id <> :id",
                    Category.class)
                    .setParameter("name", c.getName())
                    .setParameter("slug", c.getSlug())
                    .setParameter("id", c.getId() == null ? -1L : c.getId())
                    .getResultList();
        }
        return !data.isEmpty();
    }

    public boolean addNewCategory(Category c) {
        if (!checkValid(c)) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.persist(c);
                em.getTransaction().commit();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public Category getCategoryById(int categoryId) {
        EntityManager em = emf.createEntityManager();
        Category category = em.find(Category.class, categoryId);
        em.close();
        return category;
    }

    public boolean updateCategory(Category category) {
        if (!checkValid(category)) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.merge(category);
                em.getTransaction().commit();
                em.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteCategory(int categoryId) {
        try (EntityManager em = emf.createEntityManager()) {
            Category category = getCategoryById(categoryId);
            category.setStatus("INACTIVE");
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
