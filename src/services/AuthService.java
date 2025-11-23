// AuthService.java
package services;

import models.User;
import models.Driver;
import models.Passenger;
import dao.UserDAO;
import dao.DriverDAO;
import dao.PassengerDAO;

/**
 * AuthService handles user authentication and registration
 */
public class AuthService {
    
    private UserDAO userDAO;
    private DriverDAO driverDAO;
    private PassengerDAO passengerDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
        this.driverDAO = new DriverDAO();
        this.passengerDAO = new PassengerDAO();
    }

    // ========================================
    // AUTHENTICATION
    // ========================================

    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        User user = userDAO.authenticate(email, password);
        if (user == null) {
            System.err.println("Invalid email or password");
            return null;
        }

        if (user.isBlacklisted()) {
            System.err.println("Account is temporarily suspended until: " + user.getBlacklistUntil());
            return null;
        }

        System.out.println("Login successful! Welcome, " + user.getName());
        return user;
    }

    public void logout(User user) {
        if (user != null) {
            System.out.println("User " + user.getName() + " logged out successfully");
        }
    }

    // ========================================
    // REGISTRATION
    // ========================================

    public Passenger registerPassenger(String name, String rollNumber, String email, 
                                       String password, String preferredDestination) {
        if (!validateRegistrationInputs(name, rollNumber, email, password)) return null;

        if (userDAO.getUserByEmail(email) != null) {
            System.err.println("Email already registered");
            return null;
        }
        if (userDAO.getUserByRollNumber(rollNumber) != null) {
            System.err.println("Roll number already registered");
            return null;
        }

        Passenger passenger = new Passenger(name, rollNumber, email, password, preferredDestination);
        boolean success = userDAO.createUser(passenger);
        if (success) {
            passengerDAO.createPassenger(passenger);
            System.out.println("Passenger registered successfully!");
            return passenger;
        }
        return null;
    }

    public Driver registerDriver(String name, String rollNumber, String email, String password,
                                 String licenseNumber, String vehicleModel, 
                                 String vehicleNumber, int seatsAvailable) {
        if (!validateRegistrationInputs(name, rollNumber, email, password)) return null;

        if (licenseNumber == null || licenseNumber.trim().isEmpty() ||
            vehicleModel == null || vehicleModel.trim().isEmpty() ||
            vehicleNumber == null || vehicleNumber.trim().isEmpty() ||
            seatsAvailable <= 0 || seatsAvailable > 8) {
            System.err.println("Driver fields invalid");
            return null;
        }

        if (userDAO.getUserByEmail(email) != null) {
            System.err.println("Email already registered");
            return null;
        }
        if (userDAO.getUserByRollNumber(rollNumber) != null) {
            System.err.println("Roll number already registered");
            return null;
        }

        Driver driver = new Driver(name, rollNumber, email, password, licenseNumber, vehicleModel, vehicleNumber, seatsAvailable);
        boolean success = userDAO.createUser(driver);
        if (success) {
            driverDAO.createDriver(driver);
            System.out.println("Driver registered successfully!");
            return driver;
        }
        return null;
    }

    // ========================================
    // VALIDATION
    // ========================================

    /**
     * Validate common registration inputs
     *
     * - Roll number years restricted to 2018-2025 (pattern 2018A7PS1234U etc).
     * - Email must be f<YYYY><NNNN>@dubai.bits-pilani.ac.in where YYYY is 2018-2025 and NNNN any digits.
     */
    private boolean validateRegistrationInputs(String name, String rollNumber, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Name cannot be empty");
            return false;
        }

        // Roll number: year restricted to 2018-2025
        // Example: 2024A7PS0336U
        if (rollNumber == null || !rollNumber.matches("20(1[8-9]|2[0-5])A7[PT]S[0-9]{4}U")) {
            System.err.println("Invalid roll number format. Example: 2024A7PS0336U (year 2018-2025 allowed)");
            System.err.println("Your input was: [" + rollNumber + "]");
            return false;
        }

        // Email: f + YEAR(2018-2025) + 4 digits + @dubai.bits-pilani.ac.in
        // Example: f20240328@dubai.bits-pilani.ac.in
        if (email == null || !email.matches("f20(1[8-9]|2[0-5])[0-9]{4}@dubai\\.bits-pilani\\.ac\\.in")) {
            System.err.println("Invalid BITS Dubai email format. Example: f20240328@dubai.bits-pilani.ac.in (year 2018-2025 allowed)");
            System.err.println("Your input was: [" + email + "]");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    // ========================================
    // PASSWORD CHANGE / RESET
    // ========================================

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }
        if (!user.getPassword().equals(oldPassword)) {
            System.err.println("Incorrect current password");
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("New password must be at least 6 characters");
            return false;
        }
        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }
        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }
}
