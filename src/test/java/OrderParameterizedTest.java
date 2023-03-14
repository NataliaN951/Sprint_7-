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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class OrderParameterizedTest {
    private OrdersMethods orderMethods;
    private int track;
    @Parameterized.Parameter()
    public String[] color;

    @Parameterized.Parameters()
    public static Object[] params() {
        return new Object[][]{
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{}},
        };
    }

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
    //можно указать один из цветов — BLACK или GREY;
    //можно указать оба цвета;
    //Чтобы протестировать создание заказа, нужно использовать параметризацию.
    //Необходимые тестовые данные создаются перед тестом и удаляются после того, как он выполнится.

    @Test
    @DisplayName("Order - Создание заказа c указанием, без указания цвета")
    @Description("Basic test for /api/v1/orders endpoint")
    public void createOrderWithColor() {
        Order order = OrderGenerator.getOrder();
        order.setColor(color);
        ValidatableResponse createResponse = orderMethods.create(order);
        int statusCode = createResponse.extract().statusCode();
        track = createResponse.extract().path("track");
        assertEquals("Status code is not correct", 201, statusCode);
        assertThat("Order is not created", track, greaterThan(0));
    }
}
