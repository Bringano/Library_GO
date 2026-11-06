package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.Instant;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.CredentialsException;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class SessionDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        SessionDaoIntegrationTest.ds = ds;
    }

    // Ny session = validate ger tillbaka samma användare
    @Test
    void createdSessionIsValid() {
        SessionDao sessionDao = new SessionDao(ds);

        UUID sessionId = sessionDao.create(UserId.of(1));

        assertThat(sessionDao.validate(sessionId)).isEqualTo(UserId.of(1));
    }

    // Session som aldrig skapats = kastar CredentialsException
    @Test
    void unknownSessionThrows() {
        SessionDao sessionDao = new SessionDao(ds);

        assertThatThrownBy(() -> sessionDao.validate(UUID.randomUUID()))
                .isInstanceOf(CredentialsException.class);
    }

    // Borttagen session = kasta CredentialsException
    @Test
    void deletedSessionThrows() {
        SessionDao sessionDao = new SessionDao(ds);
        UUID sessionId = sessionDao.create(UserId.of(1));

        sessionDao.delete(sessionId);

        assertThatThrownBy(() -> sessionDao.validate(sessionId))
                .isInstanceOf(CredentialsException.class);
    }

    // Session äldre än 7 dagar = kastar CredentialsException "Session has expired"
    @Test
    void expiredSessionThrows() throws SQLException {
        final UUID sessionId = UUID.randomUUID();
        final Timestamp eightDaysAgo = Timestamp.from(Instant.now().minus(Duration.ofDays(8)));

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO session VALUES (?, ?, ?)")) {
            ps.setString(1, sessionId.toString());
            ps.setInt(2, 1);
            ps.setTimestamp(3, eightDaysAgo);
            ps.executeUpdate();
        }

        SessionDao sessionDao = new SessionDao(ds);

        assertThatThrownBy(() -> sessionDao.validate(sessionId))
                .isInstanceOf(CredentialsException.class)
                .hasMessage("Session has expired");
    }
}
