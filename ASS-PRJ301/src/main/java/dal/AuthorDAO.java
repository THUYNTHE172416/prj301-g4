package dal;

import jakarta.persistence.*;
import java.util.List;
import model.Author;

public class AuthorDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public List<Author> getAllAuthor() {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Author> data = em.createQuery("SELECT a FROM Author a", Author.class)
                .getResultList();

        em.close();
        return data;
    }

    public Author getAuthorById(int id) {
        EntityManager em = emf.createEntityManager();
        Author data = em.find(Author.class, id);
        em.close();
        return data;
    }
}
