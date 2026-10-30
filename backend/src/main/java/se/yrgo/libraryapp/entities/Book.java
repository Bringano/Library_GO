package se.yrgo.libraryapp.entities;

public class Book {
    private BookId id;
    private boolean available;
    private BookEdition edition;

    public Book(BookId id, boolean available, BookEdition edition) {
        this.id = id;
        this.available = available;
        this.edition = edition;
    }

    public BookId getId() {
        return id;
    }

    public boolean getAvailable() {
        return available;
    }

    public BookEdition getEdition() {
        return edition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (available ? 1231 : 1237);
        result = prime * result + ((edition == null) ? 0 : edition.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Book other = (Book) obj;
        if (available != other.available)
            return false;
        if (edition == null) {
            if (other.edition != null)
                return false;
        }
        else if (!edition.equals(other.edition))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Book [available=" + available + ", edition=" + edition + ", id=" + id + "]";
    }
}
