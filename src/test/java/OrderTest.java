import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.order.OrdersMethods;
import org.example.order.model.Order;
import org.example.order.model.OrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OrderTest {
    private OrdersMethods orderMethods;
    private int track;

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        orderMethods = new OrdersMethods();
    }

    @After
    public void cleanUp() {
        orderMethods.cancelOrder(track);
    }

    //Создание заказа
    //Проверь, что когда создаёшь заказ:
    //можно совсем не указывать цвет;
    //тело ответа содержит track.
    //Необходимые тестовые данные создаются перед тестом и удаляются после того, как он выполнится.
    @Test
    @DisplayName("Order - Создание заказа")
    @Description("Basic test for /api/v1/orders endpoint")
    public void createOrderWithoutColor() {
        Order order = OrderGenerator.getOrder();
        ValidatableResponse createResponse = orderMethods.create(order);
        int statusCode = createResponse.extract().statusCode();
        track = createResponse.extract().path("track");
        assertEquals("Status code is not correct", 201, statusCode);
        assertThat("Order is not created", track, greaterThan(0));
    }
}
