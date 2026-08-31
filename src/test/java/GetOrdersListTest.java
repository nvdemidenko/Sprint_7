package test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Список заказов")
class GetOrdersListTest extends test.ApiTestBase {

    @Test
    @DisplayName("Получение списка всех заказов")
    @Description("Эндпоинт возвращает JSON-массив даже если он пуст")
    void getOrdersList() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class));
    }
}