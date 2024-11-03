package com.pluralsight.admin;

import com.pluralsight.contract.Contract;
import com.pluralsight.contract.ContractFileManager;
import com.pluralsight.contract.SalesContract;
import com.pluralsight.contract.LeaseContract;
import com.pluralsight.utils.Console;
import java.util.List;

public class AdminUserInterface {
    private ContractFileManager contractManager;

    public AdminUserInterface() {
        this.contractManager = new ContractFileManager();
    }

    public void display() {
        AuthenticationManager authManager = new AuthenticationManager();
        int tries = 3;
        boolean authenticated = false;
        
        do {
            System.out.println("Admin Login: " + tries + " tries remaining");
            authenticated = authManager.login();
            tries--;
        } while (!authenticated && tries > 0);

        if (!authenticated) {
            System.out.println("Too many failed attempts. Exiting program.");
            System.exit(0);
        }

        contractManager = new ContractFileManager(); // reload contracts

        String options = """
                
                ADMIN INTERFACE
                Please select from the following choices:
                1 - All
                2 - Last 10
                3 - All Sales
                4 - Last 10 Sales
                5 - All Lease
                6 - Last 10 Lease
                99 - Quit
                
                >>>\s""";

        int selection = 0;
        do {
            String input = Console.PromptForString(options);
            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (selection) {
                case 1 -> displayAllContracts();
                case 2 -> displayLastNContracts(10);
                case 3 -> displayAllSalesContracts();
                case 4 -> displayLastNSalesContracts(10);
                case 5 -> displayAllLeaseContracts();
                case 6 -> displayLastNLeaseContracts(10);
                case 99 -> System.out.println("Safely exiting Admin Interface...");
                default -> System.out.println("Invalid selection. Please try again.");
            }
        } while (selection != 99);
    }

    private void displayAllContracts() {
        List<Contract> contracts = contractManager.getAllContracts();
        displayContracts(contracts);
    }

    private void displayLastNContracts(int n) {
        List<Contract> contracts = contractManager.getAllContracts();
        displayContracts(getLastN(contracts, n));
    }

    private void displayAllSalesContracts() {
        List<Contract> contracts = contractManager.getAllContracts().stream()
                .filter(c -> c instanceof SalesContract)
                .toList();
        displayContracts(contracts);
    }

    private void displayLastNSalesContracts(int n) {
        List<Contract> contracts = contractManager.getAllContracts().stream()
                .filter(c -> c instanceof SalesContract)
                .toList();
        displayContracts(getLastN(contracts, n));
    }

    private void displayAllLeaseContracts() {
        List<Contract> contracts = contractManager.getAllContracts().stream()
                .filter(c -> c instanceof LeaseContract)
                .toList();
        displayContracts(contracts);
    }

    private void displayLastNLeaseContracts(int n) {
        List<Contract> contracts = contractManager.getAllContracts().stream()
                .filter(c -> c instanceof LeaseContract)
                .toList();
        displayContracts(getLastN(contracts, n));
    }

    private List<Contract> getLastN(List<Contract> contracts, int n) { // Helper method to get the last n contracts
        int startIndex = Math.max(0, contracts.size() - n); // This makes sure we dont error is n > num on contracts on file
        return contracts.subList(startIndex, contracts.size());
    }

    private void displayContracts(List<Contract> contracts) {
        if (contracts.isEmpty()) {
            System.out.println("No contracts found.");
            return;
        }

        System.out.printf("%-10s %-20s %-25s %-15s %-25s %-15s%n", 
            "Date", "Customer", "Email", "Type", "Vehicle", "Total Price");
        System.out.println("-".repeat(115));
        
        for (Contract contract : contracts) {
            System.out.printf("%-10s %-20s %-25s %-15s %-25s $%-14.2f%n",
                contract.getDate(),
                contract.getCustomerName(),
                contract.getCustomerEmail(),
                contract instanceof SalesContract ? "Sale" : "Lease",
                contract.getVehicle().getMake() + " " + contract.getVehicle().getModel(),
                contract.getTotalPrice());
        }
        System.out.println();
    }
}
