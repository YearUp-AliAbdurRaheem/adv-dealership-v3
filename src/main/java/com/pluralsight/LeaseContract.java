package com.pluralsight;

public class LeaseContract extends Contract {
    private static final double LEASE_FEE_RATE = 0.07;
    private static final double EXPECTED_END_VALUE_RATE = 0.50;
    private static final double FINANCE_RATE = 0.04;
    private static final int LEASE_TERM_MONTHS = 36;

    private double expectedEndingValue;
    private double leaseFee;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicle) {
        super(date, customerName, customerEmail, vehicle);
        this.expectedEndingValue = vehicle.getPrice() * EXPECTED_END_VALUE_RATE; // 50% of its price
        this.leaseFee = vehicle.getPrice() * LEASE_FEE_RATE; // 7% of its price
    }

    public double getExpectedEndingValue() {
        return expectedEndingValue;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    @Override
    public double getTotalPrice() {
        double vehiclePrice = getVehicle().getPrice();
        return (vehiclePrice - expectedEndingValue) + leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        double totalPrice = getTotalPrice();
        double monthlyRate = FINANCE_RATE / 12;
        
        return totalPrice * 
               (monthlyRate * Math.pow(1 + monthlyRate, LEASE_TERM_MONTHS)) / 
               (Math.pow(1 + monthlyRate, LEASE_TERM_MONTHS) - 1);
    }
}
