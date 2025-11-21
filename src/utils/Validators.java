package utils;

public class Validators {

    // Example: 21BCE1234 format
    public static boolean isValidRollNumber(String roll) {
        return roll.matches("[0-9]{2}[A-Z]{3}[0-9]{4}");
    }

    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
