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
    ValidatableResponse success;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }

    @After
    public void tearDown() {
        if (success != null) {
            String token = User.getAccessToken(success);
            user.setAccessToken(token);
            userClient.deleteUser(user);
        }
    }

    @Test
    public void createUniqueUserTest() {
        success = userClient.createUser(user);
        success.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(user.getEmail())).and()
                .body("user.name", equalTo(user.getName())).and()
                .body("accessToken", hasLength(178)).and()
                .body("refreshToken", hasLength(80));
    }

    @Test
    public void createNotUniqueUserTest() {
        success = userClient.createUser(user);
        ValidatableResponse fail = userClient.createUser(user);
        fail.assertThat()
                .statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createInvalidUserTest() {
        user.setName(null);
        ValidatableResponse fail = userClient.createUser(user);
        fail.assertThat()
                .statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
