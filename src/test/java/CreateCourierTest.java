package test;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import models.Courier;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
//import static jdk.internal.vm.vector.VectorSupport.extract;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Создание курьера")
class CreateCourierTest extends test.ApiTestBase {

    private final client.CourierClient client = new client.CourierClient();
    //private Number createdCourierId = null;

    @Test
    @DisplayName("Позитивный: можно создать курьера")
    @Description("Успешное создание нового курьера с валидными данными")
    /*void createValidCourier() {
        Courier courier = new Courier(generateUniqueLogin(), "StrongPass123", "Иван");

        createdCourierId = given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().path("id");

        //assertThat(createdCourierId).isNotNull();
    }*/
    void createValidCourier() {
        Courier courier = new Courier(generateUniqueLogin(), "StrongPass123", "Иван");
        client.create(courier);
        client.loginAndGetId(courier);
    }

    @Test
    @DisplayName("Негативный: нельзя создать двух одинаковых курьеров")
    @Description("Попытка регистрации с уже существующим логином должна вернуть ошибку 409")
    void cannotCreateDuplicateCourier() {
        String login = generateUniqueLogin();
        Courier first = new Courier(login, "pass1", "Иван");
        Courier second = new Courier(login, "pass2", "Петр");

    /*
        given()
                .contentType(ContentType.JSON)
                .body(first)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    */
        client.create(first);

        given()
                .contentType(ContentType.JSON)
                .body(second)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifValidationFails()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Description("Сервер должен возвращать 400, если не передано одно из обязательных полей")
    @ParameterizedTest(name = "Поле [{0}] отсутствует в теле запроса")
    @ValueSource(strings = {"login", "password", "firstName"})
    @DisplayName("Негативный: ошибка при отсутствии обязательного поля")
    void errorWhenFieldIsMissing(String missingField) {
        Courier incomplete;
        switch (missingField) {
            case "login": // Не передаем логин, чтобы проверить ошибку валидации этого поля
                 incomplete = new Courier(null, "qwerty123", "Ivan");
                 break;
            case "password": // Не передаем пароль
                incomplete = new Courier("valid_login", null, "Ivan");
                break;
            case "firstName": // Не передаем имя
                incomplete = new Courier("valid_login", "qwerty123", null);
                break;

            default: // Защитная ветка на случай, если в тест попадет некорректное значение
                throw new IllegalArgumentException("Неизвестный сценарий для missingField: " + missingField);
        }

        given()
                .contentType(ContentType.JSON)
                .body(incomplete)
                .when()
                .post("/api/v1/courier")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }
/*
    @AfterEach
    void cleanupCreatedCourier() {
        // Удаляем только если создание прошло успешно и ID был записан
        if (createdCourierId != null) {
            given()
                .pathParam("id", createdCourierId)
                .delete("/api/v1/courier/{id}")
                .then()
                    .assertThat()
                    .statusCode(202)
                    .body("ok", equalTo(true));
            // Обнуляем переменную для следующего теста
            createdCourierId = null;
        }
    }
 */
    @AfterEach
    void tearDown() {
        if (client.courierId != null) {
            client.deleteCourier(client.courierId);
            // Обнуляем переменную для следующего теста
            client.courierId = null;
        }
    }
}