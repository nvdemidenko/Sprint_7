package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import models.Order;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Создание заказа")
class CreateOrderTest extends ApiTestBase {

    private Integer trackNumber;
    @BeforeEach void setup() {
       trackNumber = null;
    }

    @Test
    @DisplayName("Заказ создается успешно")
    @Description("Минимальный набор данных достаточен для создания заказа")
    void orderCanBeCreated() {
        Order order = new Order(
                "Pyotr", "Petrov", "Moscow, Lenina 10", "5",
                "+72559966321", 120
        );

        trackNumber =
                given()
                //.contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("track", notNullValue()) // Проверка остается здесь // === КРИТИЧЕСКОЕ ИСПРАВЛЕНИЕ ВМЕСТО
                .extract().path("track");
        int k = 1;
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
                "Sidor", "Sidorov", "SPB, Nevsky 10", "8",
                "+79111122333", 60
        );

        // Парсим строку цветов в массив, пропуская пустые значения
        String[] colorArray = colorsCsv.isEmpty() ? new String[0] : colorsCsv.split(",");

        trackNumber = given()
                //.contentType(ContentType.JSON)
                .body(order)
                .queryParam("color", (Object) colorArray)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(expectedStatus)
                .body("track", notNullValue())
                // Извлекаем значение поля "track" из JSON
                .extract().path("track");

        int k = 0;


        /*
        if (expectedStatus == 201) {
            given()
                    .contentType(ContentType.JSON)
                    .body(order)
                    .queryParam("color", (Object) colorArray)
                    .when()
                    .post("/api/v1/orders")
                    .then()
                    .body("track", notNullValue());
        }

         */
    }

    @AfterEach
    void tearDown() {
        if (trackNumber != null) {
            cancelOrder(trackNumber);
            trackNumber = null;
        }
    }

    /**
     * Метод отмены заказа. Вынесен отдельно для переиспользования и читаемости Allure-отчета.
     */
    @Step("Отмена заказа с номером {track}")
    private void cancelOrder(Integer track) {
            given()
                    .queryParam("track", track)//new CancelRequest(track))
                    .put("/api/v1/orders/cancel")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body(containsString("\"ok\":true"));
    }
/*
    // Вспомогательный класс для тела запроса отмены
    public static class CancelRequest {
        public Integer track;

        public CancelRequest(Integer track) {
            this.track = track;
        }
    }

 */
}