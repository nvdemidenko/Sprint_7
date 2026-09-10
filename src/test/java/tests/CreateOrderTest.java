package test;

import io.qameta.allure.*;
import models.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Создание заказа")
class CreateOrderTest extends test.ApiTestBase {

    @Test
    @DisplayName("Заказ создается успешно")
    @Description("Минимальный набор данных достаточен для создания заказа")
    void orderCanBeCreated() {
        Order order = new Order(
                "Pyotr", "Petrov", "Moscow, Tverskaya 1", "Tverskaya",
                "+79991112233", 120
        );

        given()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @DisplayName("Цвета заказа: позитивные и негативные сценарии")
    @ParameterizedTest(name = "[{index}] Цвета: {0}, Ожидаемый статус: {1}")
    @CsvSource({
            "BLACK, 201",
            "GREY, 201",
            "'BLACK,GREY', 201",
            "'', 201" // Пустое значение CSV создаст пустой список/null в зависимости от десериализатора
    })
    void checkOrderColors(String colorsCsv, int expectedStatus) {
        Order order = new Order(
                "Sidor", "Sidorov", "SPB, Nevsky 10", "Nevsky Prospekt",
                "+79110001122", 60
        );

        // Парсим строку цветов в массив, пропуская пустые значения
        String[] colorArray = colorsCsv.isEmpty() ? new String[0] : colorsCsv.split(",");

        given()
                .body(order)
                .queryParam("color", colorArray)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(expectedStatus);

        if (expectedStatus == 201) {
            given().body(order).queryParam("color", colorArray)
                    .when().post("/api/v1/orders")
                    .then().body("track", notNullValue());
        }
    }
}