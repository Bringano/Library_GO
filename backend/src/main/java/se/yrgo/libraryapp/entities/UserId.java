package se.yrgo.libraryapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public class UserId {
    private int id;

    public UserId(int id) {
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
        UserId other = (UserId) obj;
        return (id == other.id);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public static UserId of(int id) {
        return new UserId(id);
    }

    public static UserId of(String id) {
        return new UserId(Integer.parseInt(id));
    }
}
