package client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import models.Courier;
import io.restassured.response.Response;
import models.CourierCredentials;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierClient {

    public Number courierId = null;
    @Step("Создание курьера с логином '{courier.login}'")
    public void create(Courier courier) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post("/api/v1/courier");

        // Проверяем статус и тело ответа внутри клиента для надежности
        response.then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Удаление курьера с id: {courierId}")
    public void deleteCourier(Number courierId) {
        given()
                .pathParam("id", courierId)
                .delete("/api/v1/courier/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Step("Авторизация курьера {credentials.login} и получение ID")
    public void loginAndGetId(Courier courier) {

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

    public void loginAndGetId(CourierCredentials credentials) {

        courierId = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }
}