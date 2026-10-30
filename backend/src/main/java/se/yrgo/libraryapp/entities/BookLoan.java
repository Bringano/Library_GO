package se.yrgo.libraryapp.entities;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BookLoan {
    private Book book;
    private UserId user;
    private LocalDate dueDate;

    public BookLoan(Book book, UserId user, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.dueDate = dueDate;
    }

    public Book getBook() {
        return book;
    }

    public UserId getUser() {
        return user;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean getOverdue() {
        return dueDate.isBefore(LocalDate.now());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookLoan other = (BookLoan) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        }
        else if (!book.equals(other.book))
            return false;
        if (dueDate == null) {
            if (other.dueDate != null)
                return false;
        }
        else if (!dueDate.equals(other.dueDate))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        }
        else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BookLoan [book=" + book + ", dueDate=" + dueDate + ", user=" + user + "]";
    }
}
