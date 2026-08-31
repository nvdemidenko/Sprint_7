package models;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    private Number courierId = null;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {

    }

    // Геттеры обязательны для Jackson/Gson
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public Number getId(){ return courierId; }

}