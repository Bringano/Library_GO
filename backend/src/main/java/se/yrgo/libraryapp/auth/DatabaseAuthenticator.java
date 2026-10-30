package se.yrgo.libraryapp.auth;

import java.util.UUID;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.profile.CommonProfile;
import com.google.inject.Inject;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

public class DatabaseAuthenticator implements Authenticator<TokenCredentials> {
    private RoleDao roleDao;
    private SessionDao sessionDao;
  
    @Inject
    public DatabaseAuthenticator(RoleDao roleDao, SessionDao sessionDao) {
        this.roleDao = roleDao;
        this.sessionDao = sessionDao;
    }

    @Override
    public void validate(TokenCredentials credentials, WebContext context) {
        try {
            String token = credentials.getToken();
            UserId userId = sessionDao.validate(UUID.fromString(token));
            
            CommonProfile profile = new CommonProfile();
            profile.setId(userId.toString());

            for (Role role : roleDao.get(userId)) {
                profile.addRole(role.toString());
            }

            credentials.setUserProfile(profile);
        }
        catch (IllegalArgumentException ex) {
            // just ignore this
        }
    }
}
