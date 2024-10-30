package com.pluralsight;
import com.pluralsight.utils.ColorCodes;

public class Vehicle {
    private int vin;
    private int year;
    private String make;
    private String model;
    private String vehicleType; // Can only be "car", "truck, "SUV", or "van".
    private String color;
    private int odometer;
    private double price;

    public Vehicle(int vin, int year, String make, String model, 
                   String vehicleType, String color, int odometer, double price) {
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.color = color;
        this.odometer = odometer;
        this.price = price;
    }

    public int getVin() {
        return vin;
    }

    public void setVin(int vin) {
        if (vin > 0) {
            this.vin = vin;
        } else {
            throw new IllegalArgumentException("VIN must be positive!");
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        if (make != null && !make.trim().isEmpty()) { // The trim() ensures that the make is not just whitespace
            this.make = make;
        } else {
            throw new IllegalArgumentException("Make cannot be null or empty!");
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model != null && !model.trim().isEmpty()) {
            this.model = model;
        } else {
            throw new IllegalArgumentException("Model cannot be null or empty!");
        }
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        if (vehicleType != null && !vehicleType.trim().isEmpty()) { 
            String type = vehicleType.trim().toLowerCase();
            switch (type) {          // Ensures that the vehicleType is can only be "car", "truck", "SUV", or "van".
                case "car", "truck", "suv", "van":
                    this.vehicleType = type; break;
                default:
                    throw new IllegalArgumentException("Vehicle type must be 'car', 'truck', 'SUV', or 'van'!"); // Throws an error if its is not.
            }
        } else {
            throw new IllegalArgumentException("Vehicle type cannot be null or empty!");
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color != null && !color.trim().isEmpty()) {
            this.color = color;
        } else {
            throw new IllegalArgumentException("Color cannot be null or empty!");
        }
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        if (odometer >= 0) {
            this.odometer = odometer;
        } else {
            throw new IllegalArgumentException("Odometer cannot be negative!");
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative!");
        }
    }

    @Override
    public String toString() {
        String colorString;
        switch (color.toLowerCase()) {
            case "red":
                colorString = ColorCodes.RED + color + ColorCodes.RESET; break;
            case "white":
                colorString = ColorCodes.WHITE + color + ColorCodes.RESET; break;
            case "blue":
                colorString = ColorCodes.BLUE + color + ColorCodes.RESET; break;
            case "black":
                colorString = ColorCodes.BLACK + color + ColorCodes.RESET; break;
            case "green":
                colorString = ColorCodes.GREEN + color + ColorCodes.RESET; break;
            case "gray":
                colorString = ColorCodes.GRAY + color + ColorCodes.RESET; break;
            case "silver":
                colorString = ColorCodes.SILVER + color + ColorCodes.RESET; break;
            default:
                colorString = color; break;
        }
        return String.format("%-10d %-4d %-10s %-10s %-10s %-20s %-10d %-10.2f", 
                             vin, year, make, model, vehicleType, colorString, odometer, price);
    }
}
