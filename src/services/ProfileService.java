package services;

import models.User;
import models.Driver;
import models.Passenger;
import models.Rating;
import models.Ride;
import dao.UserDAO;
import dao.DriverDAO;
import dao.PassengerDAO;

import java.util.List;

public class ProfileService {

    private UserDAO userDAO;
    private DriverDAO driverDAO;
    private PassengerDAO passengerDAO;

    public ProfileService() {
        this.userDAO = new UserDAO();
        this.driverDAO = new DriverDAO();
        this.passengerDAO = new PassengerDAO();
    }

    public User getUserProfile(int userId) {
        return userDAO.getUserById(userId);
    }

    public Driver getDriverProfile(int userId) {
        return driverDAO.getDriverByUserId(userId);
    }

    public Passenger getPassengerProfile(int userId) {
        return passengerDAO.getPassengerByUserId(userId);
    }

    public boolean updateUserInfo(int userId, String name) {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }

        return userDAO.updateUser(user);
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

    public boolean updatePassword(int userId, String oldPass, String newPass) {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        if (!user.getPassword().equals(oldPass)) {
            return false;
        }
        
        user.setPassword(newPass);
        return userDAO.updateUser(user);
    }
public String getUserStatistics(int userId) {
    User user = userDAO.getUserById(userId);
    if (user == null) {
        return "User not found";
    }

    StringBuilder stats = new StringBuilder();
    stats.append("=== User Statistics ===\n");
    stats.append("ID: ").append(user.getId()).append("\n");
    stats.append("Name: ").append(user.getName()).append("\n");
    stats.append("Email: ").append(user.getEmail()).append("\n");

    BookingService bookingService = new BookingService();

    if (user.getRole().equalsIgnoreCase("PASSENGER") ||
        user.getRole().equalsIgnoreCase("BOTH")) {
    }

    if (user.getRole().equalsIgnoreCase("DRIVER") ||
        user.getRole().equalsIgnoreCase("BOTH")) {

        Driver d = driverDAO.getDriverByUserId(userId);
        if (d != null) {
            stats.append("\n=== Driver Stats ===\n");
            stats.append("Vehicle Model: ").append(d.getVehicleModel()).append("\n");
            stats.append("Vehicle Number: ").append(d.getVehicleNumber()).append("\n");
            stats.append("Seats Available: ").append(d.getSeatsAvailable()).append("\n");

            int ridesGiven = bookingService.getRideBookingCount(d.getId());
            stats.append("Total Rides Given: ").append(ridesGiven).append("\n");

        }
    }

    return stats.toString();
}


    public List<Rating> getUserRatings(int userId) {
        return java.util.Collections.emptyList();
    }

    public List<Ride> getRideHistory(User user) {
        return java.util.Collections.emptyList();
    }

    public boolean updateProfile(User updatedUser) {
        return userDAO.updateUser(updatedUser);
    }
    private BookingService bookingService = new BookingService();

public int getPassengerBookingCount(int userId) {
    return bookingService.getPassengerBookingCount(userId);
}

}
