package se.yrgo.libraryapp;

import org.pac4j.core.authorization.authorizer.RequireAllRolesAuthorizer;
import io.jooby.AccessLogHandler;
import io.jooby.CorsHandler;
import io.jooby.GracefulShutdown;
import io.jooby.Jooby;
import io.jooby.SameSite;
import io.jooby.di.GuiceModule;
import io.jooby.flyway.FlywayModule;
import io.jooby.hikari.HikariModule;
import io.jooby.json.JacksonModule;
import io.jooby.pac4j.Pac4jModule;
import io.jooby.pac4j.Pac4jOptions;
import se.yrgo.libraryapp.auth.DbCookieClient;
import se.yrgo.libraryapp.controllers.*;
import se.yrgo.libraryapp.controllers.BookController;
import se.yrgo.libraryapp.controllers.admin.*;
import se.yrgo.libraryapp.entities.Role;

/**
 * This is a Jooby application, an alternative to Spring, kind of.
 * 
 * Don't be afraid to read more about how Jooby works at https://jooby.io/
 * 
 */
public class App extends Jooby {
  public App() {
    install(new GracefulShutdown());
    install(new GuiceModule()); // dependency injection
    install(new HikariModule()); // database connections
    install(new FlywayModule()); // schema migration
    install(new JacksonModule()); // json (de)serialization

    decorator(new AccessLogHandler());
    decorator(new CorsHandler());

    // here we define our controllers

    mvc(LoginController.class);
    mvc(LogoutController.class);
    mvc(BookController.class);
    mvc(RegisterUserController.class);
    mvc(ClassificationController.class);

    // pac4j is a security framework for authentication & authorization
    // we need this for some of the controllers to make sure the
    // user is logged in and has the correct permissions

    Pac4jOptions pac4jOptions = new Pac4jOptions();
    pac4jOptions.setCookieSameSite(SameSite.LAX);
    pac4jOptions.setLogoutPath("/p4jlogout"); // can't get rid of it

    // Having three modules seems less than ideal, but I can't find another way with jooby

    Pac4jModule module1 = new Pac4jModule(pac4jOptions);
    module1.client("/*", DbCookieClient.class);
    install(module1);

    Pac4jModule module2 = new Pac4jModule(pac4jOptions);
    module2.client("/user/*",
        new RequireAllRolesAuthorizer<>(Role.USER.toString()),
        DbCookieClient.class);
    install(module2);

    Pac4jModule module3 = new Pac4jModule(pac4jOptions);
    module3.client("/admin/*", new RequireAllRolesAuthorizer<>(Role.ADMIN.toString()),
        DbCookieClient.class);
    install(module3);

    mvc(UserController.class);
    mvc(se.yrgo.libraryapp.controllers.admin.BookController.class);
    mvc(LoanController.class);
    mvc(se.yrgo.libraryapp.controllers.user.LoanController.class);
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }
}
