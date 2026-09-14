package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Список заказов")
class GetOrdersListTest extends ApiTestBase {

    @Test
    @DisplayName("Получение списка всех заказов")
    @Description("Эндпоинт возвращает JSON-массив. Проверяем структуру объектов внутри массива.")
    void getOrdersList() {
        Response response = given()
                .when()
                .get("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                // 1. Проверяем, что поле orders вообще есть и оно не null
                .body("orders", notNullValue())
                // 2. Проверяем, что это именно List (массив)
                .body("orders", instanceOf(List.class))
                .extract().response();

        // 3. Извлекаем данные для детальной проверки содержимого
        List<Map<String, Object>> orders = response.path("orders");

        // Дополнительная проверка через AssertJ для анализа элементов списка
        if (!orders.isEmpty()) {
            // Проверяем первый заказ в списке на наличие ключевых полей
            Map<String, Object> firstOrder = orders.get(0);

            assertThat(firstOrder).containsKeys("id", "firstName", "lastName", "address", "deliveryDate");

            // Пример проверки типа конкретного поля первого заказа
            assertThat(firstOrder.get("id")).isInstanceOf(Integer.class);
            // ID должен быть > 0
            assertThat((Integer) firstOrder.get("id")).isPositive();
        }

    }
}