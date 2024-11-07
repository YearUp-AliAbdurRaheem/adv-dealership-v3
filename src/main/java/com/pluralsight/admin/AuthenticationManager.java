package com.pluralsight.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.pluralsight.utils.Console;

public class AuthenticationManager {
    private static final String LOGIN_FILE = "login.csv";
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
                username = Console.PromptForString("Username: ");
            } while (username.isEmpty());
    
            do {
                password = hashPassword(Console.PromptForPassword("Password: "));
            } while (password.isEmpty());
            createAdmin(username, password);
            return true;
        }

        do {
            username = Console.PromptForString("Username: ");
        } while (username.isEmpty());

        do {
            password = hashPassword(Console.PromptForPassword("Password: "));
        } while (password.isEmpty());


        if (logins.containsKey(username) && logins.get(username).equals(password)) {
            System.out.println("Logged in as " + username + ".");
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
            for (Map.Entry<String, String> entry : logins.entrySet()) {
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
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return new String(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
