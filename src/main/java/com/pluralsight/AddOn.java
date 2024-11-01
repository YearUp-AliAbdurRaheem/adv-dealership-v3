package com.pluralsight;

public class AddOn {
    private String name;
    private double price;
    private String description;

    public AddOn(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("%-20s $%-8.2f - %s", name, price, description);
    }
}
