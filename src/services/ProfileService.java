package services;

import models.User;
import models.Driver;
import models.Passenger;
import models.Rating;
import models.Ride;
import dao.UserDAO;

import java.util.List;

import dao.DriverDAO;
import dao.PassengerDAO;

/**
 * ProfileService handles all user profile operations.
 * Demonstrates profile management & data update logic.
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

    // ================================
    // PROFILE VIEWING
    // ================================
    
    public User getUserProfile(int userId) {
        return userDAO.getUserById(userId);
    }

    public Driver getDriverProfile(int userId) {
        return driverDAO.getDriverByUserId(userId);
    }

    public Passenger getPassengerProfile(int userId) {
        return passengerDAO.getPassengerByUserId(userId);
    }

    // ================================
    // PROFILE EDIT / UPDATE
    // ================================
    
    public boolean updateUserInfo(int userId, String name) {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }

        return userDAO.updateUser(user);  // must exist in DAO
    }

    public boolean updateDriverInfo(int userId, String vehicleModel,
                                    String vehicleNumber, int seatsAvailable) {

        Driver driver = driverDAO.getDriverByUserId(userId);
        if (driver == null) return false;

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

    public boolean updatePassengerInfo(int userId, String preferredDestination) {
        Passenger passenger = passengerDAO.getPassengerByUserId(userId);
        if (passenger == null) return false;

        passenger.setPreferredDestination(preferredDestination);
        return passengerDAO.updatePassenger(passenger);
    }

    // ================================
    // PASSWORD UPDATE
    // ================================
    
    public boolean updatePassword(int userId, String oldPass, String newPass) {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        if (!user.getPassword().equals(oldPass)) {
            return false; // old password doesn't match
        }
        
        user.setPassword(newPass);
        return userDAO.updateUser(user);
    }

    // ================================
    // STATS & RATING DETAILS
    // ================================
    
    public String getUserStatistics(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        StringBuilder stats = new StringBuilder();
        stats.append("=== User Statistics ===\n");
        stats.append("Name: ").append(user.getName()).append("\n");
        stats.append("Rating: ").append(String.format("%.2f", user.getRating()))
             .append(" (").append(user.getTotalRatings()).append(" ratings)\n");
        stats.append("Warnings: ").append(user.getWarnings()).append("\n");
        stats.append("Blacklisted: ").append(user.isBlacklisted() ? "Yes" : "No").append("\n");

        if (user.getRole().equalsIgnoreCase("DRIVER") ||
            user.getRole().equalsIgnoreCase("BOTH")) {
            
            Driver d = driverDAO.getDriverByUserId(userId);
            if (d != null) {
                stats.append("\n=== Driver Stats ===\n");
                stats.append("Vehicle Model: ").append(d.getVehicleModel()).append("\n");
                stats.append("Vehicle Number: ").append(d.getVehicleNumber()).append("\n");
            }
        }

        if (user.getRole().equalsIgnoreCase("PASSENGER") ||
            user.getRole().equalsIgnoreCase("BOTH")) {
            
            Passenger p = passengerDAO.getPassengerByUserId(userId);
            if (p != null) {
                stats.append("\n=== Passenger Stats ===\n");
                stats.append("Preferred Destination: ").append(p.getPreferredDestination()).append("\n");
            }
        }

        return stats.toString();
    }

    public String getRatingDetails(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) return "User not found";

        return String.format(
                "Average Rating: %.2f/5.0 (%d total ratings)",
                user.getRating(), user.getTotalRatings()
        );
    }
    // Missing from original

public List<Rating> getUserRatings(int userId) {
    return java.util.Collections.emptyList();  // Replace with DAO implementation
}

public List<Ride> getRideHistory(User user) {
    return java.util.Collections.emptyList();  // Replace with actual logic
}

public boolean updateProfile(User updatedUser) {
    return userDAO.updateUser(updatedUser);
}

}
