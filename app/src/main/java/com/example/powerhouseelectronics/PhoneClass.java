package com.example.powerhouseelectronics;

public class PhoneClass {

    private String brand;

    private String model;

    private String color;

    private String storage;

    private String price;

    private String screenResolution;

    private String cameraResolution;

    private String image;


    public PhoneClass(String brand, String model, String color, String storage, String price, String screenResolution, String cameraResolution, String image){
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.storage = storage;
        this.price = price;
        this.screenResolution = screenResolution;
        this.cameraResolution = cameraResolution;
        this.image = image;
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
