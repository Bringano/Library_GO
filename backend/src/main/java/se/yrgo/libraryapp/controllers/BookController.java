package se.yrgo.libraryapp.controllers;

import java.util.Set;
import javax.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.jooby.annotations.QueryParam;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;
import se.yrgo.libraryapp.services.BookService;

@Path("/books")
public class BookController {

    private BookService service;

    @Inject
    BookController(BookService service) {
        this.service = service;
    }

    @GET
    public Set<BookEdition> search(@QueryParam String isbn, @QueryParam String title, @QueryParam String author, @QueryParam String classification) {
        if (classification != null) {
            return service.withClass(new DdsClassification(Integer.parseInt(classification), null));
        }

        return service.find(isbn, title, author);
    }
}
