package se.yrgo.libraryapp.auth;

import org.pac4j.http.client.direct.CookieClient;
import com.google.inject.Inject;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;

public class DbCookieClient extends CookieClient {
    @Inject
    public DbCookieClient(RoleDao roleDao, SessionDao sessionDao) {
        super("session", new DatabaseAuthenticator(roleDao, sessionDao));
    }
}
