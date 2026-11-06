package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.DdsClassification;

@Tag("integration")
@H2
public class ClassificationDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        ClassificationDaoIntegrationTest.ds = ds;
    }

    // Bara kategorier som har minst en bok i testdatan, sorterade på kod
    @Test
    void onlyClassificationsSortedOnCode() {
        ClassificationDao classificationDao = new ClassificationDao(ds);

        assertThat(classificationDao.getAvailable())
                .extracting(DdsClassification::getCode)
                .containsExactly(0, 5, 306, 510, 641, 800, 898);
    }

    // Namnet följer med koden
    @Test
    void classificationHasName() {
        ClassificationDao classificationDao = new ClassificationDao(ds);

        assertThat(classificationDao.getAvailable())
                .contains(new DdsClassification(800, "Literature"));
    }
}