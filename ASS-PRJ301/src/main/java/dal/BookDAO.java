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
                "SELECT b FROM Book b WHERE b.status = :status",
                Book.class
        ).setParameter("status", "ACTIVE")
                .getResultList();

        em.close();
        return data;
    }

    public String getAllAuthorByBookId(int bookId) {
        // khởi tạo entity manager để thao tác với db
        EntityManager em = emf.createEntityManager();

        // JPQL: lấy ra Author thông qua join với BookAuthor và Book
        List<Author> data = em.createQuery(
                "SELECT a FROM Author a "
                + "JOIN BookAuthor ba ON ba.author = a "
                + "JOIN ba.book b "
                + "WHERE b.id = :bookId", Author.class)
                .setParameter("bookId", (long) bookId) // ép kiểu sang long vì id trong entity là Long
                .getResultList();
        em.close();
        // nối tên các tác giả bằng dấu cách
        return data.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

    public Book getById(int bookId) {
        EntityManager em = emf.createEntityManager();
        Book b = em.find(Book.class, bookId);
        em.close();
        return b;
    }

    public void deleteBook(int bookId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Book b = getById(bookId);
        b.setStatus("INACTIVE");
        em.merge(b);
        em.getTransaction().commit();
        em.close();
    }
}
