package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DealershipFileManager {
    private static final String FILE_NAME = "dealership.csv";

    public Dealership getDealership() {
        Dealership dealership = null;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            // Read the first line with the dealership info
            String line = br.readLine();
            if (line != null) {
                // Grab the dealership details
                String[] dealershipInfo = line.split("\\|");
                String name = dealershipInfo[0];
                String address = dealershipInfo[1];
                String phone = dealershipInfo[2];
                dealership = new Dealership(name, address, phone);

                // Read and add vehicles to the dealership
                while ((line = br.readLine()) != null) {
                    String[] vehicleData = line.split("\\|");
                    Vehicle vehicle = createVehicle(vehicleData);
                    dealership.addVehicle(vehicle);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading dealership file: " + e.getMessage());
        }
        return dealership;
    }

    private Vehicle createVehicle(String[] data) { // Helper method to extract some logic from the getDealership method.
        int vin = Integer.parseInt(data[0]);
        int year = Integer.parseInt(data[1]);
        String make = data[2];
        String model = data[3];
        String vehicleType = data[4];
        String color = data[5];
        int odometer = Integer.parseInt(data[6]);
        double price = Double.parseDouble(data[7]);
        // Create and return a new Vehicle object, this is added to its inventory.
        return new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
    }

    public void saveDealership(Dealership dealership) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME)) {
            // Write dealership info first (header)
            fileWriter.write(String.format("%s|%s|%s%n",
                dealership.getName(),
                dealership.getAddress(), 
                dealership.getPhoneNumber()));
            
            // Write all vehicles to the file
            for (Vehicle vehicle : dealership.getAllVehicles()) {
                fileWriter.write(String.format("%d|%d|%s|%s|%s|%s|%d|%.2f%n",
                    vehicle.getVin(),
                    vehicle.getYear(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getVehicleType(),
                    vehicle.getColor(),
                    vehicle.getOdometer(),
                    vehicle.getPrice()));
            }
        } catch (IOException e) {
            System.out.println("Error writing to dealership file: " + e.getMessage());
        }
    }
}
