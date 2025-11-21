package services;

import models.User;
import models.Driver;
import models.Passenger;
import dao.UserDAO;
import dao.DriverDAO;
import dao.PassengerDAO;

import java.time.LocalDateTime;

/**
 * AuthService handles user authentication and registration
 * Demonstrates: Authentication, Validation, Security
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

    /**
     * Login user with email and password
     * @param email user's email
     * @param password user's password
     * @return User object if successful, null otherwise
     */
    public User login(String email, String password) {
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        // Authenticate user
        User user = userDAO.authenticate(email, password);
        
        if (user == null) {
            System.err.println("Invalid email or password");
            return null;
        }

        // Check if user is blacklisted
        if (user.isBlacklisted()) {
            System.err.println("Account is temporarily suspended until: " + 
                             user.getBlacklistUntil());
            return null;
        }

        System.out.println("Login successful! Welcome, " + user.getName());
        return user;
    }

    /**
     * Logout user (cleanup session)
     * @param user the user logging out
     */
    public void logout(User user) {
        if (user != null) {
            System.out.println("User " + user.getName() + " logged out successfully");
            // TODO: Clear session data, cache, etc.
        }
    }

    // ========================================
    // REGISTRATION
    // ========================================

    /**
     * Register a new user (passenger)
     * @param name full name
     * @param rollNumber BITS roll number
     * @param email BITS email
     * @param password password
     * @param preferredDestination optional preferred destination
     * @return created Passenger object, or null if failed
     */
    public Passenger registerPassenger(String name, String rollNumber, String email, 
                                       String password, String preferredDestination) {
        // Validate inputs
        if (!validateRegistrationInputs(name, rollNumber, email, password)) {
            return null;
        }

        // Check if user already exists
        if (userDAO.getUserByEmail(email) != null) {
            System.err.println("Email already registered");
            return null;
        }
        if (userDAO.getUserByRollNumber(rollNumber) != null) {
            System.err.println("Roll number already registered");
            return null;
        }

        // Create passenger
        Passenger passenger = new Passenger(name, rollNumber, email, password, preferredDestination);
        
        // Save to database
        boolean success = userDAO.createUser(passenger);
        if (success) {
            // Create passenger-specific record
            passengerDAO.createPassenger(passenger);
            System.out.println("Passenger registered successfully!");
            return passenger;
        }

        return null;
    }

    /**
     * Register a new driver
     * @param name full name
     * @param rollNumber BITS roll number
     * @param email BITS email
     * @param password password
     * @param licenseNumber driver's license number
     * @param vehicleModel vehicle model
     * @param vehicleNumber vehicle registration number
     * @param seatsAvailable number of seats available
     * @return created Driver object, or null if failed
     */
    public Driver registerDriver(String name, String rollNumber, String email, String password,
                                 String licenseNumber, String vehicleModel, 
                                 String vehicleNumber, int seatsAvailable) {
        // Validate inputs
        if (!validateRegistrationInputs(name, rollNumber, email, password)) {
            return null;
        }

        // Validate driver-specific inputs
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            System.err.println("License number is required");
            return null;
        }
        if (vehicleModel == null || vehicleModel.trim().isEmpty()) {
            System.err.println("Vehicle model is required");
            return null;
        }
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            System.err.println("Vehicle number is required");
            return null;
        }
        if (seatsAvailable <= 0 || seatsAvailable > 8) {
            System.err.println("Seats available must be between 1 and 8");
            return null;
        }

        // Check if user already exists
        if (userDAO.getUserByEmail(email) != null) {
            System.err.println("Email already registered");
            return null;
        }
        if (userDAO.getUserByRollNumber(rollNumber) != null) {
            System.err.println("Roll number already registered");
            return null;
        }

        // Create driver
        Driver driver = new Driver(name, rollNumber, email, password, 
                                   licenseNumber, vehicleModel, vehicleNumber, seatsAvailable);
        
        // Save to database
        boolean success = userDAO.createUser(driver);
        if (success) {
            // Create driver-specific record
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
     */
    private boolean validateRegistrationInputs(String name, String rollNumber, 
                                               String email, String password) {
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Name cannot be empty");
            return false;
        }

        // Validate roll number format: [1-2][0-9][0-9][0-9]A7[PT]S[0-9][0-9][0-9][0-9]U
        if (rollNumber == null || !rollNumber.matches("[1-2][0-9]{3}A7[PT]S[0-9]{4}U")) {
            System.err.println("Invalid roll number format. Example: 2024A7PS0336U");
            return false;
        }

        // Validate email format: f[1-2][0-9][0-9][0-9]03[0-9][0-9][0-9]@dubai.bits-pilani.ac.in
        if (email == null || !email.matches("f[1-2][0-9]{3}03[0-9]{3}@dubai\\.bits-pilani\\.ac\\.in")) {
            System.err.println("Invalid BITS Dubai email format. Example: f20240328@dubai.bits-pilani.ac.in");
            return false;
        }

        // Validate password strength
        if (password == null || password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    /**
     * Change user password
     * @param userId user ID
     * @param oldPassword current password
     * @param newPassword new password
     * @return true if successful, false otherwise
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // Get user
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        // Verify old password
        if (!user.getPassword().equals(oldPassword)) {
            System.err.println("Incorrect current password");
            return false;
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("New password must be at least 6 characters");
            return false;
        }

        // Update password
        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }

    /**
     * Reset password (admin function or email verification)
     * @param email user's email
     * @param newPassword new password
     * @return true if successful, false otherwise
     */
    public boolean resetPassword(String email, String newPassword) {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }

        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }
}
