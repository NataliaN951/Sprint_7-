import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.courier.CourierMethods;
import org.example.courier.model.Courier;
import org.example.courier.model.CourierCredential;
import org.example.courier.model.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginCourierTest {
    private CourierMethods courierMethods;
    private int courierId;

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierMethods = new CourierMethods();
    }

    @After
    public void cleanUp() {
        courierMethods.delete(courierId);
    }

    //Логин курьера
    //Проверь:
    //курьер может авторизоваться;
    //для авторизации нужно передать все обязательные поля;
    //успешный запрос возвращает id.
    @Test
    @DisplayName("Courier - Авторизация курьера с валидными данными")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationCourierWithValidData() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Status code is not correct", 201, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierMethods.login(CourierCredential.from(courier));
        courierId = loginResponse.extract().path("id");
        assertTrue("Courier ID not created", courierId != 0);
    }

    //    система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Courier - Авторизация курьера с некорректным логином")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationCourierWithIncorrectLogin() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Status code is not correct", 201, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        CourierCredential courierCredential = new CourierCredential(courier.getLogin(), "U7");
        ValidatableResponse loginResponse = courierMethods.login(courierCredential);
        int secondStatusCode = loginResponse.extract().statusCode();
        String isCourierAuthorized = loginResponse.extract().path("message");
        String expectedMessage = "Учетная запись не найдена";

        assertEquals("Status code is not correct", 404, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isCourierAuthorized);
    }

    @Test
    @DisplayName("Courier - Авторизация курьера с некорректным паролем")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationCourierWithIncorrectPassword() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Status code is not correct", 201, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        CourierCredential courierCredential = new CourierCredential("U7", courier.getPassword());
        ValidatableResponse loginResponse = courierMethods.login(courierCredential);
        int secondStatusCode = loginResponse.extract().statusCode();
        String isCourierAuthorized = loginResponse.extract().path("message");
        String expectedMessage = "Учетная запись не найдена";

        assertEquals("Status code is not correct", 404, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isCourierAuthorized);
    }

    //    если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier - Авторизация курьера, если какого-то поля нет")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationCourierWithoutOneField() {
        CourierCredential courierCredential = new CourierCredential("", "U7");
        ValidatableResponse loginResponse = courierMethods.login(courierCredential);
        int secondStatusCode = loginResponse.extract().statusCode();
        String isCourierAuthorized = loginResponse.extract().path("message");
        String expectedMessage = "Недостаточно данных для входа";

        assertEquals("Status code is not correct", 400, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isCourierAuthorized);
    }

    //    если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier - Авторизация курьера с некорректными данными")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationCourierWithIncorrectData() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse loginResponse = courierMethods.login(CourierCredential.from(courier));
        int secondStatusCode = loginResponse.extract().statusCode();
        String isCourierAuthorized = loginResponse.extract().path("message");
        String expectedMessage = "Учетная запись не найдена";

        assertEquals("Status code is not correct", 404, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isCourierAuthorized);
    }

}
