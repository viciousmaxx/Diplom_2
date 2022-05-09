package diplom_2.auth;

import diplom_2.user.User;
import diplom_2.user.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

public class RegisterTest {
    User user;
    UserClient userClient;
    ValidatableResponse response;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }

    @After
    public void tearDown() {
        if (response.extract().body().path("success").equals(true)) {
            String token = User.getAccessToken(response);
            user.setAccessToken(token);
            userClient.deleteUser(user);
        }
    }

    @Test
    public void createUniqueUserTest() {
        response = userClient.createUser(user);
        response.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(user.getEmail())).and()
                .body("user.name", equalTo(user.getName())).and()
                .body("refreshToken", hasLength(80));
    }

    @Test
    public void createNotUniqueUserTest() {
        response = userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat()
                .statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createInvalidUserTest() {
        user.setName(null);
        response = userClient.createUser(user);
        response.assertThat()
                .statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
