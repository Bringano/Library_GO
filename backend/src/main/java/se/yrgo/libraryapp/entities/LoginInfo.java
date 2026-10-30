package se.yrgo.libraryapp.entities;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

public class LoginInfo {
    private UserId userId;
    private String passwordHash;
    
    public LoginInfo(UserId userId, String passwordHash) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(passwordHash);

        this.userId = userId;
        this.passwordHash = passwordHash;
    }

    public UserId getUserId() {
        return userId;
    }

    @JsonIgnore // we don't want to accidently serialize this
    public String getPasswordHash() {
        // make sure we can only read this once to prevent
        // it from escaping into logs and other places
        String hash = passwordHash;
        passwordHash = null;
        return hash;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((passwordHash == null) ? 0 : passwordHash.hashCode());
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
        LoginInfo other = (LoginInfo) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (passwordHash == null) {
            if (other.passwordHash != null)
                return false;
        } else if (!passwordHash.equals(other.passwordHash))
            return false;
        return true;
    }
}
