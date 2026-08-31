package models;

import java.util.List;

/**
 * Модель данных для создания и получения заказа.
 */
public class Order {

    // Поля, которые отправляются в теле запроса при создании (POST /api/v1/orders)
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTimeInMinutes;
    private List<String> orderColor; // BLACK или GREY. Может быть пустым списком.
    private Integer deliveryFloor;   // Опциональное поле

    // Поля, которые приходят в ответе от сервера
    private Integer track;           // Номер отслеживания (приходит после создания)
    private String status;           // Статус заказа (например, "NEW")

    /** Конструктор только с обязательными полями для создания заказа */
    public Order(String firstName, String lastName, String address, String metroStation,
                 String phone, int rentTimeInMinutes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTimeInMinutes = rentTimeInMinutes;
    }

    /** Полный конструктор на случай, если потребуется создать объект со всеми данными сразу */
    public Order(String firstName, String lastName, String address, String metroStation,
                 String phone, int rentTimeInMinutes, List<String> orderColor, Integer deliveryFloor) {
        this(firstName, lastName, address, metroStation, phone, rentTimeInMinutes);
        this.orderColor = orderColor;
        this.deliveryFloor = deliveryFloor;
    }

    // Пустой конструктор необходим некоторым сериализаторам (Gson/Jackson по умолчанию)
    public Order() { }

    /* --- Геттеры и сеттеры --- */

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRentTimeInMinutes() {
        return rentTimeInMinutes;
    }

    public void setRentTimeInMinutes(int rentTimeInMinutes) {
        this.rentTimeInMinutes = rentTimeInMinutes;
    }

    public List<String> getOrderColor() {
        return orderColor;
    }

    public void setOrderColor(List<String> orderColor) {
        this.orderColor = orderColor;
    }

    public Integer getDeliveryFloor() {
        return deliveryFloor;
    }

    public void setDeliveryFloor(Integer deliveryFloor) {
        this.deliveryFloor = deliveryFloor;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Утилита для тестов: создает минимальный валидный заказ без цветов.
     */
    public static Order minimalValid(String uniqueSuffix) {
        return new Order(
                "Иван" + uniqueSuffix,
                "Иванов" + uniqueSuffix,
                "ул. Тестовая, д. 1, кв. 1",
                "Тверская",
                "+79990000000",
                60
        );
    }

    @Override
    public String toString() {
        return "Order{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", phone='" + phone + '\'' +
                ", rentTimeInMinutes=" + rentTimeInMinutes +
                ", orderColor=" + orderColor +
                ", deliveryFloor=" + deliveryFloor +
                ", track=" + track +
                ", status='" + status + '\'' +
                '}';
    }
}