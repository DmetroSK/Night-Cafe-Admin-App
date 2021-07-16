package com.nightcafeadmin.app.fooditems;

public class ItemModel {

    public String image;
    public String name;
    public String category;
    public String regular;
    public String large;
    public String status;


    ItemModel(){

    }

    public ItemModel(String image, String name, String category, String regular, String large,String status) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.regular = regular;
        this.large = large;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
