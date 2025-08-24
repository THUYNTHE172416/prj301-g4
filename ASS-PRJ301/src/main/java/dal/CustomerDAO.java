/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Optional;
import model.Customer;

/**
 *
 * @author Nguyen Van Manh
 */
public class CustomerDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SUMMER2025");

    public Customer findByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }

        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Customer c WHERE c.phone = :p",
                    Customer.class
            )
                    .setParameter("p", phone.trim())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // không tìm thấy thì trả về null
        } finally {
            em.close();
        }
    }

    public List<Customer> findByName(String name) {

        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Customer c "
                    + "WHERE LOWER(c.fullName) LIKE :n "
                    + "ORDER BY c.fullName ASC",
                    Customer.class
            )
                    .setParameter("n", "%" + name + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Customer> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        EntityManager em = emf.createEntityManager();
        try {
            Customer c = em.find(Customer.class, id);
            return Optional.ofNullable(c);
        } finally {
            em.close();
        }
    }
}
