package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import com.pluralsight.ui.UserInterface;

public class Program {
    // Class-level variables to hold the arguments
    public static String username;
    public static String password;
    public static String sqlServerAddress;
    public static BasicDataSource dataSource;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(
                "Application needs three arguments to run: " +
                "java com.pluralsight.UsingDriverManager <username> <password> <address>");
            System.exit(1);
        }

        // Set args
        username = args[0];
        password = args[1];
        sqlServerAddress = args[2];

        // load the MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load SQL driver");
        }

        dataSource.setUrl(sqlServerAddress); // Dealerships
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Load the UI
        UserInterface userInterface = new UserInterface();
        userInterface.display();
    }
}
