package com.example.modeldata;

public class Product {
    private String name;
    private String brand;
    private double price; // Change to double
    private int imageResId;
    private String description;
    private int quantity;

    public Product(String name, String brand, double price, int imageResId, String description, int quantity) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageResId = imageResId;
        this.description = description;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

}
