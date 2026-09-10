package test;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
//import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Авторизация курьера")
class LoginCourierTest extends test.ApiTestBase {

    static CourierCredentials credentials;

    private static final client.CourierClient client = new client.CourierClient();
    //private Number createdCourierId = null;

    //private final String testLogin = "test_login_for_auth";
    //private final String testPassword = "VerySecretPass_456";

    @BeforeAll
    static void setUp() {
        // Создаем тестового пользователя перед каждым запуском тестов этого класса
        //var credentials = new Courier(generateUniqueLogin(), "StrongPass123", "Иван");
        //given().body(credentials).post("/api/v1/courier");
        Courier courier = new Courier(generateUniqueLogin(), "StrongPass123", "Иван");
        client.create(courier);
        credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        //client.loginAndGetId(courier);
    }
/*
    @AfterEach
    void tearDown() {
        // Удаляем пользователя после тестов
        var idResponse = given()
                .body(new CourierCredentials(testLogin, testPassword))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");

        if (idResponse != null) {
            given().delete("/api/v1/courier/" + idResponse);
        }
*/
    @AfterEach
    void tearDown() {
        if (client.courierId != null) {
            client.deleteCourier(client.courierId);
            // Обнуляем переменную для следующего теста
            //client.courierId = null;
        }
}

    @Test
    @DisplayName("Позитивный: курьер может авторизоваться")
    @Description("Корректные данные должны вернуть ID курьера и статус 200")
    void canLoginWithValidData() {

        client.loginAndGetId(credentials);

        assertThat(client.courierId).describedAs("Поле 'id' должно присутствовать и быть корректным числом")
                .isNotNull();
    }

    @Test
    @DisplayName("Негативный: неверный пароль")
    @Description("Ошибка аутентификации при правильном логине и неправильном пароле")
    void wrongPasswordReturnsError() {
        given()
                .contentType(ContentType.JSON)
                .body(new CourierCredentials(credentials.getLogin(), "WrongPass"))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log().ifValidationFails()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Негативный: несуществующий пользователь")
    void nonExistentUserReturnsError() {
        given()
                .contentType(ContentType.JSON)
                .body(new CourierCredentials("no_such_user", "any_pass"))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log().ifValidationFails()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Негативный: отсутствие поля в теле запроса")
    @ParameterizedTest(name = "Поле {0} отсутствует")
    @ValueSource(strings = {"login", "password"})
    void fieldMissingInLoginRequest(String missingField) {
        CourierCredentials creds;
        switch (missingField) {
            case "login": // Создаем объект без логина
                creds = new CourierCredentials(null, credentials.getPassword());
                break;
            case "password": // Создаем объект без пароля
                creds = new CourierCredentials(credentials.getLogin(), null);
                break;
             default: // Эта ветка сработает только если параметризация теста сломается
                 throw new IllegalArgumentException("Неизвестное поле для проверки: " + missingField);
        }
        given()
                .contentType(ContentType.JSON)
                .body(creds)
            .when()
                .post("/api/v1/courier/login")
            .then()
                .log()
                .ifValidationFails()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }
}