package com.pluralsight.utils;

import java.util.Scanner;

public class Console {

    static Scanner scanner = new Scanner(System.in);

    public static String PromptForString(String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String PromptForString(){
        return scanner.nextLine();
    }

    public static boolean PromptForYesNo(String prompt){
        System.out.print(prompt + " ( Y for Yes, N for No ) ?");
        String userinput = scanner.nextLine();

        return
                (
                        userinput.equalsIgnoreCase("Y")
                                ||
                        userinput.equalsIgnoreCase("YES")
                ); // Returns false for any invalid input, may cause unexpected behavior

    }

    public static short PromptForShort(String prompt){
        System.out.print(prompt);
        String value = scanner.nextLine();
        short userinput = Short.parseShort(value);
        return  userinput;
    }

    public static int PromptForInt(String prompt){
        System.out.print(prompt);
        String value = scanner.nextLine();
        int userinput = Integer.parseInt(value);
        return userinput;
    }

    public static double PromptForDouble(String prompt){
        System.out.print(prompt);
        String userInputs = scanner.nextLine();
        double userinput = Double.parseDouble(userInputs);
        return userinput;
    }

    public static byte PromptForByte(String prompt){
        System.out.print(prompt);
        String value = scanner.nextLine();
        byte userinput = Byte.parseByte(value);
        return userinput;
    }

    public static byte PromptForByte(){
        String value = scanner.nextLine();
        byte userinput = Byte.parseByte(value);
        return userinput;
    }

    public static float PromptForFloat(String prompt){
        System.out.print(prompt);
        String value = scanner.nextLine();
        float userinput =Float.parseFloat(value);
        return  userinput;
    }

    public static String PromptForPassword(String prompt) { // This lets you hide the password input in the console
        java.io.Console systemConsole = System.console();
        if (systemConsole == null) {
            return PromptForString(prompt);
        }
        System.out.print(prompt);
        char[] passwordChars = systemConsole.readPassword();
        return new String(passwordChars);
    }

}