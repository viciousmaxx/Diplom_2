package diplom_2.orders;

import com.github.javafaker.Faker;
import diplom_2.user.User;
import diplom_2.user.UserClient;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.*;

public class OrdersTest {
    User user;
    UserClient userClient;
    ValidatableResponse success;
    OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        orderClient = new OrderClient();
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
    @Description("Создание заказа: без авторизации")
    public void makeOrderWithoutAuth() {
        Order order = new Order(orderClient.getIngredients());
        ValidatableResponse validatableResponse = orderClient.createOrder(order);
        validatableResponse.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @Description("Создание заказа: с авторизацией")
    public void makeOrderWithAuth() {
        success = userClient.createUser(user);
        String token = User.getAccessToken(success);
        Order order = new Order(orderClient.getIngredients());
        ValidatableResponse validatableResponse = orderClient.createOrderWithAuth(order, token);
        validatableResponse.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @Description("Создание заказа: со случайными ингредиентами")
    public void makeOrderWithIngredients() {
        ArrayList<String> ingredient = new ArrayList<>();
        ArrayList<String> allIngredients = (orderClient.getIngredients());
        for (int i = ThreadLocalRandom.current().nextInt(0, allIngredients.size()); i <= allIngredients.size(); i += 1)
        {
            ingredient.add(allIngredients.get
                    (ThreadLocalRandom.current().nextInt(0, allIngredients.size())));
        }
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(order);
        validatableResponse.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @Description("Создание заказа: без ингредиентов")
    public void makeOrderWithoutIngredients() {
        ArrayList<String> ingredient = new ArrayList<>();
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(order);
        validatableResponse.assertThat()
                .statusCode(400).and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание заказа: с неверным хешем ингредиентов")
    public void makeOrderWithWrongHashIngredients() {
        String randomTextForHash = Faker.instance().chuckNorris().fact();
        String randomHash = (DigestUtils.md5Hex(randomTextForHash));
        ArrayList<String> ingredient = new ArrayList<>();
        ingredient.add(randomHash);
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(order);
        validatableResponse.assertThat()
                .statusCode(500);
    }

    @Test
    @Description("Получить заказы конкретного пользователя")
    public void getOrderWithAuth() {
        success = userClient.createUser(user);
        String token = User.getAccessToken(success);
        Order order = new Order(orderClient.getIngredients());
        orderClient.createOrderWithAuth(order, token);
        ValidatableResponse validatableResponse = orderClient.getOrder(token);
        validatableResponse.assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                .body("orders", isA(List.class))
                .body("orders.name[0]", endsWith("бургер"))
                .body("orders.number[0]", isA(Integer.class));
    }

    @Test
    @Description("Получить заказы конкретного пользователя без аутентификации")
    public void getOrderWithoutAuth() {
        success = userClient.createUser(user);
        String token = User.getAccessToken(success);
        Order order = new Order(orderClient.getIngredients());
        orderClient.createOrderWithAuth(order, token);
        ValidatableResponse validatableResponse = orderClient.getOrderWithoutAuth();
        validatableResponse.assertThat()
                .statusCode(401).and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
