package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.LoginInfo;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {

        // this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserById() {

        // this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);

        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getLoginInfoForExistingUser() {
        final String hash = "$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ";

        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeInfo = userDao.getLoginInfo("test");

        assertThat(maybeInfo).isPresent();
        assertThat(maybeInfo.get().getUserId()).isEqualTo(UserId.of(1));
        assertThat(maybeInfo.get().getPasswordHash()).isEqualTo(hash);
    }

    @Test
    void getLoginInfoForUnknownUserIsEmpty() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.getLoginInfo("nobody")).isEmpty();
    }

    @Test
    void getUnknownUserIsEmpty() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.get("999")).isEmpty();
    }

    @Test
    void takenNameIsNotAvailable() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable("test")).isFalse();
    }

    @Test
    void freeNameIsAvailable() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable("nobody")).isTrue();
    }

    @Test
    void registeredUserCanBeFound() {
        UserDao userDao = new UserDao(ds);

        boolean registered = userDao.register("testuser", "Test User", "hashpass");
        Optional<LoginInfo> maybeInfo = userDao.getLoginInfo("testuser");

        assertThat(registered).isTrue();
        assertThat(maybeInfo).isPresent();
        assertThat(maybeInfo.get().getPasswordHash()).isEqualTo("hashpass");
    }

    @Test
    void nameWithApostropheIsStoredCorrectly() {
        final String realname = "Ian O'Toole";
        UserDao userDao = new UserDao(ds);

        boolean registered = userDao.register("iantoole", realname, "passhash");
        assertThat(registered).isTrue();

        UserId id = userDao.getLoginInfo("iantoole").get().getUserId();
        Optional<User> maybeUser = userDao.get(id.toString());

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getRealname()).isEqualTo(realname);
    }
}