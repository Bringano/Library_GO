package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class BookServiceTest {

    @Mock
    private BookDao bookDao;

    // Alla sökfält tomma = tom mängd, databasen tillfrågas inte
    @Test
    void allFieldsBlankGivesEmptyAndSkipsDao() {
        BookService bookService = new BookService(bookDao);

        assertThat(bookService.find(null, "", "   ")).isEmpty();
        verify(bookDao, never()).find(any(), any(), any());
    }

    // Minst ett sökfält ifyllt = sökningen skickas till databasen
    @Test
    void oneFieldFilledSearchesInDao() {
        final BookEdition hobbit = new BookEdition("The Hobbit", "Tolkien", "123");
        when(bookDao.find(null, "Hobbit", null)).thenReturn(Set.of(hobbit));

        BookService bookService = new BookService(bookDao);

        assertThat(bookService.find(null, "Hobbit", null)).containsExactly(hobbit);
    }

    // Sökning på kategori = skickas vidare till databasen
    @Test
    void withClassSearchesInDao() {
        final DdsClassification fiction = new DdsClassification(800, "Literature");
        final BookEdition hobbit = new BookEdition("The Hobbit", "Tolkien", "123");
        when(bookDao.withClass(fiction)).thenReturn(Set.of(hobbit));

        BookService bookService = new BookService(bookDao);

        assertThat(bookService.withClass(fiction)).containsExactly(hobbit);
    }
}