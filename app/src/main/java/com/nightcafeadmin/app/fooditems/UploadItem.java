package com.nightcafeadmin.app.fooditems;

public class UploadItem {

    public String image;
    public String name;
    public String category;
    public String regular;
    public String large;
    public String status;

    public UploadItem(String url, String name,String category,String regular,String large,String status) {

        this.image = url;
        this.name = name;
        this.category = category;
        this.regular = regular;
        this.large = large;
        this.status = status;

    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRegular() {
        return regular;
    }

    public String getLarge() {
        return large;
    }

    public String getStatus() {return status;}
}
