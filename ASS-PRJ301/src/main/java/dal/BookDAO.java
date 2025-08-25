/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import model.Author;
import model.Book;
import model.BookAuthor;

/**
 *
 * @author DELL
 */
public class BookDAO {

    // khoi tao 1 entity manager factory de tao cac entity manager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SUMMER2025");

    public List<Book> getAllBook() {
        // khoi tao entity manager de thao tac voi db
        EntityManager em = emf.createEntityManager();
        // lay het tat ca giu lieu bang book
        List<Book> data = em.createQuery(
                "SELECT b FROM Book b WHERE b.status = :s", Book.class)
                .setParameter("s", "ACTIVE")
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

    public boolean updateBook(Book book) {
        if (!findCodeAndISBN(book)) {
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
                + "WHERE (b.code = :c or b.isbn = :isbn) AND b.id <> :id",
                Book.class)
                .setParameter("c", b.getCode())
                .setParameter("isbn", b.getIsbn())
                .setParameter("id", b.getId())
                .getResultList();

        return data.size() > 0;
    }

    
    public boolean addNewBook(Book book) {
        EntityManager em = emf.createEntityManager();
        if (!findCodeAndISBN(book)) {
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
