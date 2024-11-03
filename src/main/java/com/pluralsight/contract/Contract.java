package com.pluralsight.contract;

import com.pluralsight.addons.AddOn;
import com.pluralsight.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public abstract class Contract {
    private String date;
    private String customerName;
    private String customerEmail;
    private Vehicle vehicle;
    private List<AddOn> addOns;

    public Contract(String date, String customerName, String customerEmail, Vehicle vehicle) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicle = vehicle;
        this.addOns = new ArrayList<>();
    }

    // Abstract getters
    public abstract double getTotalPrice(); // its public so it can be accessed by the AdminUserInterface wich is in a different package.
    public abstract double getMonthlyPayment();

    // Getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public void addAddOn(AddOn addOn) {
        addOns.add(addOn);
    }

    public List<AddOn> getAddOns() {
        return new ArrayList<>(addOns);
    }

    public double getAddOnsTotal() {
        return addOns.stream()
                    .mapToDouble(AddOn::getPrice)
                    .sum();
    }
}
