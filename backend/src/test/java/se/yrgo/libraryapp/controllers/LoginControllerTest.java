package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.StatusCode;
import org.pac4j.core.exception.CredentialsException;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleDao roleDao;

    @Mock
    private SessionDao sessionDao;

    @Mock
    private Context context;

    // Ej inloggad + rätt lösenord = ny session skapas, cookie sätts, användarens roll(er) returneras
    @Test
    void correctLoginGivesRolesAndCookie() {
        final UserId id = UserId.of(1);
        final UUID sessionId = UUID.randomUUID();
        final LoginData login = new LoginData("testuser", "password");

        when(userService.validate("testuser", "password")).thenReturn(Optional.of(id));
        when(sessionDao.create(id)).thenReturn(sessionId);
        when(roleDao.get(id)).thenReturn(List.of(Role.USER));

        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        List<Role> roles = controller.login(context, null, login);

        assertThat(roles).containsExactly(Role.USER);
        verify(context).setResponseCookie(any(Cookie.class));
    }

    // Ej inloggad + fel lösenord = 401, inga roller, ingen session skapas
    @Test
    void incorrectLoginGives401AndNoSession() {
        final LoginData login = new LoginData("testuser", "wrong");

        when(userService.validate("testuser", "wrong")).thenReturn(Optional.empty());

        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        List<Role> roles = controller.login(context, null, login);

        assertThat(roles).isEmpty();

        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
        verify(sessionDao, never()).create(any());
    }

    // Redan inloggad (giltig cookie) + loggar in igen = lösenordet kontrolleras inte, tom lista returneras
    @Test
    void validSessionSkipsValidation() {
        final UUID sessionId = UUID.randomUUID();
        final LoginData login = new LoginData("testuser", "password");

        when(sessionDao.validate(sessionId)).thenReturn(UserId.of(1));

        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        List<Role> roles = controller.login(context, sessionId.toString(), login);

        assertThat(roles).isEmpty();
        verify(userService, never()).validate(any(), any());
    }

    // Sidan laddas om med giltig cookie = användarens roll(er) hämtas och returneras
    @Test
    void validSessionIsLoggedIn() {
        final UUID sessionId = UUID.randomUUID();
        final UserId id = UserId.of(1);

        when(sessionDao.validate(sessionId)).thenReturn(id);
        when(roleDao.get(id)).thenReturn(List.of(Role.USER));

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        assertThat(controller.isLoggedIn(sessionId.toString())).containsExactly(Role.USER);
    }

    // Sidan laddas om utan cookie = tom lista (ej inloggad)
    @Test
    void isLoggedInWithoutCookieReturnsNoRoles() {
        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        assertThat(controller.isLoggedIn(null)).isEmpty();
    }

    // Cookie innehåller skräp som inte är ett UUID = tom lista (ej inloggad)
    @Test
    void invalidUuidIsNotLoggedIn() {
        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        assertThat(controller.isLoggedIn("abc")).isEmpty();
    }

    // Cookie med UUID som inte finns i databasen (t.ex. utgången session) = tom lista (ej inloggad)
    @Test
    void unknownSessionIsNotLoggedIn() {
        final UUID sessionId = UUID.randomUUID();

        when(sessionDao.validate(sessionId)).thenThrow(new CredentialsException("unknown session"));

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        assertThat(controller.isLoggedIn(sessionId.toString())).isEmpty();
    }
}