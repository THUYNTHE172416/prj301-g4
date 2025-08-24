package model;
import jakarta.persistence.*;
@Entity
@IdClass(BookAuthorId.class)
@Table(name = "BookAuthors")
public class BookAuthor {
    @Id
    @ManyToOne
    @JoinColumn(name = "BookId")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "AuthorId")
    private Author author;

    public BookAuthor() {}

    public BookAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
