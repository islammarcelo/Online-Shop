package com.example.shop.Model;

public class AdminOrders {
    private String address, city, data, name, phone, state, time, totalPrice;

    public AdminOrders(String address, String city, String data, String name, String phone, String state, String time, String totalPrice) {
        this.address = address;
        this.city = city;
        this.data = data;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalPrice = totalPrice;
    }

    public AdminOrders() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
