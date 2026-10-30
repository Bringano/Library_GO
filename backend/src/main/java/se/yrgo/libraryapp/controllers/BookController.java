package se.yrgo.libraryapp.controllers;

import java.util.Set;
import javax.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.jooby.annotations.QueryParam;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;

@Path("/books")
public class BookController {
    private BookDao bookDao;

    @Inject
    BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GET
    public Set<BookEdition> search(@QueryParam String isbn, @QueryParam String title, @QueryParam String author, @QueryParam String classification) {
        if (classification != null) {
            return bookDao.withClass(new DdsClassification(Integer.parseInt(classification), null));
        }

        return bookDao.find(isbn, title, author);
    }
}
