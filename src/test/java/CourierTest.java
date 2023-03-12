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


public class CourierTest {
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

    //Создание курьера Проверь:
    //курьера можно создать;
    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    //запрос возвращает правильный код ответа;
    //успешный запрос возвращает ok: true;
    //Необходимые тестовые данные создаются перед тестом и удаляются после того, как он выполнится.
    @Test
    @DisplayName("Courier - Создание курьера")
    @Description("Basic test for /api/v1/courier endpoint")
    public void createCourierWithValidData() {
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

    //нельзя создать двух одинаковых курьеров;
    @Test
    @DisplayName("Courier - Создание курьера с теми же логином, паролем, именем")
    @Description("Basic test for /api/v1/courier endpoint")
    public void createCourierWithSameData() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Status code is not correct", 201, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        ValidatableResponse createSecondResponse = courierMethods.create(courier);
        int secondStatusCode = createSecondResponse.extract().statusCode();
        String isSecondCourierCreated = createSecondResponse.extract().path("message");
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";

        assertEquals("Status code is not correct", 409, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isSecondCourierCreated);

    }

    // если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier - Создание курьера, если одного из полей нет")
    @Description("Test for /api/v1/courier endpoint")
    public void createCourierWithoutOneField() {
        Courier courier = new Courier("977799", "Max799");
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        String isCourierCreated = createResponse.extract().path("message");
        String expectedMessage = "Недостаточно данных для создания учетной записи";

        assertEquals("Status code is not correct", 400, statusCode);
        assertEquals("Message is not correct", expectedMessage, isCourierCreated);
    }

    //если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    @DisplayName("Courier - Создание курьера, если создать пользователя с логином, который уже есть")
    @Description("Test for /api/v1/courier endpoint")
    public void createCourierWithSameLogin() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierMethods.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Status code is not correct", 201, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        Courier secondCourier = new Courier(courier.getLogin(), "11", "11");
        ValidatableResponse createSecondResponse = courierMethods.create(secondCourier);
        int secondStatusCode = createSecondResponse.extract().statusCode();
        String isSecondCourierCreated = createSecondResponse.extract().path("message");
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";

        assertEquals("Status code is not correct", 409, secondStatusCode);
        assertEquals("Message is not correct", expectedMessage, isSecondCourierCreated);
    }
}
