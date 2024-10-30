package com.pluralsight;

import com.pluralsight.utils.Console;

import java.util.ArrayList;
import java.util.Arrays;

public class UserInterface {
    private Dealership dealership;

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
                99 - Quit

                >>>\s""";

        int selection;

        // User Interface Loop
        do {
            System.out.println("Welcome to " + dealership.getName() + "!");
            selection = Console.PromptForInt(options);
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
                case 99 -> System.exit(0);
                default -> System.out.println("Invalid selection. Please try again.");
            }
        } while (selection != 99);
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
        int minYear = 0, maxYear = 0;

        do {
            input = Console.PromptForString("Enter minimum year (or 'q' to cancel): ");
            if (input.equals("q")) {
                return;
            }
            try {
                minYear = Integer.parseInt(input);
                // Year Validation
                if (minYear < 0) {
                    System.out.println("Year cannot be negative. Please try again.");
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
                if (maxYear < 0) {
                    System.out.println("Year cannot be negative. Please try again.");
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
                if (year <= 0) {
                    System.out.println("Year must be positive.");
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

    private static final String[] VALID_VEHICLE_TYPES = {"car", "truck", "suv", "van"}; // Helper section to validate vehicle types

    private boolean isValidVehicleType(String vehicleType) {
        return Arrays.stream(VALID_VEHICLE_TYPES)
                     .anyMatch(type -> type.equalsIgnoreCase(vehicleType)); // New type of stream :D
    }
}
