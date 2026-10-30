package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.pac4j.core.exception.CredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.UserId;

public class SessionDao {
    private static Logger logger = LoggerFactory.getLogger(SessionDao.class);
    private static final Duration SESSION_EXPIRATION = Duration.ofDays(7);

    private DataSource ds;

    @Inject
    SessionDao(DataSource ds) {
        this.ds = ds;
    }

    public UUID create(UserId userId) {
        try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
            UUID uuid = UUID.randomUUID();
            stmt.executeUpdate("INSERT INTO session VALUES ('" + uuid.toString() + "', " + userId
                    + ", CURRENT_TIMESTAMP)");
            return uuid;
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to create session", ex);
        }
    }

    public void delete(UUID session) {
        try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM session WHERE id = '" + session.toString() + "'");
        } catch (SQLException ex) {
            logger.error("Unable to delete session", ex);
        }
    }

    public UserId validate(UUID session) {
        try (Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT user_id, created FROM session WHERE id = '"
                        + session.toString() + "'")) {

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                Timestamp timestamp = rs.getTimestamp("created");
                validateExpiration(timestamp);
                return UserId.of(userId);
            }
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to validate session", ex);
        }

        throw new CredentialsException("Unable to validate session");
    }

    private void validateExpiration(Timestamp timestamp) {
        Instant sessionCreated = timestamp.toInstant();
        Instant now = Instant.now();
        if (sessionCreated.plus(SESSION_EXPIRATION).isBefore(now)) {
            throw new CredentialsException("Session has expired");
        }
    }
}
