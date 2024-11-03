package com.pluralsight.contract;

import com.pluralsight.addons.AddOn;
import com.pluralsight.vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                
                // Get common contract data
                String type = data[0];
                String date = data[1];
                String customerName = data[2];
                String customerEmail = data[3];
                
                Vehicle vehicle = new Vehicle(
                    Integer.parseInt(data[4]),    // VIN
                    Integer.parseInt(data[5]),    // Year
                    data[6],                      // Make
                    data[7],                      // Model
                    data[8],                      // Type
                    data[9],                      // Color
                    Integer.parseInt(data[10]),   // Odometer
                    Double.parseDouble(data[11])  // Price
                );

                if (type.equals("SALE")) {
                    boolean isFinanced = data[16].equals("YES");
                    contractRecords.add(new SalesContract(date, customerName, customerEmail, vehicle, isFinanced));
                } else if (type.equals("LEASE")) {
                    contractRecords.add(new LeaseContract(date, customerName, customerEmail, vehicle));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading contracts: " + e.getMessage());
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
