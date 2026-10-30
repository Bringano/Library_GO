package se.yrgo.libraryapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public class BookId {
    private int id;

    public BookId(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookId other = (BookId) obj;
        return (id == other.id);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public static BookId of(int id) {
        return new BookId(id);
    }

    public static BookId of(String id) {
        return new BookId(Integer.parseInt(id));
    }
}
