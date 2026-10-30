package se.yrgo.libraryapp.controllers.user;

import java.util.List;
import javax.inject.Inject;
import org.pac4j.core.profile.CommonProfile;
import io.jooby.annotations.ContextParam;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.UserId;

@Path("/user/loans")
public class LoanController {
    private BookDao bookDao;

    @Inject
    LoanController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GET
    public List<BookLoan> loans(@ContextParam CommonProfile user) {
        if (user == null) {
            return List.of();
        }

        return bookDao.loans(UserId.of(user.getId()));
    }
}
