package se.yrgo.libraryapp.entities.forms;

import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.UserId;

public class LoanData {
    private BookId book;
    private UserId user;

    public LoanData(BookId book, UserId user) {
        this.book = book;
        this.user = user;
    }

    public BookId getBook() {
        return book;
    }

    public UserId getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
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
        LoanData other = (LoanData) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        }
        else if (!book.equals(other.book))
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
        return "LoanData [book=" + book + ", user=" + user + "]";
    }
}
