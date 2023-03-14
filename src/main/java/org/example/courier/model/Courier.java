package org.example.courier.model;

public class Courier {
    public String login;
    public String password;
    public String firstName;

    public Courier() {
    }

    public Courier(String password, String firstName) {
        this.password = password;
        this.firstName = firstName;
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
