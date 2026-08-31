package test;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTestBase {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @BeforeAll
    public static void globalSetup() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addFilter(new AllureRestAssured());

        RestAssured.requestSpecification = builder.build();
    }
/*
    @Step("Удаление курьера с id: {courierId}")
    protected void deleteCourier(Number courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .log()
                .ifValidationFails() // Покажет детали только при ошибке
                .statusCode(200) // Проверка кода ответа согласно ТЗ
                .body("ok", equalTo(true)); // Проверка тела ответа
        }

 */
static String generateUniqueLogin() {
        return "login_" + RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
}