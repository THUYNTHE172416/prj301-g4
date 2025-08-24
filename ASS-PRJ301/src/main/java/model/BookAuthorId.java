package model;

import java.io.Serializable;
import java.util.Objects;

public class BookAuthorId implements Serializable {
    private Long book;   // phải trùng tên field trong entity BookAuthor
    private Long author; // phải trùng tên field trong entity BookAuthor

    public BookAuthorId() {}

    public BookAuthorId(Long book, Long author) {
        this.book = book;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookAuthorId)) return false;
        BookAuthorId that = (BookAuthorId) o;
        return Objects.equals(book, that.book) &&
               Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, author);
    }
}

