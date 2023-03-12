package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.base.ScooterRestClient;
import org.example.order.model.Order;

import static io.restassured.RestAssured.given;

public class OrdersMethods extends ScooterRestClient {
    private static final String ORDER_URI = BASE_URI + "orders/";

    @Step("Создание заказа: {order}")
    public ValidatableResponse create(Order order) {
        return
                given()
                        .spec(getbaseReqSpec())
                        .body(order) // передача файла
                        .when()
                        .post(ORDER_URI)
                        .then();

    }

    @Step("Отмена заказа: {track}")
    public ValidatableResponse cancelOrder(int track) {
        return
                given()
                        .spec(getbaseReqSpec())
                        .when()
                        .put(ORDER_URI + "cancel?track=" + track)
                        .then();
    }
}
