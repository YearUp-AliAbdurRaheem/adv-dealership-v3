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
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(FILENAME, true))) {
            StringBuilder line = new StringBuilder();

            if (contract instanceof SalesContract) {
                line.append("SALE|")
                .append(contract.getDate()).append("|")
                .append(contract.getCustomerName()).append("|")
                .append(contract.getCustomerEmail()).append("|")
                .append(contract.getVehicle().getVin()).append("|")
                .append(contract.getVehicle().getYear()).append("|")
                .append(contract.getVehicle().getMake()).append("|")
                .append(contract.getVehicle().getModel()).append("|")
                .append(contract.getVehicle().getVehicleType()).append("|")
                .append(contract.getVehicle().getColor()).append("|")
                .append(contract.getVehicle().getOdometer()).append("|")
                .append(String.format("%.2f", contract.getVehicle().getPrice())).append("|")
                .append(String.format("%.2f|", contract.getTotalPrice() * 0.05))  // Sales tax
                .append("100.00|")  // Recording fee is this arbitrary?
                .append(SalesContract.getProcessingFee()).append("|")
                .append(contract.getTotalPrice()).append("|")
                .append(((SalesContract)contract).isFinanced() ? "YES|" : "NO|") // Casting contract to SalesContract to access isFinanced() method
                .append(((SalesContract)contract).isFinanced() ? contract.getMonthlyPayment() : 0.0); // 0 if not financed
            }

            else if (contract instanceof LeaseContract) {
                line.append("LEASE|")
                .append(contract.getDate()).append("|")
                .append(contract.getCustomerName()).append("|")
                .append(contract.getCustomerEmail()).append("|")
                .append(contract.getVehicle().getVin()).append("|")
                .append(contract.getVehicle().getYear()).append("|")
                .append(contract.getVehicle().getMake()).append("|")
                .append(contract.getVehicle().getModel()).append("|")
                .append(contract.getVehicle().getVehicleType()).append("|")
                .append(contract.getVehicle().getColor()).append("|")
                .append(contract.getVehicle().getOdometer()).append("|")
                .append(String.format("%.2f", contract.getVehicle().getPrice())).append("|")
                .append(((LeaseContract)contract).getExpectedEndingValue()).append("|")
                .append(((LeaseContract)contract).getLeaseFee()).append("|")
                .append(contract.getTotalPrice()).append("|")
                .append(contract.getMonthlyPayment());
            }

            for (AddOn addOn : contract.getAddOns()) {
                line.append("|")
                .append(addOn.getName())
                .append("|")
                .append(addOn.getPrice());
            }

            bWriter.write(line.toString());
            bWriter.newLine();
            bWriter.close();
            loadContracts(); // Reload contracts after saving
        } 
        catch (IOException e) {
            System.out.println("Error saving contract: " + e.getMessage()); // LOOKINTO: How can i close the bWriter here?
        }
    }

    public List<Contract> getAllContracts() {
        return new ArrayList<>(contractRecords); // Return a copy to prevent changes
    }
}
