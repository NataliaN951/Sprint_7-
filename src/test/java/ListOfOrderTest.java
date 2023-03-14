import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.order.OrdersMethods;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ListOfOrderTest {

    private OrdersMethods orderMethods;
    private List orders;

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        orderMethods = new OrdersMethods();
    }

    //Список заказов
    //Проверь, что в тело ответа возвращается список заказов.
    @Test
    @DisplayName("Order - Получение списка заказов")
    @Description("Basic test for /api/v1/orders endpoint")
    public void getOrderlist() {
        ValidatableResponse createResponse = orderMethods.getOrderList();
        int statusCode = createResponse.extract().statusCode();
        orders = createResponse.extract().path("orders");
        assertEquals("Status code is not correct", 200, statusCode);
        assertThat("Order is not created", orders, notNullValue());
    }
}
