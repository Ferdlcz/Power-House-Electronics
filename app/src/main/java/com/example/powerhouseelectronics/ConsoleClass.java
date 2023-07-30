package com.example.powerhouseelectronics;

import com.google.gson.annotations.SerializedName;

public class ConsoleClass {

    @SerializedName("brand")
    private String brand;

    @SerializedName("model")
    private String model;

    @SerializedName("storage")
    private String storage;

    @SerializedName("price")
    private String price;

    @SerializedName("features")
    private String features;

    @SerializedName("color")
    private String color;

    @SerializedName("stock")
    private String stock;

    @SerializedName("image")
    private String image;


    public ConsoleClass(String brand, String model, String storage, String price, String features, String color, String stock, String imageUri) {
        this.brand = brand;
        this.model = model;
        this.storage = storage;
        this.price = price;
        this.features = features;
        this.color = color;
        this.stock = stock;
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
