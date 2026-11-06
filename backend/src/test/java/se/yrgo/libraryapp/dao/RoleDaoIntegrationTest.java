package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class RoleDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        RoleDaoIntegrationTest.ds = ds;
    }

    // Användaren test (id 1) är admin enligt testdatan
    @Test
    void testUserIsAdmin() {
        RoleDao roleDao = new RoleDao(ds);

        assertThat(roleDao.get(UserId.of(1))).containsExactly(Role.ADMIN);
    }

    // Användare som inte finns = tom lista
    @Test
    void unknownUserHasNoRoles() {
        RoleDao roleDao = new RoleDao(ds);

        assertThat(roleDao.get(UserId.of(999))).isEmpty();
    }
}