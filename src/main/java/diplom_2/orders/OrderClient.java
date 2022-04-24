package diplom_2.orders;

import diplom_2.RestClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;

public class OrderClient extends RestClient {
    @DisplayName("Запрос списка ингредиентов")
    @Step("Запрос списка ингредиентов")
    public ArrayList<String> getIngredients() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get("api/ingredients/")
                .then().log().ifError()
                .extract().path("data._id");
    }

    @DisplayName("Создание заказа без авторизации")
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(Order order) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("api/orders/")
                .then()
                .log().ifError();
    }

    @DisplayName("Создание заказа с авторизацией")
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post("api/orders/")
                .then()
                .log().ifError();
    }

    @DisplayName("Получение заказа с авторизацией")
    @Step("Получение заказа с авторизацией")
    public ValidatableResponse getOrder(String token) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get("api/orders/")
                .then()
                .log().ifError();
    }

    @DisplayName("Получение заказа без авторизации")
    @Step("Получение заказа без авторизации")
    public ValidatableResponse getOrderWithoutAuth() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get("api/orders/")
                .then()
                .log().ifError();
    }
}