package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;

@Tag("foobar")
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class RegisterUserIntegrationTest extends SystemTestBase {

    @Test
    public void canRegisterNewUser(int serverPort) throws Exception {
        HttpResponse<String> registerResponse = postJson(serverPort, "/register",
                "{\"name\":\"newuser\",\"realName\":\"New User\",\"password\":\"secret\"}");

        assertEquals(200, registerResponse.statusCode());
        assertEquals("true", registerResponse.body());

        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"newuser\",\"password\":\"secret\"}");

        assertEquals(200, loginResponse.statusCode());
    }

    @Test
    public void cannotRegisterWithInvalidUsername(int serverPort) throws Exception {
        HttpResponse<String> registerResponse = postJson(serverPort, "/register",
                "{\"name\":\"abc\",\"realName\":\"My Name\",\"password\":\"secret\"}");

        assertEquals(200, registerResponse.statusCode());
        assertEquals("false", registerResponse.body());

        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"abc\",\"password\":\"secret\"}");

        assertEquals(401, loginResponse.statusCode());
    }

    @Test
    public void cannotRegisterSameNameTwice(int serverPort) throws Exception {
        String json = "{\"name\":\"double\",\"realName\":\"My Name\",\"password\":\"secret\"}";

        HttpResponse<String> firstResponse = postJson(serverPort, "/register", json);
        assertEquals("true", firstResponse.body());

        HttpResponse<String> secondResponse = postJson(serverPort, "/register", json);
        assertEquals(200, secondResponse.statusCode());
        assertEquals("false", secondResponse.body());
    }

    @Test
    public void unusedNameIsAvailable(int serverPort) throws Exception {
        HttpResponse<String> response = get(serverPort, "/register/available?name=available");

        assertEquals(200, response.statusCode());
        assertEquals("true", response.body());
    }

    @Test
    public void existingNameIsNotAvailable(int serverPort) throws Exception {
        HttpResponse<String> response = get(serverPort, "/register/available?name=test");

        assertEquals(200, response.statusCode());
        assertEquals("false", response.body());
    }
}