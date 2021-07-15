package com.nightcafeadmin.app.fooditems;

public class UploadItem {

    public String image;
    public String name;
    public String category;
    public String regular;
    public String large;

    public UploadItem(String url, String name,String category,String regular,String large) {

        this.image = url;
        this.name = name;
        this.category = category;
        this.regular = regular;
        this.large = large;

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
}
