package controllers;

import models.*;
import services.*;
import dao.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RideController {
    private UserDAO userDAO;
    private RideService rideService;
    private BookingService bookingService;
    private AuthService authService;
    private ProfileService profileService;
    private ReportService reportService;
    
    private User currentUser;

    public RideController() {
        userDAO = new UserDAO(); 
        this.rideService = new RideService();
        this.bookingService = new BookingService();
        this.authService = new AuthService();
        this.profileService = new ProfileService();
        this.reportService = new ReportService();
    }

    public boolean login(String email, String password) {
        User user = authService.login(email, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            authService.logout(currentUser);
            currentUser = null;
        }
    }

    public boolean registerPassenger(String name, String rollNumber, String email, 
                                    String password, String preferredDestination) {
        Passenger passenger = authService.registerPassenger(name, rollNumber, email, 
                                                           password, preferredDestination);
        return passenger != null;
    }

    public boolean registerDriver(String name, String rollNumber, String email, String password,
                                  String licenseNumber, String vehicleModel, 
                                  String vehicleNumber, int seatsAvailable) {
        Driver driver = authService.registerDriver(name, rollNumber, email, password,
                                                   licenseNumber, vehicleModel, 
                                                   vehicleNumber, seatsAvailable, "DRIVER");
        return driver != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public boolean postRide(String origin, String destination, LocalDate departureDate, 
                           LocalTime departureTime, int seatsAvailable, double pricePerSeat) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to post a ride");
            return false;
        }

        Driver driver = profileService.getDriverProfile(currentUser.getId());
        if (driver == null) {
            System.err.println("Only drivers can post rides");
            return false;
        }

        Ride ride = rideService.postRide(driver, origin, destination, departureDate, 
                                        departureTime, seatsAvailable, pricePerSeat);
        return ride != null;
    }

    public boolean cancelRide(int rideId) {
        if (!isLoggedIn()) {
            return false;
        }
        return rideService.cancelRide(rideId, currentUser.getId());
    }

    public List<Ride> getMyPostedRides() {
        if (!isLoggedIn()) {
            return null;
        }
        return rideService.getRidesByDriver(currentUser.getId());
    }

    public boolean completeRide(int rideId) {
        if (!isLoggedIn()) {
            return false;
        }
        return rideService.completeRide(rideId, currentUser.getId());
    }

    public List<Ride> searchAllRides() {
        return rideService.getAllActiveRides();
    }

    public List<Ride> searchRidesByDestination(String destination) {
        return rideService.searchRidesByDestination(destination);
    }

    public List<Ride> searchRidesByRoute(String origin, String destination) {
        return rideService.searchRidesByRoute(origin, destination);
    }

    public List<Ride> searchRidesByDate(LocalDate date) {
        return rideService.searchRidesByDate(date);
    }

    public List<Ride> getMyBookedRides() {
        if (!isLoggedIn()) {
            return null;
        }
        return rideService.getRidesBookedByPassenger(currentUser.getId());
    }

    public boolean bookRide(int rideId, int seatsRequested) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to book a ride");
            return false;
        }
        return bookingService.bookRide(currentUser.getId(), rideId, seatsRequested);
    }

    public boolean cancelBooking(int bookingId) {
        if (!isLoggedIn()) {
            return false;
        }
        return bookingService.cancelBooking(bookingId, currentUser.getId());
    }

    public boolean hasBookedRide(int rideId) {
        if (!isLoggedIn()) {
            return false;
        }
        return bookingService.hasPassengerBookedRide(currentUser.getId(), rideId);
    }

    public int getMyBookingCount() {
        if (!isLoggedIn()) {
            return 0;
        }
        return bookingService.getPassengerBookingCount(currentUser.getId());
    }

    public boolean postRideRequest(String origin, String destination, LocalDate preferredDate, 
                                   LocalTime preferredTime, int seatsRequested, String notes) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to post a ride request");
            return false;
        }

        Passenger passenger = profileService.getPassengerProfile(currentUser.getId());
        if (passenger == null) {
            System.err.println("Only passengers can post ride requests");
            return false;
        }

        RideRequest request = rideService.postRideRequest(passenger, origin, destination, 
                                                          preferredDate, preferredTime, 
                                                          seatsRequested, notes);
        return request != null;
    }

    public boolean cancelRideRequest(int requestId) {
        if (!isLoggedIn()) {
            return false;
        }
        return rideService.cancelRideRequest(requestId, currentUser.getId());
    }

    public List<RideRequest> getAllPendingRequests() {
        return rideService.getAllPendingRequests();
    }

    public List<RideRequest> getMyRideRequests() {
        if (!isLoggedIn()) {
            return null;
        }
        return rideService.getRequestsByPassenger(currentUser.getId());
    }

    public List<Ride> findMatchingRidesForRequest(RideRequest request) {
        return rideService.findMatchingRidesForRequest(request);
    }

    public List<RideRequest> findMatchingRequestsForRide(Ride ride) {
        return rideService.findMatchingRequestsForRide(ride);
    }

    public User getUserProfile() {
        if (!isLoggedIn()) {
            return null;
        }
        return profileService.getUserProfile(currentUser.getId());
    }

    public boolean updateUserName(String newName) {
        if (!isLoggedIn()) {
            return false;
        }

        boolean success = profileService.updateUserInfo(currentUser.getId(), newName);
        if (success) {
            currentUser.setName(newName);
        }
        return success;
    }

    public boolean updateDriverVehicle(String vehicleModel, String vehicleNumber, int seatsAvailable) {
        if (!isLoggedIn()) {
            return false;
        }
        return profileService.updateDriverInfo(currentUser.getId(), vehicleModel, 
                                              vehicleNumber, seatsAvailable);
    }

    public boolean updatePassengerDestination(String preferredDestination) {
        if (!isLoggedIn()) {
            return false;
        }
        return profileService.updatePassengerInfo(currentUser.getId(), preferredDestination);
    }

    public String getUserStatistics() {
        if (!isLoggedIn()) {
            return "Not logged in";
        }
        return profileService.getUserStatistics(currentUser.getId());
    }

    public boolean reportUser(int reportedUserId, Integer rideId, String reason) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to report");
            return false;
        }
        return reportService.submitReport(currentUser.getId(), reportedUserId, rideId, reason);
    }

    public List<Report> getReportsForUser(int userId) {
        return reportService.getReportsForUser(userId);
    }

    public int getReportCount(int userId) {
        return reportService.getReportCount(userId);
    }

    public boolean confirmBooking(int bookingId) {
        if (!isLoggedIn()) {
            return false;
        }
        return bookingService.confirmBooking(bookingId, currentUser.getId());
    }

    public int getPendingBookingsCount() {
        if (!isLoggedIn()) {
            return 0;
        }
        return bookingService.getPendingBookingsCountForDriver(currentUser.getId());
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!isLoggedIn()) {
            return false;
        }
        return authService.changePassword(currentUser.getId(), oldPassword, newPassword);
    }

    public Ride getRideById(int rideId) {
        RideDAO rideDAO = new RideDAO();
        return rideDAO.getRideById(rideId);
    }

    public RideRequest getRideRequestById(int requestId) {
        RideRequestDAO requestDAO = new RideRequestDAO();
        return requestDAO.getRideRequestById(requestId);
    }

    public boolean isCurrentUserBlacklisted() {
        return isLoggedIn() && currentUser.isBlacklisted();
    }

    public String getBlacklistExpiry() {
        if (isLoggedIn() && currentUser.isBlacklisted()) {
            return currentUser.getBlacklistUntil().toString();
        }
        return "Not blacklisted";
    }

    public int getBookingId(int passengerId, int rideId) {
        return bookingService.getBookingId(passengerId, rideId);
    }

    public String getCurrentUserInfo() {
        if (!isLoggedIn()) {
            return "Not logged in";
        }

        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(currentUser.getName()).append("\n");
        info.append("Email: ").append(currentUser.getEmail()).append("\n");
        info.append("Roll Number: ").append(currentUser.getRollNumber()).append("\n");
        info.append("Role: ").append(currentUser.getRole()).append("\n");
        info.append("Rating: ").append(String.format("%.2f", currentUser.getRating())).append("\n");
        info.append("Warnings: ").append(currentUser.getWarnings()).append("\n");
        info.append("Blacklisted: ").append(currentUser.isBlacklisted() ? "Yes" : "No").append("\n");

        return info.toString();
    }

    public String formatRideDisplay(Ride ride) {
        return String.format(
            "Ride #%d: %s\n" +
            "Driver: %s\n" +
            "Departure: %s\n" +
            "Seats: %d/%d available\n" +
            "Price: AED %.2f per seat\n" +
            "Status: %s",
            ride.getId(),
            ride.getRouteDescription(),
            ride.getDriverName(),
            ride.getFormattedDepartureDateTime(),
            ride.getSeatsAvailable(),
            ride.getSeatsTotal(),
            ride.getPricePerSeat(),
            ride.getStatus()
        );
    }

    public String formatRideRequestDisplay(RideRequest request) {
        return String.format(
            "Request #%d: %s\n" +
            "Passenger: %s\n" +
            "Preferred: %s\n" +
            "Seats needed: %d\n" +
            "Notes: %s\n" +
            "Status: %s",
            request.getId(),
            request.getRouteDescription(),
            request.getPassengerName(),
            request.getFormattedRequestDateTime(),
            request.getSeatsRequested(),
            request.getNotes() != null ? request.getNotes() : "None",
            request.getStatus()
        );
    }
    public boolean rateDriver(int driverId, double rating) {
    if (!isLoggedIn()) {
        System.err.println("Must be logged in to rate a driver");
        return false;
    }
 
    User user = userDAO.getUserById(driverId);
    if (user == null) {
        System.err.println("Driver not found");
        return false;
    }
    if (!(user instanceof Driver)) {
        System.err.println("User is not a driver");
        return false;
    }
    Driver driver = (Driver) user;

    driver.addRating(rating);

    
    boolean updated = userDAO.updateUser(driver); 
    if (!updated) {
        System.err.println("Failed to save driver rating");
        return false;
    }
    return true;
}

}