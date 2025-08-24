package dal;

import jakarta.persistence.*;
import java.util.List;
import model.Publisher;

public class PublisherDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public List<Publisher> getAllPublisher() {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Publisher> data = em.createQuery("SELECT p FROM Publisher p", Publisher.class )
                .getResultList();
        em.close();
        return data;
    }
    
    public Publisher getPublisherById(int id) {
        EntityManager em = emf.createEntityManager();
        Publisher data = em.find(Publisher.class, id);
        em.close();
        return data;
    }
}
