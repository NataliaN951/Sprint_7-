package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.base.ScooterRestClient;
import org.example.courier.model.Courier;
import org.example.courier.model.CourierCredential;

import static io.restassured.RestAssured.given;


public class CourierMethods extends ScooterRestClient {

    private static final String COURIER_URI = BASE_URI + "courier/";

    @Step("Создание курьера: {courier}")
    public ValidatableResponse create(Courier courier) {
        return
                given()
                        .spec(getbaseReqSpec())
                        .body(courier) // передача файла
                        .when()
                        .post(COURIER_URI)
                        .then();
    }

    @Step("Логин курьера: {courierCredential}")
    public ValidatableResponse login(CourierCredential courierCredential) {
        return
                given()
                        .spec(getbaseReqSpec())
                        .body(courierCredential) // передача файла
                        .when()
                        .post(COURIER_URI + "login/")
                        .then();
    }

    @Step("Удаление курьера: {id}")
    public ValidatableResponse delete(int id) {
        String idString = String.format("{\"id\": \"%d\"}", id);
        System.out.println(idString);
        return
                given()
                        .spec(getbaseReqSpec())
                        .body(idString) // передача файла
                        .when()
                        .delete(COURIER_URI + id)
                        .then();
    }
}
