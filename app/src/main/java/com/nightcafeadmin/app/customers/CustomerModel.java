package com.nightcafeadmin.app.customers;

public class CustomerModel {

    public String fullName;
    public String phone;
    public String street;
    public String city;

    public CustomerModel(){

    }

    public CustomerModel(String name, String phone, String street, String city) {

        this.fullName = name;
        this.phone = phone;
        this.street = street;
        this.city = city;

    }

    public String getName() {
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
