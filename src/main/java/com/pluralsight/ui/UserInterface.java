package com.pluralsight.ui;

import com.pluralsight.addons.AddOn;
import com.pluralsight.addons.AddOnManager;
import com.pluralsight.contract.Contract;
import com.pluralsight.contract.ContractFileManager;
import com.pluralsight.contract.LeaseContract;
import com.pluralsight.contract.SalesContract;
import com.pluralsight.dealership.Dealership;
import com.pluralsight.dealership.DealershipFileManager;
import com.pluralsight.utils.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.pluralsight.admin.AdminUserInterface;
import com.pluralsight.vehicle.Vehicle;

public class UserInterface {
    private Dealership dealership;
    private List<Contract> contracts;

    public UserInterface() {
        // Constructor
    }

    private void initializeDealership() { // This is the init() method mentioned in workbook 4. i just gave it a better name.
        dealership = new DealershipFileManager().getDealership();
    }
    
    private void displayVehicles(ArrayList<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        System.out.printf("%-10s %-4s %-10s %-10s %-10s %-10s %-12s %-10s%n", "VIN", "Year", "Make", "Model", "Type", "Color", "Mileage", "Price");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.toString());
        }
        System.out.println();
    }


    
    public void display() {
        

        initializeDealership(); // Loads the dealership from the file.
        String options = """
                Please select from the following choices:
                1 - Find vehicles within a price range
                2 - Find vehicles by make / model
                3 - Find vehicles by year range
                4 - Find vehicles by color
                5 - Find vehicles by mileage range
                6 - Find vehicles by type (car, truck, SUV, van)
                7 - List ALL vehicles
                8 - Add a vehicle
                9 - Remove a vehicle
                10 - Sell/Lease a Vehicle
                11 - Admin Interface
                99 - Quit

                >>>\s""";

        int selection = 0;
        String input;

        // User Interface Loop
        do {
            contracts = new ContractFileManager().getAllContracts();
            System.out.println("\nWelcome to " + dealership.getName() + "!");
            input = Console.PromptForString(options);
            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input = "";
                continue;
            }
            switch (selection) {
                case 1 -> processGetByPriceRequest();
                case 2 -> processGetByMakeModelRequest();
                case 3 -> processGetByYearRequest();
                case 4 -> processGetByColorRequest();
                case 5 -> processGetByMileageRequest();
                case 6 -> processGetByVehicleTypeRequest();
                case 7 -> processGetAllVehiclesRequest();
                case 8 -> processAddVehicleRequest();
                case 9 -> processRemoveVehicleRequest();
                case 10 -> processSellLeaseRequest();
                case 11 -> new AdminUserInterface().display();
                case 99 -> System.exit(0);
                default -> {
                    System.out.println("Invalid selection. Please try again.");
                    input = "";
                }
            }
        } while (input.isEmpty() || selection != 99);
    }

    public void processSellLeaseRequest() {
        int vin = 0;
        String input;
        // Get all the info we need from the user
        // Get VIN
        do {
            input = Console.PromptForString("Enter VIN of the vehicle to sell/lease (or 'v' to view all vehicles or 'q' to cancel): ");
            if (input.equalsIgnoreCase("q")) return;
            if (input.equalsIgnoreCase("v")) {displayVehicles(dealership.getAllVehicles()); input = ""; continue;}

            try {
                vin = Integer.parseInt(input);

                Vehicle vehicleToSell = dealership.getVehicleByVin(vin);
                if (vehicleToSell == null) {
                    System.out.println("Vehicle not found. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        // Get contract type
        String contractType;
        do {
            contractType = Console.PromptForString("Enter contract type (sale/lease) (or 'q' to cancel): ");
            if (contractType.equalsIgnoreCase("q")) return;
            if (!contractType.equalsIgnoreCase("sale") && !contractType.equalsIgnoreCase("lease")) {
                System.out.println("Invalid contract type. Please enter 'sale' or 'lease'.");
                contractType = ""; // Reset input
            }
        } while (contractType.isEmpty());

        // Get customer name
        String customerName;
        do {
            customerName = Console.PromptForString("Enter customer name (or 'q' to cancel): ");
            if (customerName.equalsIgnoreCase("q")) return;
        } while (customerName.isEmpty());

        // Get customer email
        String customerEmail;
        do {
            customerEmail = Console.PromptForString("Enter customer email (or 'q' to cancel): ");
            if (customerEmail.equalsIgnoreCase("q")) return;
        } while (customerEmail.isEmpty());

        // Get date
        String date;
        do {
            date = Console.PromptForString("Enter date (YYYYMMDD) (or 'q' to cancel): ");
            if (date.equalsIgnoreCase("q")) return;
            
            // Validate date format
            if (date.length() != 8 || !date.matches("\\d{8}")) {
                System.out.println("Invalid date format. Please use YYYYMMDD (e.g., 20210928)");
                date = ""; // Reset input
                continue;
            }
        } while (date.isEmpty());

        // Add-on selection
        List<AddOn> selectedAddOns = new ArrayList<>();
        boolean selectingAddOns = true;

        System.out.println("\nAvailable Add-ons:");
        List<AddOn> availableAddOns = AddOnManager.getAvailableAddOns();
        for (int i = 0; i < availableAddOns.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, availableAddOns.get(i));
        }

        while (selectingAddOns) {
            String addOnInput = Console.PromptForString("\nSelect an add-on number (or 'done' to finish, 'q' to cancel): ");
            if (addOnInput.equalsIgnoreCase("q")) return;
            if (addOnInput.equalsIgnoreCase("done")) break;
            
            try {
                int selection = Integer.parseInt(addOnInput) - 1;
                if (selection >= 0 && selection < availableAddOns.size()) {
                    AddOn selected = availableAddOns.get(selection);
                    selectedAddOns.add(selected);
                    System.out.printf("Added: %s%n", selected);
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        Vehicle vehicle = dealership.getVehicleByVin(vin);
        Contract contract;

        // Create appropriate contract type
        if (contractType.equalsIgnoreCase("sale")) {
            String financeInput;
            boolean isFinanced;
            do {
                financeInput = Console.PromptForString("Will this be financed? (yes/no) (or 'q' to cancel): ");
                if (financeInput.equalsIgnoreCase("q")) return;
                if (financeInput.equalsIgnoreCase("yes")) {
                    isFinanced = true;
                    break;
                } else if (financeInput.equalsIgnoreCase("no")) {
                    isFinanced = false;
                    break;
                }
                System.out.println("Please enter 'yes' or 'no'.");
            } while (true);

            contract = new SalesContract(date, customerName, customerEmail, vehicle, isFinanced);
        } else {
            // Check if vehicle is too old for lease (more than 3yo)
            if (java.time.Year.now().getValue() - vehicle.getYear() > 3) {
                System.out.println("Vehicle is too old to lease (more than 3 years old).");
                return;
            }
            contract = new LeaseContract(date, customerName, customerEmail, vehicle);
        }

        // Save contract and remove vehicle
        new ContractFileManager().saveContract(contract);
        dealership.removeVehicle(vehicle);
        new DealershipFileManager().saveDealership(dealership);

        // Display success message with contract details
        System.out.printf("\nContract processed successfully!\n");
        System.out.printf("Total Price: $%.2f\n", contract.getTotalPrice());
        System.out.printf("Monthly Payment: $%.2f\n\n", contract.getMonthlyPayment());
    }

    public void processGetByPriceRequest() {
        String input = "";
        double minPrice = 0, maxPrice = 0;

        do {
            input = Console.PromptForString("Enter minimum price (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                minPrice = Double.parseDouble(input);
                // Price Validation
                if (minPrice < 0) {
                    System.out.println("Price cannot be negative. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        do {
            input = Console.PromptForString("Enter maximum price (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                maxPrice = Double.parseDouble(input);
                // Price Validation
                if (maxPrice < minPrice) {
                    System.out.println("Maximum price cannot be less than minimum price. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        displayVehicles(dealership.getVehiclesByPrice(minPrice, maxPrice));
    }

    public void processGetByMakeModelRequest() {
        String make, model;

        do {
            make = Console.PromptForString("Enter make (or 'q' to cancel): ");
            if (make.equals("q")) {
                return;
            }
        } while (make.isEmpty());

        do {
            model = Console.PromptForString("Enter model (or 'q' to cancel): ");
            if (model.equals("q")) {
                return;
            }
        } while (model.isEmpty());

        displayVehicles(dealership.getVehiclesByMakeModel(make, model));
    }

    public void processGetByYearRequest() {
        String input = "";
        int minYear = 1900, maxYear = 0;

        do {
            input = Console.PromptForString("Enter minimum year (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                minYear = Integer.parseInt(input);
                // Year Validation
                if (!isValidYear(minYear)) {
                    System.out.println("Year cannot be less than 1900. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid year.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        do {
            input = Console.PromptForString("Enter maximum year (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                maxYear = Integer.parseInt(input);
                // Year Validation
                if (!isValidYear(maxYear)) {
                    System.out.println("Year cannot be less than 1900. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                if (maxYear < minYear) {
                    System.out.println("Maximum year cannot be less than minimum year. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid year.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        displayVehicles(dealership.getVehiclesByYear(minYear, maxYear));
    }

    public void processGetByColorRequest() {
        String color;

        do {
            color = Console.PromptForString("Enter make (or 'q' to cancel): ");
            if (color.equals("q")) {
                return;
            }
        } while (color.isEmpty());

        displayVehicles(dealership.getVehiclesByColor(color));
    }

    public void processGetByMileageRequest() {
        String input = "";
        int minMileage = 0, maxMileage = 0;

        do {
            input = Console.PromptForString("Enter minimum mileage (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                minMileage = Integer.parseInt(input);
                // mileage Validation
                if (minMileage < 0) {
                    System.out.println("Mileage cannot be negative. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid mileage.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        do {
            input = Console.PromptForString("Enter maximum mileage (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                maxMileage = Integer.parseInt(input);
                // mileage Validation
                if (maxMileage < 0) {
                    System.out.println("Mileage cannot be negative. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                if (maxMileage < minMileage) {
                    System.out.println("Maximum mileage cannot be less than minimum mileage. Please try again.");
                    input = ""; // Reset input
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid mileage.");
                input = ""; // Reset input
            }
        } while (input.isEmpty());

        displayVehicles(dealership.getVehiclesByMileage(minMileage, maxMileage));
    }

    public void processGetByVehicleTypeRequest() {
        String vehicleType;

        do {
            vehicleType = Console.PromptForString("Enter vehicle type (car, truck, SUV, van) (or 'q' to cancel): ");
            if (vehicleType.equals("q")) {
                return;
            }
        } while (vehicleType.isEmpty() || !isValidVehicleType(vehicleType));

        displayVehicles(dealership.getVehiclesByType(vehicleType));
    }

    public void processGetAllVehiclesRequest() {
        displayVehicles(dealership.getAllVehicles());
    }

    public void processAddVehicleRequest() {
        int vin;
        do {
            String vinInput = Console.PromptForString("Enter VIN of the vehicle (or 'q' to cancel): ");
            if (vinInput.equalsIgnoreCase("q")) return;
            try {
                vin = Integer.parseInt(vinInput);
                if (vin <= 0) {
                    System.out.println("VIN must be a positive number.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for VIN.");
            }
        } while (true);

        String make;
        do {
            make = Console.PromptForString("Enter make of the vehicle (or 'q' to cancel): ");
            if (make.equalsIgnoreCase("q")) return;
        } while (make.isEmpty());

        String model;
        do {
            model = Console.PromptForString("Enter model of the vehicle (or 'q' to cancel): ");
            if (model.equalsIgnoreCase("q")) return;
        } while (model.isEmpty());

        int year;
        do {
            String yearInput = Console.PromptForString("Enter year of the vehicle (or 'q' to cancel): ");
            if (yearInput.equalsIgnoreCase("q")) return;
            try {
                year = Integer.parseInt(yearInput);
                if (!isValidYear(year)) {
                    System.out.println("Year cannot be less than 1900. Please try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid year.");
            }
        } while (true);

        String color;
        do {
            color = Console.PromptForString("Enter color of the vehicle (or 'q' to cancel): ");
            if (color.equalsIgnoreCase("q")) return;
        } while (color.isEmpty());

        double price;
        do {
            String priceInput = Console.PromptForString("Enter price of the vehicle (or 'q' to cancel): ");
            if (priceInput.equalsIgnoreCase("q")) return;
            try {
                price = Double.parseDouble(priceInput);
                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        } while (true);

        int odometer;
        do {
            String odometerInput = Console.PromptForString("Enter odometer reading (or 'q' to cancel): ");
            if (odometerInput.equalsIgnoreCase("q")) return;
            try {
                odometer = Integer.parseInt(odometerInput);
                if (odometer < 0) {
                    System.out.println("Odometer cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } while (true);

        String vehicleType;
        do {
            vehicleType = Console.PromptForString("Enter vehicle type (car, truck, SUV, van) (or 'q' to cancel): ");
            if (vehicleType.equalsIgnoreCase("q")) return;
        } while (vehicleType.isEmpty() || !isValidVehicleType(vehicleType));

        Vehicle newVehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        dealership.addVehicle(newVehicle);
        new DealershipFileManager().saveDealership(dealership); // Save the updated dealership to its file
        System.out.println("Added " + vehicleType + " with VIN " + vin + "!\n");
    }

    public void processRemoveVehicleRequest() {
        int vin = 0;
        
        while (true) {
            String input = Console.PromptForString("Enter VIN of the vehicle to remove (or 'q' to cancel): ");
            if (input.equalsIgnoreCase("q")) return;
            try {
                vin = Integer.parseInt(input);
                Vehicle vehicleToRemove = dealership.getVehicleByVin(vin);
                if (vehicleToRemove != null) {
                    dealership.removeVehicle(vehicleToRemove);
                    System.out.println("Removed " + vehicleToRemove.getVehicleType() + " with VIN " + vin + "!\n");
                    break;
                } else {
                    System.out.println("Vehicle not found. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for VIN.");
            }
        }
    }

    private static final String[] VALID_VEHICLE_TYPES = {"car", "truck", "suv", "van"}; // Helper section to validate stuff

    private boolean isValidVehicleType(String vehicleType) {
        return Arrays.stream(VALID_VEHICLE_TYPES)
                     .anyMatch(type -> type.equalsIgnoreCase(vehicleType)); // New type of stream :D
    }

    private boolean isValidYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year >= 1900 && year <= currentYear + 1;  // Allow current year + 1 for newer models
    }
}
