package diplom_2.auth;

import diplom_2.user.User;
import diplom_2.user.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

public class LoginTest {
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
    public void loginUserTest() {
        success = userClient.createUser(user);
        ValidatableResponse login = userClient.loginUser(user);
        login.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(user.getEmail())).and()
                .body("user.name", equalTo(user.getName())).and()
                .body("accessToken", hasLength(178)).and()
                .body("refreshToken", hasLength(80));
    }

    @Test
    public void loginIncorrectUserTest() {
        ValidatableResponse login = userClient.loginUser(user);
        login.assertThat()
                .statusCode(401).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
