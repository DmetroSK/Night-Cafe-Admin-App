package com.nightcafeadmin.app.orders;

public class OrdersModel {

    public String phone;
    public String id;

    public OrdersModel(){

    }

    public OrdersModel(String phone, String id) {

        this.phone = phone;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
