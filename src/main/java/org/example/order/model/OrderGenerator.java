package org.example.order.model;

public class OrderGenerator {
    public static Order getOrder() {
        return new Order(
                "Максим",
                "Кукушкин",
                "Москва, пр. Кутузовский, 32",
                "Кутузовская",
                "+7 800 355 35 35",
                4,
                "2023-03-23",
                "Заказ Кукушкина"
        );
    }
}
