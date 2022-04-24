package diplom_2.auth;

import diplom_2.user.User;
import diplom_2.user.UserClient;
import io.restassured.response.ValidatableResponse;
import org.junit.*;

import static org.hamcrest.Matchers.equalTo;

public class UserTest {
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
    public void editUserNameWithTokenTest() {
        success = userClient.createUser(user);
        user.setName("Some New Name");
        user.setAccessToken(User.getAccessToken(success));
        ValidatableResponse edit = userClient.editUserWithToken(user);
        edit.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(user.getEmail())).and()
                .body("user.name", equalTo("Some New Name"));
    }

    @Test
    public void editUserEmailWithTokenTest() {
        success = userClient.createUser(user);
        user.setEmail("some@new.email");
        user.setAccessToken(User.getAccessToken(success));
        ValidatableResponse edit = userClient.editUserWithToken(user);
        edit.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo("some@new.email")).and()
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    public void editUserInfoWithoutTokenTest() {
        success = userClient.createUser(user);
        user.setName("Some New Name");
        ValidatableResponse edit = userClient.editUserWithoutToken(user);
        edit.assertThat()
                .statusCode(401).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("You should be authorised"));
    }
}
