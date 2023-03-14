package org.example.courier.model;

public class CourierCredential {
    public String login;
    public String password;

    public CourierCredential(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredential from(Courier courier) {
        return new CourierCredential(courier.getLogin(), courier.getPassword());
    }
}
