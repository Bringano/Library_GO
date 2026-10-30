package se.yrgo.libraryapp.controllers;

import java.util.*;

import org.pac4j.core.exception.*;

import com.google.inject.*;

import io.jooby.*;
import io.jooby.annotations.*;
import se.yrgo.libraryapp.dao.*;
import se.yrgo.libraryapp.entities.*;
import se.yrgo.libraryapp.entities.forms.*;
import se.yrgo.libraryapp.services.*;

@Path("/login")
public class LoginController {

    private UserService userService;
    private RoleDao roleDao;
    private SessionDao sessionDao;

    @Inject
    LoginController(UserService userService, RoleDao roleDao, SessionDao sessionDao) {
        this.userService = userService;
        this.roleDao = roleDao;
        this.sessionDao = sessionDao;
    }

    @POST
    public List<Role> login(Context context, @CookieParam("session") String sessionCookie, LoginData login) {
        
        if (isInvalidSession(sessionCookie)) {
            Optional<UserId> maybeUserId = userService.validate(login.getUsername(), login.getPassword());

            if (maybeUserId.isEmpty()) {
                context.setResponseCode(StatusCode.UNAUTHORIZED);
                return List.of();
            }

            UserId userId = maybeUserId.get();
            UUID sessionId = sessionDao.create(userId);
            Cookie cookie = createSessionCookie(sessionId);
            context.setResponseCookie(cookie);

            return roleDao.get(userId);
        }
        
        return List.of();
    }

    @GET
    @Path("/check")
    public List<Role> isLoggedIn(@CookieParam("session") String sessionCookie) {
        try {
            UserId userId = getUserForSession(sessionCookie);
            return roleDao.get(userId);
        }
        catch (IllegalArgumentException | CredentialsException ex) {
            return List.of();
        }
    }

    private boolean isInvalidSession(String sessionCookie) {
        // currently does not care if you try to login with other account

        try {
            getUserForSession(sessionCookie);
            return false;
        }
        catch (IllegalArgumentException | CredentialsException ex) {
            return true;
        }
    }

    private UserId getUserForSession(String sessionCookie) {
        if (sessionCookie == null) {
            throw new IllegalArgumentException("Session cookie is null");
        }

        return sessionDao.validate(UUID.fromString(sessionCookie));
    }

    private Cookie createSessionCookie(UUID session) {
        Cookie cookie = new Cookie("session", session.toString());
        cookie.setSameSite(SameSite.LAX);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }
}
