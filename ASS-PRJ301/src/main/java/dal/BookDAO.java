/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.*;
import java.util.List;
import model.Book;

/**
 *
 * @author DELL
 */
public class BookDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public int getTotalBooks() {
        EntityManager em = emf.createEntityManager();
        Long count = em.createQuery("select count(b) from Book b", Long.class).getSingleResult();
        em.close();
        return count.intValue();
    }

    public List<Book> getAllBook(int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        List<Book> data = em.createQuery("SELECT b FROM Book b", Book.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        em.close();
        return data;
    }

    public Book getBookById(int bookId) {
        EntityManager em = emf.createEntityManager();
        Book b = em.find(Book.class, bookId);
        em.close();
        return b;
    }

    public Book getBookById(Long bookId) {
        EntityManager em = emf.createEntityManager();
        Book b = em.find(Book.class, bookId);
        em.close();
        return b;
    }

    /**
     * change status book to inactive
     */
    public void deleteBook(int bookId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Book b = getBookById(bookId);
        b.setStatus("INACTIVE");
        em.merge(b); // update
        em.getTransaction().commit();
        em.close();
    }

    public boolean findCode(String code) {
        EntityManager em = emf.createEntityManager();
        List<Book> data = em.createQuery("SELECT b FROM Book b "
                + "WHERE (b.code = :c )",
                Book.class)
                .setParameter("c", code)
                .getResultList();

        return data.size() > 0;
    }

    public boolean updateBook(Book book) {
        if (!findCodeAndISBN(book) && findCode(book.getCode())) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(book); // update
            em.getTransaction().commit();
            em.close();
            return true;
        }
        return false;
    }

    public boolean findCodeAndISBN(Book b) {
        EntityManager em = emf.createEntityManager();
        List<Book> data = em.createQuery("SELECT b FROM Book b "
                + "WHERE (b.code = :c or b.isbn = :isbn or b.title = :title) AND b.id <> :id",
                Book.class)
                .setParameter("c", b.getCode())
                .setParameter("isbn", b.getIsbn())
                .setParameter("title", b.getTitle().trim())
                .setParameter("id", b.getId() == null ? -1L : b.getId())
                .getResultList();

        return data.size() > 0;
    }

    public boolean addNewBook(Book book) {
        EntityManager em = emf.createEntityManager();
        boolean check = findCodeAndISBN(book);
        if (!check) {
            em.getTransaction().begin();
            em.persist(book); // add new
            em.getTransaction().commit();
            em.close();
            return true;
        }
        return false;
    }

    public List<Book> getAllBookByKeyword(String search) {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Book> data = em.createQuery(
                "SELECT b FROM Book b WHERE b.status = :status and (b.title like :kw or b.code like :kw)",
                Book.class
        )
                .setParameter("status", "ACTIVE")
                .setParameter("kw", "%" + search + "%")
                .getResultList();

        em.close();
        return data;
    }

}
