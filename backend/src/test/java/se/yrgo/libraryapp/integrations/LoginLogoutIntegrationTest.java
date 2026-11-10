package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;

@Tag("foobar")
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest extends SystemTestBase {

    @Test
    public void alwaysOkToLogout(int serverPort) throws Exception {
        HttpResponse<String> logoutResponse = get(serverPort, "/logout");

        assertEquals("", logoutResponse.body());
        assertEquals(200, logoutResponse.statusCode());
    }

    @Test
    public void canLoginAndLogout(int serverPort) throws Exception {
        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}");
        assertEquals(200, loginResponse.statusCode());

        HttpResponse<String> logoutResponse = get(serverPort, "/logout");
        assertEquals(200, logoutResponse.statusCode());
    }

    @Test
    public void cannotLoginWithWrongPassword(int serverPort) throws Exception {
        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"test\",\"password\":\"wrongp\"}");

        assertEquals(401, loginResponse.statusCode());
    }

    @Test
    public void cannotLoginWithUnknownUser(int serverPort) throws Exception {
        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"wrong\",\"password\":\"yrgoP4ssword\"}");

        assertEquals(401, loginResponse.statusCode());
    }
}