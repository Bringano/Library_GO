package se.yrgo.libraryapp.dao;

import java.sql.*;
import java.time.*;
import java.util.*;
import javax.inject.*;
import javax.sql.*;
import org.slf4j.*;
import se.yrgo.libraryapp.entities.*;

public class BookDao {
    private static Logger logger = LoggerFactory.getLogger(BookDao.class);
    private DataSource ds;

    @Inject
    BookDao(DataSource ds) {
        this.ds = ds;
    }

    public Optional<Book> get(BookId id) {
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = createGetQuery(conn, id);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                boolean available = rs.getBoolean("available");

                BookEdition edition = new BookEdition(title, author, isbn);

                return Optional.of(new Book(id, available, edition));
            }
        } catch (SQLException ex) {
            logger.error("Uable to get book " + id, ex);
        }

        return Optional.empty();
    }


    public Set<BookEdition> find(String isbn, String title, String author) {
        if ((isbn == null || isbn.trim().length() == 0)
                && (title == null || title.trim().length() == 0)
                && (author == null || author.trim().length() == 0)) {
            return Set.of();
        }

        try (Connection conn = ds.getConnection();
                PreparedStatement ps = createFindQuery(conn, isbn, title, author);
                ResultSet rs = ps.executeQuery()) {
            return getFullBookEdition(rs);
        } catch (SQLException ex) {
            logger.error("Unable to find books", ex);
            return Set.of();
        }
    }

    public Set<BookEdition> withClass(DdsClassification classification) {

        try (Connection conn = ds.getConnection();
                PreparedStatement ps = createClassificationQuery(conn, classification);
                ResultSet rs = ps.executeQuery()) {
            return getBookEdition(rs);
        } catch (SQLException ex) {
            logger.error("Unable to get books from class", ex);
            return Set.of();
        }
    }

    public List<BookLoan> loans(UserId user) {
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = createLoanQuery(conn, user);
                ResultSet rs = ps.executeQuery()) {
            return getLoansFromSet(rs, user);
        } catch (SQLException ex) {
            logger.error("Uable to get book loans", ex);
            return List.of();
        }
    }

    public List<BookLoan> overdueLoans() {
        String query = "SELECT bl.book_id, be.isbn, title, author, return_date, u.id AS user_id "
                + "FROM book_loan AS bl JOIN book AS b ON bl.book_id = b.book_id "
                + "JOIN book_edition AS be ON b.isbn = be.isbn "
                + "JOIN user AS u ON bl.borrower_id = u.id WHERE return_date < CURDATE() "
                + "ORDER BY return_date ASC";

        try (Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            return getOverdueFromSet(rs);
        } catch (SQLException ex) {
            logger.error("Uable to get books", ex);
            return List.of();
        }
    }

    public boolean lend(BookId book, UserId user) {
        String query = "INSERT INTO book_loan VALUES (?, ?, DATE_ADD(CURDATE(), INTERVAL 1 MONTH))";

        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, book.getId());
            ps.setInt(2, user.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.error("Unable to lend out book " + book + " to user " + user, ex);
            return false;
        }
    }

    public boolean returnBook(BookId book) {
        String query = "DELETE FROM book_loan WHERE book_id = ?";

        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, book.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.error("Unable to return book " + book, ex);
            return false;
        }
    }

    @SuppressWarnings("java:S2095")
    private PreparedStatement createGetQuery(Connection conn, BookId id) throws SQLException {
        String query = "SELECT be.isbn, title, author, "
                + "b.book_id NOT IN (SELECT book_id FROM book_loan) AS available FROM book AS b "
                + "JOIN book_edition AS be ON b.isbn = be.isbn WHERE b.book_id = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id.getId());

        return ps;
    }

    private List<BookLoan> getLoansFromSet(ResultSet rs, UserId user) throws SQLException {
        List<BookLoan> loans = new ArrayList<>();
        while (rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");

            BookEdition edition = new BookEdition(title, author, isbn);

            int id = rs.getInt("book_id");
            BookId bookId = BookId.of(id);

            // a book thas is lent out to someone is never available
            Book book = new Book(bookId, false, edition);

            LocalDate returnDate = rs.getDate("return_date").toLocalDate();

            loans.add(new BookLoan(book, user, returnDate));
        }
        return loans;
    }

    private List<BookLoan> getOverdueFromSet(ResultSet rs) throws SQLException {
        List<BookLoan> loans = new ArrayList<>();
        while (rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");

            BookEdition edition = new BookEdition(title, author, isbn);

            int rawBookId = rs.getInt("book_id");
            BookId bookId = BookId.of(rawBookId);

            // a book thas is lent out to someone is never available
            Book book = new Book(bookId, false, edition);

            LocalDate returnDate = rs.getDate("return_date").toLocalDate();

            int rawUserId = rs.getInt("user_id");
            UserId user = UserId.of(rawUserId);

            loans.add(new BookLoan(book, user, returnDate));
        }
        return loans;
    }

    @SuppressWarnings("java:S2095")
    private PreparedStatement createLoanQuery(Connection conn, UserId user) throws SQLException {
        String query = "SELECT bl.book_id, be.isbn, title, author, return_date "
                + "FROM book_loan AS bl JOIN book AS b ON bl.book_id = b.book_id "
                + "JOIN book_edition AS be ON b.isbn = be.isbn WHERE borrower_id = ? "
                + "ORDER BY return_date ASC, title";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, user.getId());

        return ps;
    }

    private static Set<BookEdition> getFullBookEdition(ResultSet rs) throws SQLException {
        Set<BookEdition> books = new HashSet<>();
        while (rs.next()) {
            int count = rs.getInt("count");
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            int ddsCode = rs.getInt("dds_code");
            String ddsClass = rs.getString("class");
            DdsClassification dds = new DdsClassification(ddsCode, ddsClass);
            books.add(new BookEdition(count, title, author, isbn, dds));
        }
        return books;
    }

    private static Set<BookEdition> getBookEdition(ResultSet rs) throws SQLException {
        Set<BookEdition> books = new HashSet<>();
        while (rs.next()) {
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            books.add(new BookEdition(title, author, isbn));
        }
        return books;
    }

    @SuppressWarnings("java:S2095")
    private static PreparedStatement createFindQuery(Connection conn, String isbn, String title,
            String author) throws SQLException {
        String baseQuery =
                "SELECT COUNT(b.book_id) AS count, be.isbn, title, author, dds_code, class "
                        + "FROM book_edition AS be " + "JOIN book AS b ON be.isbn = b.isbn "
                        + "JOIN dewey_decimal_system AS dds ON be.dds_code = dds.code WHERE 1=1 ";

        String[] input = {isbn, title, author};
        String[] inputQuery = {" be.isbn LIKE ? ", " be.title LIKE ? ", " be.author LIKE ?"};
        List<String> resultQueries = new ArrayList<>();
        List<String> resultData = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            String str = input[i];
            if (str != null && str.trim().length() > 0) {
                resultQueries.add(inputQuery[i]);
                resultData.add("%" + str.trim() + "%");
            }
        }

        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        for (String resultQuery : resultQueries) {
            queryBuilder.append(" AND ").append(resultQuery);
        }

        queryBuilder.append(" GROUP BY be.isbn");

        PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());
        for (int i = 0; i < resultData.size(); i++) {
            ps.setString(i + 1, resultData.get(i));
        }

        return ps;
    }

    @SuppressWarnings("java:S2095")
    private static PreparedStatement createClassificationQuery(Connection conn,
            DdsClassification classification) throws SQLException {
        String query =
                "SELECT isbn, title, author FROM book_edition WHERE dds_code = ? GROUP BY isbn";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, classification.getCode());

        return ps;
    }
}
