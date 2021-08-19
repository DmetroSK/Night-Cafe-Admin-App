package com.nightcafeadmin.app.orders;

public class OrderDetailsModel {

    public String image;
    public String name;
    public String type;
    public String qty;
    public String price;
    public String status;

    public OrderDetailsModel() {

    }

    public OrderDetailsModel(String image, String name, String type, String qty, String price, String status) {
        this.image = image;
        this.name = name;
        this.type = type;
        this.qty = qty;
        this.price = price;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
