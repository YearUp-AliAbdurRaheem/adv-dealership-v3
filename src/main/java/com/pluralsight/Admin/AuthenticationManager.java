package com.pluralsight.Admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.pluralsight.utils.Console;

public class AuthenticationManager {
    private final String LOGIN_FILE = "login.csv";
    private HashMap<String, String> logins;

    public AuthenticationManager() {
        loadLogins();
    }

    private void loadLogins() {
        logins = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOGIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                logins.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("Error loading logins: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) { // If the file is empty user needs to create an admin account
            // Could log here but idk how rn
        }
    }

    public boolean login() {
        String username, password;

        if (logins.isEmpty()) {
            System.out.println("No logins found. Please create an admin account first.");
            do {
                username = Console.PromptForString("Enter your username: ");
            } while (username.isEmpty());
    
            do {
                password = hashPassword(Console.PromptForString("Enter your password: "));
            } while (password.isEmpty());
            createAdmin(username, password);
            return true;
        }

        do {
            username = Console.PromptForString("Enter your username: ");
        } while (username.isEmpty());

        do {
            password = hashPassword(Console.PromptForString("Enter your password: "));
        } while (password.isEmpty());

        //System.out.println(password + " " + logins.get(username));

        if (logins.containsKey(username) && logins.get(username).equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password.");
        }

        return false;
    }

    private void createAdmin(String username, String password) {
        logins.put(username, password);
        saveLogins();
    }

    private void saveLogins() {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(LOGIN_FILE))) {
            for (HashMap.Entry<String, String> entry : logins.entrySet()) {
                bWriter.write(entry.getKey() + "|" + entry.getValue());
                bWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving logins: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return new String(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
