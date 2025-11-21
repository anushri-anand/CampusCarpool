package services;

import models.User;
import models.Driver;
import models.Passenger;
import dao.UserDAO;
import dao.DriverDAO;
import dao.PassengerDAO;

/**
 * ProfileService handles user profile operations
 * Demonstrates: Profile Management, Data Updates
 */
public class ProfileService {
    
    private UserDAO userDAO;
    private DriverDAO driverDAO;
    private PassengerDAO passengerDAO;
    
    public ProfileService() {
        this.userDAO = new UserDAO();
        this.driverDAO = new DriverDAO();
        this.passengerDAO = new PassengerDAO();
    }

    // ========================================
    // PROFILE VIEWING
    // ========================================

    /**
     * Get user profile by ID
     * @param userId the user ID
     * @return User object
     */
    public User getUserProfile(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Get driver profile by user ID
     * @param userId the user ID
     * @return Driver object
     */
    public Driver getDriverProfile(int userId) {
        return driverDAO.getDriverByUserId(userId);
    }

    /**
     * Get passenger profile by user ID
     * @param userId the user ID
     * @return Passenger object
     */
    public Passenger getPassengerProfile(int userId) {
        return passengerDAO.getPassengerByUserId(userId);
    }

    // ========================================
    // PROFILE UPDATES
    // ========================================

    /**
     * Update user basic information
     * @param userId user ID
     * @param name new name (null to keep current)
     * @return true if successful
     */
    public boolean updateUserInfo(int userId, String name) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }

        return userDAO.updateUser(user);
    }

    /**
     * Update driver vehicle information
     * @param userId user ID
     * @param vehicleModel new vehicle model (null to keep current)
     * @param vehicleNumber new vehicle number (null to keep current)
     * @param seatsAvailable new seats count (-1 to keep current)
     * @return true if successful
     */
    public boolean updateDriverInfo(int userId, String vehicleModel, 
                                    String vehicleNumber, int seatsAvailable) {
        Driver driver = driverDAO.getDriverByUserId(userId);
        if (driver == null) {
            System.err.println("Driver not found");
            return false;
        }

        if (vehicleModel != null && !vehicleModel.trim().isEmpty()) {
            driver.setVehicleModel(vehicleModel);
        }
        if (vehicleNumber != null && !vehicleNumber.trim().isEmpty()) {
            driver.setVehicleNumber(vehicleNumber);
        }
        if (seatsAvailable > 0 && seatsAvailable <= 8) {
            driver.setSeatsAvailable(seatsAvailable);
        }

        return driverDAO.updateDriver(driver);
    }

    /**
     * Update passenger preferred destination
     * @param userId user ID
     * @param preferredDestination new preferred destination
     * @return true if successful
     */
    public boolean updatePassengerInfo(int userId, String preferredDestination) {
        Passenger passenger = passengerDAO.getPassengerByUserId(userId);
        if (passenger == null) {
            System.err.println("Passenger not found");
            return false;
        }

        passenger.setPreferredDestination(preferredDestination);
        return passengerDAO.updatePassenger(passenger);
    }

    // ========================================
    // STATISTICS
    // ========================================

    /**
     * Get user statistics
     * @param userId user ID
     * @return formatted statistics string
     */
    public String getUserStatistics(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        StringBuilder stats = new StringBuilder();
        stats.append("=== User Statistics ===\n");
        stats.append("Name: ").append(user.getName()).append("\n");
        stats.append("Rating: ").append(String.format("%.2f", user.getRating()));
        stats.append(" (").append(user.getTotalRatings()).append(" ratings)\n");
        stats.append("Warnings: ").append(user.getWarnings()).append("\n");
        stats.append("Blacklisted: ").append(user.isBlacklisted() ? "Yes" : "No").append("\n");

        if (user.getRole().equals("DRIVER") || user.getRole().equals("BOTH")) {
            Driver driver = driverDAO.getDriverByUserId(userId);
            if (driver != null) {
                stats.append("\n=== Driver Stats ===\n");
                stats.append("Total Rides Posted: ").append(driver.getTotalRidesPosted()).append("\n");
                stats.append("Vehicle: ").append(driver.getVehicleModel()).append("\n");
            }
        }

        if (user.getRole().equals("PASSENGER") || user.getRole().equals("BOTH")) {
            Passenger passenger = passengerDAO.getPassengerByUserId(userId);
            if (passenger != null) {
                stats.append("\n=== Passenger Stats ===\n");
                stats.append("Total Rides Booked: ").append(passenger.getTotalRidesBooked()).append("\n");
                stats.append("Total Requests Posted: ").append(passenger.getTotalRideRequests()).append("\n");
            }
        }

        return stats.toString();
    }

    /**
     * View user's rating breakdown
     * @param userId user ID
     * @return rating details
     */
    public String getRatingDetails(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        return String.format("Average Rating: %.2f/5.0 (%d total ratings)",
                           user.getRating(), user.getTotalRatings());
    }
}
