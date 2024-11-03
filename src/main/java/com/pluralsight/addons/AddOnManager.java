package com.pluralsight.addons;

import java.util.ArrayList;
import java.util.List;

public class AddOnManager {
    private static final List<AddOn> AVAILABLE_ADDONS = new ArrayList<>();
    
    static {
        // Initialize with our default add-ons
        AVAILABLE_ADDONS.add(new AddOn("Nitrogen Tires", 199.99, "Premium nitrogen tire fill for better pressure stability"));
        AVAILABLE_ADDONS.add(new AddOn("Window Tinting", 299.99, "Professional window tinting for UV protection"));
        AVAILABLE_ADDONS.add(new AddOn("Floor Mats", 129.99, "All-season rubber floor mats"));
        AVAILABLE_ADDONS.add(new AddOn("Splash Guards", 149.99, "Custom-fit splash guards"));
        AVAILABLE_ADDONS.add(new AddOn("Cargo Tray", 89.99, "Heavy-duty cargo area protector"));
        AVAILABLE_ADDONS.add(new AddOn("Wheel Locks", 79.99, "Security wheel locks with unique key"));
    }

    public static List<AddOn> getAvailableAddOns() {
        return new ArrayList<>(AVAILABLE_ADDONS);
    }
}
