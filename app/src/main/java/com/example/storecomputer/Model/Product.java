package com.example.storecomputer.Model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private String status;
    private int stock;
    private String manufacturer;
    private String description;
    private double price;
    private String image;

    public Product(int id, String name, String brand, String status, int stock, String manufacturer, String description, double price, String image) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.status = status;
        this.stock = stock;
        this.manufacturer = manufacturer;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public Product() {
        this(0, "", "", "", 0, "", "", 0, "");
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBrand() {
        return brand;
    }
    public String getStatus() {
        return status;
    }
    public int getStock() {
        return stock;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public String getImage() {
        return image;
    }
}
