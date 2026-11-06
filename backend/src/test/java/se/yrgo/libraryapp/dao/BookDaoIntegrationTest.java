package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import java.time.LocalDate;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.DdsClassification;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class BookDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        BookDaoIntegrationTest.ds = ds;
    }

    // Bok 1 finns och är inte utlånad = tillgänglig
    @Test
    void bookNotLentIsAvailable() {
        BookDao bookDao = new BookDao(ds);
        Optional<Book> maybeBook = bookDao.get(BookId.of(1));

        assertThat(maybeBook).isPresent();
        assertThat(maybeBook.get().getEdition().getTitle()).isEqualTo("T-SQL Fundamentals");
        assertThat(maybeBook.get().getAvailable()).isTrue();
    }

    // Bok 2 är utlånad enligt testdatan = inte tillgänglig
    @Test
    void lentBookIsNotAvailable() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.get(BookId.of(2)).get().getAvailable()).isFalse();
    }

    // Bok som inte finns = empty
    @Test
    void unknownBookIsEmpty() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.get(BookId.of(999))).isEmpty();
    }

    // Sök på del av titel = hittar boken
    @Test
    void findByPartOfTitle() {
        BookDao bookDao = new BookDao(ds);
        Set<BookEdition> result = bookDao.find(null, "Pippi", null);

        assertThat(result).hasSize(1);
        BookEdition pippi = result.iterator().next();
        assertThat(pippi.getAuthor()).isEqualTo("Astrid Lindgren");
    }

    // Sökning som inte matchar något = tom mängd
    @Test
    void findWithNoMatchIsEmpty() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.find(null, "bok finns ej", null)).isEmpty();
    }

    // Kategori 898 (Scandinavian) = bara Pippi
    @Test
    void withClassGivesBooksInClass() {
        BookDao bookDao = new BookDao(ds);
        Set<BookEdition> result = bookDao.withClass(new DdsClassification(898, null));

        assertThat(result).extracting(BookEdition::getTitle)
                .containsExactly("Här kommer Pippi Långstrump");
    }

    // User 2 har två lån, sorterade på återlämningsdatum (tidigast först)
    @Test
    void loansForUserSortedByReturnDate() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.loans(UserId.of(2)))
                .extracting(BookLoan::getDueDate)
                .containsExactly(LocalDate.of(2021, 5, 2), LocalDate.of(2021, 5, 12));
    }

    // User 1 har inga lån = tom lista
    @Test
    void userWithoutLoansHasNone() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.loans(UserId.of(1))).isEmpty();
    }

    // Båda lånen i testdatan har passerat sitt datum = båda är försenade
    @Test
    void overdueLoansFindsBoth() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.overdueLoans())
                .extracting(loan -> loan.getBook().getId())
                .containsExactly(BookId.of(4), BookId.of(2));
    }

    // Lämna tillbaka en utlånad bok = true, och boken blir tillgänglig igen
    @Test
    void returnLentBookMakesItAvailable() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.returnBook(BookId.of(2))).isTrue();
        assertThat(bookDao.get(BookId.of(2)).get().getAvailable()).isTrue();
    }

    // Lämna tillbaka en bok som inte är utlånad = false
    @Test
    void returnBookNotLentGivesFalse() {
        BookDao bookDao = new BookDao(ds);

        assertThat(bookDao.returnBook(BookId.of(1))).isFalse();
    }
}