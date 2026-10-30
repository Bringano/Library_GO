package se.yrgo.libraryapp.services;

import java.util.Optional;
import javax.inject.Inject;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

public class UserService {
    private UserDao userDao;

    @Inject
    UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<UserId> validate(String username, String password) {
        Optional<LoginInfo> maybeLoginInfo = userDao.getLoginInfo(username);
        if (maybeLoginInfo.isEmpty()) {
            return Optional.empty();
        }

        LoginInfo loginInfo = maybeLoginInfo.get();

        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        if (!encoder.matches(password, loginInfo.getPasswordHash())) {
            return Optional.empty();
        }

        return Optional.of(loginInfo.getUserId());
    }        
}
