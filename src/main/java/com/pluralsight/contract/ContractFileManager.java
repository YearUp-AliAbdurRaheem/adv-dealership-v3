package com.pluralsight.contract;

import com.pluralsight.Program;
import com.pluralsight.addons.AddOn;
import com.pluralsight.vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContractFileManager {
    private static final String FILENAME = "contract.csv";
    private List<Contract> contractRecords;

    public ContractFileManager() {
        contractRecords = new ArrayList<>();
        loadContracts();
    }

    public void loadContracts() {
        contractRecords.clear(); // Clear existing records

        try (Connection connection = Program.dataSource.getConnection();) {
            // Load Sales Contracts
            String salesQuery = "SELECT * FROM SalesContracts";
            try (Statement statement = connection.createStatement();
                 ResultSet salesResultSet = statement.executeQuery(salesQuery)) {
                while (salesResultSet.next()) {
                    Vehicle vehicle = new Vehicle(
                        salesResultSet.getInt("vin"),    // VIN
                        salesResultSet.getInt("vehicle_year"),   // Year
                        salesResultSet.getString("make"), // Make
                        salesResultSet.getString("model"), // Model
                        salesResultSet.getString("vehicle_type"), // Type
                        salesResultSet.getString("color"), // Color
                        (int) salesResultSet.getLong("odometer"), // Odometer is BIGINT so it has to be cast
                        salesResultSet.getDouble("vehicle_price")  // Price
                    );

                    boolean isFinanced = salesResultSet.getBoolean("is_financed");
                    contractRecords.add(new SalesContract(
                        salesResultSet.getString("date"),
                        salesResultSet.getString("customer_name"),
                        salesResultSet.getString("customer_email"),
                        vehicle,
                        isFinanced
                    ));
                }
            }

            // Load Lease Contracts
            String leaseQuery = "SELECT * FROM LeaseContracts";
            try (Statement statement = connection.createStatement();
                 ResultSet leaseResultSet = statement.executeQuery(leaseQuery)) {
                while (leaseResultSet.next()) {
                    Vehicle vehicle = new Vehicle(
                        leaseResultSet.getInt("vin"),    // VIN
                        leaseResultSet.getInt("vehicle_year"),   // Year
                        leaseResultSet.getString("make"), // Make
                        leaseResultSet.getString("model"), // Model
                        leaseResultSet.getString("vehicle_type"), // Type
                        leaseResultSet.getString("color"), // Color
                        (int) leaseResultSet.getLong("odometer"), // Odometer
                        leaseResultSet.getDouble("vehicle_price")  // Price
                    );

                    contractRecords.add(new LeaseContract(
                        leaseResultSet.getString("date"),
                        leaseResultSet.getString("customer_name"),
                        leaseResultSet.getString("customer_email"),
                        vehicle
                    ));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveContract(Contract contract) {
        String insertQuery = "";
        
        if (contract instanceof SalesContract) {
            insertQuery = """
                INSERT INTO SalesContracts (
                    date, customer_name, customer_email, vin, vehicle_year, make, model, 
                    vehicle_type, color, odometer, vehicle_price, sales_tax, recording_fee, 
                    processing_fee, total_price, is_financed, monthly_payment
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        } else if (contract instanceof LeaseContract) {
            insertQuery = """
                INSERT INTO LeaseContracts (
                    date, customer_name, customer_email, vin, vehicle_year, make, model, 
                    vehicle_type, color, odometer, vehicle_price, expected_ending_value, 
                    lease_fee, total_price, monthly_payment
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        }

        try (Connection connection = Program.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            
            // Set parameters for SalesContract
            if (contract instanceof SalesContract) {
                SalesContract salesContract = (SalesContract) contract;
                preparedStatement.setString(1, salesContract.getDate());
                preparedStatement.setString(2, salesContract.getCustomerName());
                preparedStatement.setString(3, salesContract.getCustomerEmail());
                preparedStatement.setInt(4, salesContract.getVehicle().getVin());
                preparedStatement.setInt(5, salesContract.getVehicle().getYear());
                preparedStatement.setString(6, salesContract.getVehicle().getMake());
                preparedStatement.setString(7, salesContract.getVehicle().getModel());
                preparedStatement.setString(8, salesContract.getVehicle().getVehicleType());
                preparedStatement.setString(9, salesContract.getVehicle().getColor());
                preparedStatement.setInt(10, salesContract.getVehicle().getOdometer());
                preparedStatement.setDouble(11, salesContract.getVehicle().getPrice());
                preparedStatement.setDouble(12, salesContract.getTotalPrice() * 0.05);
                preparedStatement.setDouble(13, 100.00);
                preparedStatement.setDouble(14, SalesContract.getProcessingFee());
                preparedStatement.setDouble(15, salesContract.getTotalPrice());
                preparedStatement.setBoolean(16, salesContract.isFinanced());
                preparedStatement.setDouble(17, salesContract.isFinanced() ? salesContract.getMonthlyPayment() : 0.0);
            }
            // Set parameters for LeaseContract
            else if (contract instanceof LeaseContract) {
                LeaseContract leaseContract = (LeaseContract) contract;
                preparedStatement.setString(1, leaseContract.getDate());
                preparedStatement.setString(2, leaseContract.getCustomerName());
                preparedStatement.setString(3, leaseContract.getCustomerEmail());
                preparedStatement.setInt(4, leaseContract.getVehicle().getVin());
                preparedStatement.setInt(5, leaseContract.getVehicle().getYear());
                preparedStatement.setString(6, leaseContract.getVehicle().getMake());
                preparedStatement.setString(7, leaseContract.getVehicle().getModel());
                preparedStatement.setString(8, leaseContract.getVehicle().getVehicleType());
                preparedStatement.setString(9, leaseContract.getVehicle().getColor());
                preparedStatement.setInt(10, leaseContract.getVehicle().getOdometer());
                preparedStatement.setDouble(11, leaseContract.getVehicle().getPrice());
                preparedStatement.setDouble(12, leaseContract.getExpectedEndingValue());
                preparedStatement.setDouble(13, leaseContract.getLeaseFee());
                preparedStatement.setDouble(14, leaseContract.getTotalPrice());
                preparedStatement.setDouble(15, leaseContract.getMonthlyPayment());
            }

            preparedStatement.executeUpdate(); // Execute the query and add the new contract
            loadContracts(); // Reload contracts
        } 
        catch (SQLException e) {
            System.out.println("Error saving contract: " + e.getMessage());
        }
    }

    public List<Contract> getAllContracts() {
        return new ArrayList<>(contractRecords); // Return a copy to prevent changes
    }
}
