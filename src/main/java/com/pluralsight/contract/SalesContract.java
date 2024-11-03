package com.pluralsight.contract;

import com.pluralsight.vehicle.Vehicle;

public class SalesContract extends Contract {
    private static final double SALES_TAX_RATE = 0.05;
    private static final double PROCESSING_FEE = 295;
    private boolean isFinanced;

    public SalesContract(String date, String customerName, String customerEmail,
                         Vehicle vehicle, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicle);
        this.isFinanced = isFinanced;
    }

    public static double getProcessingFee() {
        return PROCESSING_FEE;
    }

    public boolean isFinanced() {
        return isFinanced;
    }


    @Override
    public double getTotalPrice() {
        double vehiclePrice = getVehicle().getPrice();
        double salesTax = vehiclePrice * SALES_TAX_RATE;
        double processingFee = vehiclePrice < 10000 ? 295 : 495;  
        
        return vehiclePrice + salesTax + processingFee; 
    }

    @Override
    public double getMonthlyPayment() {
        if (!isFinanced) {
            return 0.0;
        }
        
        double amount = getTotalPrice();
        int months = getVehicle().getPrice() >= 10000 ? 48 : 24;
        double rate = getVehicle().getPrice() >= 10000 ? 0.0425 : 0.0525;
        
        return amount * (rate/12 * Math.pow(1 + rate/12, months)) / (Math.pow(1 + rate/12, months) - 1);
    }
}
