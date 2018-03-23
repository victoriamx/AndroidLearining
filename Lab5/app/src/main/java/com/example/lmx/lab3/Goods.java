package com.example.lmx.lab3;

import java.io.Serializable;

public class Goods implements Serializable {
    private String firstLetter;
    private String name;
    private String price;
    private String type;
    private String detail;
    private int imageId;

    public Goods(String name, String price, String type, String detail, int imageId) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.detail = detail;
        this.imageId = imageId;
        firstLetter = name.charAt(0) + "";
    }

    public int getImageId() {
        return imageId;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getInfomation() {
        return detail;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}