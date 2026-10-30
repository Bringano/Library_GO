package se.yrgo.libraryapp.controllers;

import java.util.UUID;
import javax.inject.Inject;
import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.SameSite;
import io.jooby.annotations.CookieParam;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import se.yrgo.libraryapp.dao.SessionDao;

@Path("/logout")
public class LogoutController {
    private SessionDao sessionDao;

    @Inject
    LogoutController(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @POST
    @GET
    public void logout(Context context, @CookieParam("session") String sessionCookie) {
        if (sessionCookie == null) {
            return;
        }

        try {
            sessionDao.delete(UUID.fromString(sessionCookie));
        } catch (IllegalArgumentException ex) {
            // just ignore this
        }

        Cookie cookie = new Cookie("session", "");
        cookie.setSameSite(SameSite.LAX);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        context.setResponseCookie(cookie);
    }
}
