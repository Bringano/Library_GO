package se.yrgo.libraryapp.controllers.admin;

import javax.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.jooby.annotations.PathParam;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookId;

@Path("/admin/books")
public class BookController {
    private BookDao bookDao;

    @Inject
    BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GET
    @Path("/{id}")
    public Book search(@PathParam String id) {
        try {
            var book = bookDao.get(BookId.of(id));
            return book.orElse(null);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
}
