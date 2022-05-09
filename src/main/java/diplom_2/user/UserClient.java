package diplom_2.user;

import diplom_2.RestClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

public class UserClient extends RestClient {

    @DisplayName("Регистрация пользователя")
    @Step("Регистрация пользователя")
    public ValidatableResponse createUser(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("api/auth/register/")
                .then().log().ifError();
    }

    @DisplayName("Удаление пользователя")
    @Step("Удаление пользователя")
    public void deleteUser(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(user)
                .when()
                .delete("api/auth/user/")
                .then()
                .assertThat()
                .statusCode(202)
                .log().ifError();
    }

    @DisplayName("Логин пользователя")
    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("api/auth/login/")
                .then()
                .log().ifError();
    }

    @DisplayName("Обновление данных о пользователе с токеном")
    @Step("Обновление данных о пользователе с токеном")
    public ValidatableResponse editUserWithToken(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(user)
                .when()
                .patch("api/auth/user/")
                .then()
                .log().ifError();
    }

    @DisplayName("Обновление данных о пользователе без токена")
    @Step("Обновление данных о пользователе без токена")
    public ValidatableResponse editUserWithoutToken(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch("api/auth/user/")
                .then()
                .log().ifError();
    }
}
