package controllers;

import models.*;
import services.*;
import dao.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * RideController manages ride-related operations between GUI and services
 * Demonstrates: MVC Pattern, Controller Layer, Business Logic Delegation
 */
public class RideController {
    
    private RideService rideService;
    private BookingService bookingService;
    private AuthService authService;
    private ProfileService profileService;
    private ReportService reportService;
    
    private User currentUser; // Currently logged-in user

    // Constructor
    public RideController() {
        this.rideService = new RideService();
        this.bookingService = new BookingService();
        this.authService = new AuthService();
        this.profileService = new ProfileService();
        this.reportService = new ReportService();
    }

    // ========================================
    // USER AUTHENTICATION
    // ========================================

    /**
     * Login user
     * @param email user email
     * @param password user password
     * @return true if successful, false otherwise
     */
    public boolean login(String email, String password) {
        User user = authService.login(email, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            authService.logout(currentUser);
            currentUser = null;
        }
    }

    /**
     * Register a new passenger
     */
    public boolean registerPassenger(String name, String rollNumber, String email, 
                                    String password, String preferredDestination) {
        Passenger passenger = authService.registerPassenger(name, rollNumber, email, 
                                                           password, preferredDestination);
        return passenger != null;
    }

    /**
     * Register a new driver
     */
    public boolean registerDriver(String name, String rollNumber, String email, String password,
                                  String licenseNumber, String vehicleModel, 
                                  String vehicleNumber, int seatsAvailable) {
        Driver driver = authService.registerDriver(name, rollNumber, email, password,
                                                   licenseNumber, vehicleModel, 
                                                   vehicleNumber, seatsAvailable);
        return driver != null;
    }

    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get current user's role
     */
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    // ========================================
    // RIDE POSTING (Driver Actions)
    // ========================================

    /**
     * Driver posts a new ride
     */
    public boolean postRide(String origin, String destination, LocalDate departureDate, 
                           LocalTime departureTime, int seatsAvailable, double pricePerSeat) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to post a ride");
            return false;
        }

        // Get driver object
        Driver driver = profileService.getDriverProfile(currentUser.getId());
        if (driver == null) {
            System.err.println("Only drivers can post rides");
            return false;
        }

        Ride ride = rideService.postRide(driver, origin, destination, departureDate, 
                                        departureTime, seatsAvailable, pricePerSeat);
        return ride != null;
    }

    /**
     * Driver cancels their ride
     */
    public boolean cancelRide(int rideId) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in");
            return false;
        }

        return rideService.cancelRide(rideId, currentUser.getId());
    }

    /**
     * Get rides posted by current driver
     */
    public List<Ride> getMyPostedRides() {
        if (!isLoggedIn()) {
            return null;
        }

        return rideService.getRidesByDriver(currentUser.getId());
    }

    /**
     * Mark ride as completed
     */
    public boolean completeRide(int rideId) {
        if (!isLoggedIn()) {
            return false;
        }

        return rideService.completeRide(rideId, currentUser.getId());
    }

    // ========================================
    // RIDE SEARCHING (Passenger Actions)
    // ========================================

    /**
     * Search all active rides
     */
    public List<Ride> searchAllRides() {
        return rideService.getAllActiveRides();
    }

    /**
     * Search rides by destination
     */
    public List<Ride> searchRidesByDestination(String destination) {
        return rideService.searchRidesByDestination(destination);
    }

    /**
     * Search rides by origin and destination
     */
    public List<Ride> searchRidesByRoute(String origin, String destination) {
        return rideService.searchRidesByRoute(origin, destination);
    }

    /**
     * Search rides by date
     */
    public List<Ride> searchRidesByDate(LocalDate date) {
        return rideService.searchRidesByDate(date);
    }

    /**
     * Get rides booked by current passenger
     */
    public List<Ride> getMyBookedRides() {
        if (!isLoggedIn()) {
            return null;
        }

        return rideService.getRidesBookedByPassenger(currentUser.getId());
    }

    // ========================================
    // RIDE BOOKING (Passenger Actions)
    // ========================================

    /**
     * Passenger books a ride
     */
    public boolean bookRide(int rideId, int seatsRequested) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to book a ride");
            return false;
        }

        return bookingService.bookRide(currentUser.getId(), rideId, seatsRequested);
    }

    /**
     * Cancel a booking
     */
    public boolean cancelBooking(int bookingId) {
        if (!isLoggedIn()) {
            return false;
        }

        return bookingService.cancelBooking(bookingId, currentUser.getId());
    }

    /**
     * Check if current passenger has booked a specific ride
     */
    public boolean hasBookedRide(int rideId) {
        if (!isLoggedIn()) {
            return false;
        }

        return bookingService.hasPassengerBookedRide(currentUser.getId(), rideId);
    }

    /**
     * Get booking count for current user
     */
    public int getMyBookingCount() {
        if (!isLoggedIn()) {
            return 0;
        }

        return bookingService.getPassengerBookingCount(currentUser.getId());
    }

    // ========================================
    // RIDE REQUEST (Passenger Actions)
    // ========================================

    /**
     * Passenger posts a ride request
     */
    public boolean postRideRequest(String origin, String destination, LocalDate preferredDate, 
                                   LocalTime preferredTime, int seatsRequested, String notes) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to post a ride request");
            return false;
        }

        // Get passenger object
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

    /**
     * Cancel a ride request
     */
    public boolean cancelRideRequest(int requestId) {
        if (!isLoggedIn()) {
            return false;
        }

        return rideService.cancelRideRequest(requestId, currentUser.getId());
    }

    /**
     * Get all pending ride requests
     */
    public List<RideRequest> getAllPendingRequests() {
        return rideService.getAllPendingRequests();
    }

    /**
     * Get ride requests posted by current passenger
     */
    public List<RideRequest> getMyRideRequests() {
        if (!isLoggedIn()) {
            return null;
        }

        return rideService.getRequestsByPassenger(currentUser.getId());
    }

    /**
     * Find matching rides for a specific request
     */
    public List<Ride> findMatchingRidesForRequest(RideRequest request) {
        return rideService.findMatchingRidesForRequest(request);
    }

    /**
     * Find matching requests for a specific ride (driver view)
     */
    public List<RideRequest> findMatchingRequestsForRide(Ride ride) {
        return rideService.findMatchingRequestsForRide(ride);
    }

    // ========================================
    // PROFILE MANAGEMENT
    // ========================================

    /**
     * Get user profile
     */
    public User getUserProfile() {
        if (!isLoggedIn()) {
            return null;
        }

        return profileService.getUserProfile(currentUser.getId());
    }

    /**
     * Update user name
     */
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

    /**
     * Update driver vehicle info
     */
    public boolean updateDriverVehicle(String vehicleModel, String vehicleNumber, int seatsAvailable) {
        if (!isLoggedIn()) {
            return false;
        }

        return profileService.updateDriverInfo(currentUser.getId(), vehicleModel, 
                                              vehicleNumber, seatsAvailable);
    }

    /**
     * Update passenger preferred destination
     */
    public boolean updatePassengerDestination(String preferredDestination) {
        if (!isLoggedIn()) {
            return false;
        }

        return profileService.updatePassengerInfo(currentUser.getId(), preferredDestination);
    }

    /**
     * Get user statistics
     */
    public String getUserStatistics() {
        if (!isLoggedIn()) {
            return "Not logged in";
        }

        return profileService.getUserStatistics(currentUser.getId());
    }

    /**
     * Get rating details
     */
    public String getRatingDetails() {
        if (!isLoggedIn()) {
            return "Not logged in";
        }

        return profileService.getRatingDetails(currentUser.getId());
    }

    // ========================================
    // REPORTING
    // ========================================

    /**
     * Submit a report against a user
     */
    public boolean reportUser(int reportedUserId, Integer rideId, String reason) {
        if (!isLoggedIn()) {
            System.err.println("Must be logged in to report");
            return false;
        }

        return reportService.submitReport(currentUser.getId(), reportedUserId, rideId, reason);
    }

    /**
     * Get reports against a user
     */
    public List<Report> getReportsForUser(int userId) {
        return reportService.getReportsForUser(userId);
    }

    /**
     * Get report count for a user
     */
    public int getReportCount(int userId) {
        return reportService.getReportCount(userId);
    }

    // ========================================
    // DRIVER SPECIFIC ACTIONS
    // ========================================

    /**
     * Driver confirms a booking
     */
    public boolean confirmBooking(int bookingId) {
        if (!isLoggedIn()) {
            return false;
        }

        return bookingService.confirmBooking(bookingId, currentUser.getId());
    }

    /**
     * Get pending bookings for driver's rides
     */
    public int getPendingBookingsCount() {
        if (!isLoggedIn()) {
            return 0;
        }

        return bookingService.getPendingBookingsCountForDriver(currentUser.getId());
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Change password
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!isLoggedIn()) {
            return false;
        }

        return authService.changePassword(currentUser.getId(), oldPassword, newPassword);
    }

    /**
     * Get ride details
     */
    public Ride getRideById(int rideId) {
        RideDAO rideDAO = new RideDAO();
        return rideDAO.getRideById(rideId);
    }

    /**
     * Get ride request details
     */
    public RideRequest getRideRequestById(int requestId) {
        RideRequestDAO requestDAO = new RideRequestDAO();
        return requestDAO.getRideRequestById(requestId);
    }

    /**
     * Check if current user is blacklisted
     */
    public boolean isCurrentUserBlacklisted() {
        return isLoggedIn() && currentUser.isBlacklisted();
    }

    /**
     * Get blacklist expiry time
     */
    public String getBlacklistExpiry() {
        if (isLoggedIn() && currentUser.isBlacklisted()) {
            return currentUser.getBlacklistUntil().toString();
        }
        return "Not blacklisted";
    }

    /**
     * Get booking ID for passenger and ride
     */
    public int getBookingId(int passengerId, int rideId) {
        return bookingService.getBookingId(passengerId, rideId);
    }

    /**
     * Display current user info
     */
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

    /**
     * Format ride for display
     */
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

    /**
     * Format ride request for display
     */
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
}
