package com.example.powerhouseelectronics;

import com.google.gson.annotations.SerializedName;

public class PhoneClass {
    @SerializedName("brand")
    private String brand;

    @SerializedName("model")
    private String model;

    @SerializedName("color")
    private String color;

    @SerializedName("storage")
    private String storage;

    @SerializedName("price")
    private String price;

    @SerializedName("screenResolution")
    private String screenResolution;

    @SerializedName("cameraResolution")
    private String cameraResolution;

    @SerializedName("image")
    private String image;


    public PhoneClass(String brand, String model, String color, String storage, String price, String screenResolution, String cameraResolution, String imageUri) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.storage = storage;
        this.price = price;
        this.screenResolution = screenResolution;
        this.cameraResolution = cameraResolution;
        this.image = imageUri != null ? imageUri.toString(): null;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getCameraResolution() {
        return cameraResolution;
    }

    public void setCameraResolution(String cameraResolution) {
        this.cameraResolution = cameraResolution;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
