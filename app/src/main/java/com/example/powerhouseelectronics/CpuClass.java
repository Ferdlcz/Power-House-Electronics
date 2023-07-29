package com.example.powerhouseelectronics;

import com.google.gson.annotations.SerializedName;

public class CpuClass {

    @SerializedName("brand")
    private String brand;

    @SerializedName("model")
    private String model;

    @SerializedName("processor")
    private String processor;

    @SerializedName("ram")
    private String ram;

    @SerializedName("storage")
    private String storage;

    @SerializedName("price")
    private String price;

    @SerializedName("operatingSystem")
    private String operatingSystem;

    @SerializedName("graphicsCard")
    private String graphicsCard;

    @SerializedName("stock")
    private String stock;

    @SerializedName("image")
    private String image;


    public CpuClass(String brand, String model, String processor, String ram, String storage, String price, String operatingSystem, String graphicsCard, String stock, String imageUri) {
        this.brand = brand;
        this.model = model;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
        this.price = price;
        this.operatingSystem = operatingSystem;
        this.graphicsCard = graphicsCard;
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

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
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

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
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
