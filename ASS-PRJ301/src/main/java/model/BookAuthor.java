package model;
import jakarta.persistence.*;

@Entity
@Table(name="BookAuthors")
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "BookId")
    private Book book;
    
    @ManyToOne
    @JoinColumn(name = "AuthorId")
    private Author author;

    public BookAuthor() {
    }

    public BookAuthor(Long id, Book book, Author author) {
        this.id = id;
        this.book = book;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
