package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;


public class ApiTestBase {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @BeforeAll
    public static void globalSetup() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addFilter(new AllureRestAssured());

        RestAssured.requestSpecification = builder.build();
    }

protected static String generateUniqueLogin() {
        return "login_" + RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
}