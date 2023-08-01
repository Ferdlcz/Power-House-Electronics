package com.example.powerhouseelectronics;

public class Product {

    private String id;
    private String brand;
    private String model;
    private String image;
    private String price;

    private int quantity;

    public Product(String id,String brand, String model, String price, String image) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
