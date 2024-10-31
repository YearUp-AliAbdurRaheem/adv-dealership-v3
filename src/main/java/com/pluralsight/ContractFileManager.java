package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.oracle.graal.compiler.enterprise.l;

public class ContractFileManager {
    private static final String FILENAME = "contracts.txt";

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
                .append(((SalesContract)contract).isFinanced() ? "YES" : "NO")
                .append(contract.)
            }
    }
}
