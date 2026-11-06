package se.yrgo.libraryapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder mockEncoder;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);

        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    @SuppressWarnings("deprecation")
    void unknownUsernameGivesEmpty() {
        final String username = "testuser";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username, "password")).isEmpty();
    }

    @Test
    @SuppressWarnings("deprecation")
    void wrongPasswordGivesEmpty() {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final LoginInfo info = new LoginInfo(id, "password");
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username, "wrongpassword")).isEmpty();
    }

    @Test
    void registerSendsHashToDao() {
        when(mockEncoder.encode("secret")).thenReturn("hashed-secret");

        UserService userService = new UserService(userDao, mockEncoder);
        userService.register("testuser", "Test User", "secret");

        verify(userDao).register("testuser", "Test User", "hashed-secret");
    }

    @Test
    void registerReturnsDaoResult() {
        when(mockEncoder.encode("secret")).thenReturn("hashed-secret");
        when(userDao.register("testuser", "Test User", "hashed-secret")).thenReturn(true);

        UserService userService = new UserService(userDao, mockEncoder);

        assertThat(userService.register("testuser", "Test User", "secret")).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "ab", "  ab  ", "     " })
    void tooShortNameIsNotAvailable(String name) {
        UserService userService = new UserService(userDao, mockEncoder);

        assertThat(userService.isNameAvailable(name)).isFalse();
        verify(userDao, never()).isNameAvailable(any());
    }

    @Test
    void validNameIsCheckedInDao() {
        when(userDao.isNameAvailable("abcd")).thenReturn(true);

        UserService userService = new UserService(userDao, mockEncoder);

        assertThat(userService.isNameAvailable("abcd")).isTrue();
    }
}
