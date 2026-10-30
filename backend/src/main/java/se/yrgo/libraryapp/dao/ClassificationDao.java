package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.DdsClassification;

public class ClassificationDao {
    private static Logger logger = LoggerFactory.getLogger(ClassificationDao.class);
    private DataSource ds;

    @Inject
    ClassificationDao(DataSource ds) {
        this.ds = ds;
    }

    public List<DdsClassification> getAvailable() {
        String query = "SELECT DISTINCT dds.code, dds.class FROM dewey_decimal_system AS dds "
                + "WHERE dds.code IN (SELECT dds_code FROM book_edition) ORDER BY dds.code";

        try (Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            List<DdsClassification> classifications = new ArrayList<>();
            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("class");
                classifications.add(new DdsClassification(code, name));
            }
            return classifications;
        } catch (SQLException ex) {
            logger.error("Unable to execute query", ex);
            return List.of();
        }
    }
}
