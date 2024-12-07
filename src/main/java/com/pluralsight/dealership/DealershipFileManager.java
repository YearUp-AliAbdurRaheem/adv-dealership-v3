package com.pluralsight.dealership;

import com.pluralsight.vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DealershipFileManager {
    private static final String FILE_NAME = "dealership.csv";

    public Dealership getDealership() {
        Dealership dealership = null;

        try (Connection connection = Program.dataSource.getConnection();) {
            String dealershipQuery = "SELECT * FROM dealerships"; // Query to get dealership details
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(dealershipQuery)) {
                if (resultSet.next()) {
                    // Grab the dealership details
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    dealership = new Dealership(name, address, phone);

                    // Now load vehicles associated with this dealership
                    String vehicleQuery = """
                        SELECT v.* FROM vehicles v
                        JOIN inventory i ON v.vin = i.VIN
                        WHERE i.dealership_id = ?
                    """;
                    try (PreparedStatement vehicleStatement = connection.prepareStatement(vehicleQuery)) {
                        vehicleStatement.setInt(1, dealership.getId()); // Assuming you have a method to get the dealership ID
                        try (ResultSet vehicleResultSet = vehicleStatement.executeQuery()) {
                            while (vehicleResultSet.next()) {
                                Vehicle vehicle = new Vehicle(
                                    vehicleResultSet.getInt("vin"),
                                    vehicleResultSet.getInt("year"),
                                    vehicleResultSet.getString("make"),
                                    vehicleResultSet.getString("model"),
                                    vehicleResultSet.getString("vehicle_type"),
                                    vehicleResultSet.getString("color"),
                                    vehicleResultSet.getLong("odometer"), // Keep as long
                                    vehicleResultSet.getDouble("price")
                                );
                                dealership.addVehicle(vehicle);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving dealership: " + e.getMessage());
        }
        return dealership;
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
