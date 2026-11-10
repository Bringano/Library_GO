package se.yrgo.libraryapp.integrations;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.jooby.JoobyTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import se.yrgo.libraryapp.App;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;

@Tag("integration")
@Tag("foobar")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {

    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");

    public static App createApp() {
        System.setProperty("db.url", mySQLContainer.getJdbcUrl());
        System.setProperty("db.user", mySQLContainer.getUsername());
        System.setProperty("db.password", mySQLContainer.getPassword());
        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }

    private HttpClient client;

    @BeforeEach
    void createClient() {
        client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();
    }

    @Test
    public void canLoginAndLogout(int serverPort) throws Exception {
        HttpResponse<String> loginResponse = postJson(serverPort, "/login",
                "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}");
        assertEquals(200, loginResponse.statusCode());

        HttpResponse<String> logoutResponse = get(serverPort, "/logout");

        assertEquals(200, logoutResponse.statusCode());
    }

    private HttpResponse<String> get(int port, String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> postJson(int port, String path, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void alwaysOkToLogout(int serverPort) throws IOException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(
                    URI.create("http://localhost:" + serverPort + "/logout")).build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            assertEquals("", resp.body());
            assertEquals(HttpURLConnection.HTTP_OK, resp.statusCode());
        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }


}