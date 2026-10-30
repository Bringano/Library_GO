package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import se.yrgo.libraryapp.entities.*;

public class UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDao.class);
    private DataSource ds;

    @Inject
    UserDao(DataSource ds) {
        this.ds = ds;
    }

    public Optional<User> get(String id) {
        try (Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT user, realname FROM user WHERE id = '" + id + "'")) {
            if (rs.next()) {
                String name = rs.getString("user");
                String realname = rs.getString("realname");
                return Optional.of(new User(UserId.of(id), name, realname));
            }
        } catch (SQLException ex) {
            logger.error("Unable to fetch user " + id, ex);
        }

        return Optional.empty();
    }

    public Optional<LoginInfo> getLoginInfo(String user) {
        try (Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, password_hash FROM user WHERE user = '" + user + "'")) {
            if (rs.next()) {
                int id = rs.getInt("id");
                UserId userId = UserId.of(id);
                String passwordHash = rs.getString("password_hash");
                return Optional.of(new LoginInfo(userId, passwordHash));
            }
        } catch (SQLException ex) {
            logger.error("Unable to get user " + user, ex);
        }

        return Optional.empty();
    }

    public boolean register(String name, String realname, String password) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        String passwordHash = encoder.encode(password);

        // handle names like Ian O'Toole
        realname = realname.replace("'", "\\'");

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            return insertUserAndRole(name, realname, passwordHash, conn);
        } catch (SQLException ex) {
            logger.error("Unable to register user " + name, ex);
            return false;
        }
    }

    public boolean isNameAvailable(String name) {
        if (name == null || name.trim().length() < 3) {
            return false;
        }

        String query = "SELECT id FROM user WHERE user = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException ex) {
            logger.error("Unable to lookup user name " + name, ex);
            return false;
        }
    }

    private boolean insertUserAndRole(String name, String realname, String passwordHash,
            Connection conn) throws SQLException {
        String insertUser = "INSERT INTO user (user, realname, password_hash) VALUES ('" + name
                + "', '" + realname + "', '" + passwordHash + "')";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertUser, Statement.RETURN_GENERATED_KEYS);
            UserId userId = getGeneratedUserId(stmt);

            if (userId.getId() > 0 && addToUserRole(conn, userId)) {
                conn.commit();
                return true;
            }
            else {
                conn.rollback();
                return false;
            }
        } catch (SQLException ex) {
            conn.rollback();
            logger.error("Unable to register user " + name, ex);
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private UserId getGeneratedUserId(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            rs.next();
            return UserId.of(rs.getInt(1));
        }
    }

    private boolean addToUserRole(Connection conn, UserId user) throws SQLException {
        String insertRole = "INSERT INTO user_role (user_id, role_id) VALUES (" + user + ", 2)";

        try (Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(insertRole) == 1;
        }
    }
}
