package se.yrgo.libraryapp.controllers.admin;

import javax.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.jooby.annotations.PathParam;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;

@Path("/admin/users")
public class UserController {
    private UserDao userDao;

    @Inject
    UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GET
    @Path("/{id}")
    public User getUser(@PathParam String id) {
        try {
            var user = userDao.get(id);
            return user.orElse(null);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
