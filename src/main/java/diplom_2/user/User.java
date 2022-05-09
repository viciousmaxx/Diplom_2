package diplom_2.user;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Data;

@Data
public class User {
    public String email;
    public String password;
    public String name;
    public String accessToken;
    public String refreshToken;

    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Step("Создаем пользователя со случайными данными")
    public static User getRandomUser() {
        final String email = Faker.instance().internet().emailAddress();
        final String password = Faker.instance().internet().password();
        final String name = Faker.instance().name().name();

        Allure.attachment("Email", email);
        Allure.attachment("Password", password);
        Allure.attachment("First name", name);

        return new User(email, password, name);
    }

    public static String getAccessToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken").toString().substring(7);

    }

}
