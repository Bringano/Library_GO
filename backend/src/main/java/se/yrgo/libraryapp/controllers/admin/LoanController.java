package se.yrgo.libraryapp.controllers.admin;

import java.util.List;
import com.google.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.forms.LoanData;

@Path("/admin/loan")
public class LoanController {
    private BookDao bookDao;

    @Inject
    LoanController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @POST
    @Path("/lend")
    public boolean lendBook(LoanData loanData) {
        return bookDao.lend(loanData.getBook(), loanData.getUser());
    }

    @POST
    @Path("/return")
    public boolean returnBook(BookId book) {
        return bookDao.returnBook(book);
    }

    @GET
    @Path("/overdue")
    public List<BookLoan> overdue() {
        return bookDao.overdueLoans();
    }
}
