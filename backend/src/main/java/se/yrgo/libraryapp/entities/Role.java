package se.yrgo.libraryapp.entities;

public enum Role {
    ADMIN,
    USER;

    public static Role fromString(String roleName) {
        if (roleName == null) {
            throw new IllegalArgumentException("role name can't be null");
        }
        
        return Role.valueOf(roleName.trim().toUpperCase());
    }
}
