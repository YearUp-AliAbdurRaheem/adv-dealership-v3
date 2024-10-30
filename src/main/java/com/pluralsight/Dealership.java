package com.pluralsight;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Dealership {
    private String name;
    private String address;
    private String phoneNumber;
    private ArrayList<Vehicle> inventory;

    public Dealership(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.inventory = new ArrayList<>(); // Initialize the inventory list
    }


    public ArrayList<Vehicle> getVehiclesByPrice(double minPrice, double maxPrice) {
        return new ArrayList<>(inventory.stream() // Because .collect returns and arraylist not a list we have to wrap the output in an arraylist to get one
            .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice) // btw in python we can do minPrice <= vehicle.price <= maxPrice
            .collect(Collectors.toList()));
    }
    public ArrayList<Vehicle> getVehiclesByMakeModel(String make, String model) {
        return new ArrayList<>(inventory.stream()
            .filter(vehicle -> vehicle.getMake().equalsIgnoreCase(make) && vehicle.getModel().equalsIgnoreCase(model))
            .collect(Collectors.toList()));
    }
    public ArrayList<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        return new ArrayList<>(inventory.stream()
            .filter(vehicle -> vehicle.getYear() >= minYear && vehicle.getYear() <= maxYear)
            .collect(Collectors.toList()));
    }
    public ArrayList<Vehicle> getVehiclesByColor(String color) {
        return new ArrayList<>(inventory.stream()
            .filter(vehicle -> vehicle.getColor().equalsIgnoreCase(color))
            .collect(Collectors.toList()));
    }
    public ArrayList<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        return new ArrayList<>(inventory.stream()
            .filter(vehicle -> vehicle.getOdometer() >= minMileage && vehicle.getOdometer() <= maxMileage)
            .collect(Collectors.toList()));
    }
    public ArrayList<Vehicle> getVehiclesByType(String vehicleType) {
        return new ArrayList<>(inventory.stream()
            .filter(vehicle -> vehicle.getVehicleType().equalsIgnoreCase(vehicleType))
            .collect(Collectors.toList()));
    }

    public Vehicle getVehicleByVin(int vin) {
        return inventory.stream()
            .filter(vehicle -> vehicle.getVin() == vin)
            .findFirst()
            .orElse(null);
    }
    public ArrayList<Vehicle> getAllVehicles() {
        return inventory; // Bruh this is just a getter for inventory
    }

    public void addVehicle(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        inventory.remove(vehicle);
    }

    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
}
