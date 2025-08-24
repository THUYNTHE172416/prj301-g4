
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Users; // Đúng: Đã đổi tên thành Users

import java.util.Optional;

public class UserDao {

    private final EntityManagerFactory emf;

    public UserDao() {
        // Tên của persistence unit trong persistence.xml
        this.emf = Persistence.createEntityManagerFactory("SUMMER2025");
    }

    public Optional<Users> findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            // SỬA LẠI DÒNG NÀY:
            // Thay đổi "FROM User" thành "FROM Users" để khớp với tên entity của bạn
            TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            
            Users user = query.setParameter("username", username)
                                .getSingleResult();
            return Optional.of(user);
        } catch (jakarta.persistence.NoResultException e) {
            // Không tìm thấy người dùng, trả về Optional.empty()
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public void save(Users user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            // Sử dụng persist để lưu đối tượng mới
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}



















