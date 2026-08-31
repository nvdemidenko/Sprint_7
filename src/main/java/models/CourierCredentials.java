package models;

public class CourierCredentials {
    private String login;
    private String password;

    // Пустой конструктор для библиотек сериализации (Gson/Jackson)
    public CourierCredentials() {}

    /**
     * Основной конструктор для создания учетных данных курьера.
     *
     * @param login    Логин курьера
     * @param password Пароль курьера
     */
    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Геттеры и сеттеры остаются без изменений
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}