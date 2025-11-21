package services;

import models.Ride;
import models.RideRequest;
import models.Driver;
import models.Passenger;
import dao.RideDAO;
import dao.RideRequestDAO;
import dao.UserDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RideService handles business logic for rides and ride requests.
 * Demonstrates: Service Layer Pattern, Business Logic Separation
 */
public class RideService {
    
    private RideDAO rideDAO;
    private RideRequestDAO rideRequestDAO;
    private UserDAO userDAO;

    // Constructor with dependency injection
    public RideService() {
        this.rideDAO = new RideDAO();
        this.rideRequestDAO = new RideRequestDAO();
        this.userDAO = new UserDAO();
    }

    // Constructor for testing (allows mock DAOs)
    public RideService(RideDAO rideDAO, RideRequestDAO rideRequestDAO, UserDAO userDAO) {
        this.rideDAO = rideDAO;
        this.rideRequestDAO = rideRequestDAO;
        this.userDAO = userDAO;
    }

    // ========================================
    // RIDE POSTING (Driver Actions)
    // ========================================

    /**
     * Driver posts a new ride
     * @param driver the driver posting the ride
     * @param origin starting location
     * @param destination ending location
     * @param departureDate date of departure
     * @param departureTime time of departure
     * @param seatsAvailable number of seats offered
     * @param pricePerSeat price per seat
     * @return the created Ride object, or null if failed
     */
    public Ride postRide(Driver driver, String origin, String destination,
                         LocalDate departureDate, LocalTime departureTime,
                         int seatsAvailable, double pricePerSeat) {
        
        // Validate driver can post ride
        if (!driver.canPostRide()) {
            System.err.println("Driver cannot post ride (blacklisted or invalid vehicle info)");
            return null;
        }

        // Validate inputs
        if (origin == null || origin.trim().isEmpty()) {
            System.err.println("Origin cannot be empty");
            return null;
        }
        if (destination == null || destination.trim().isEmpty()) {
            System.err.println("Destination cannot be empty");
            return null;
        }
        if (origin.equalsIgnoreCase(destination)) {
            System.err.println("Origin and destination cannot be the same");
            return null;
        }
        if (departureDate.isBefore(LocalDate.now())) {
            System.err.println("Departure date cannot be in the past");
            return null;
        }
        if (seatsAvailable <= 0 || seatsAvailable > driver.getSeatsAvailable()) {
            System.err.println("Invalid number of seats");
            return null;
        }
        if (pricePerSeat < 0) {
            System.err.println("Price cannot be negative");
            return null;
        }

        // Create vehicle info string
        String vehicleInfo = driver.getVehicleModel() + " - " + driver.getVehicleNumber();

        // Create new ride
        Ride ride = new Ride(
            driver.getId(),
            driver.getName(),
            origin,
            destination,
            departureDate,
            departureTime,
            seatsAvailable,
            pricePerSeat,
            vehicleInfo
        );

        // Save to database
        boolean success = rideDAO.createRide(ride);
        if (success) {
            // Add ride ID to driver's posted rides
            driver.addPostedRide(ride.getId());
            
            // Check for matching ride requests
            checkAndNotifyMatchingRequests(ride);
            
            return ride;
        }

        return null;
    }

    /**
     * Driver cancels their posted ride
     * @param rideId the ID of the ride to cancel
     * @param driverId the ID of the driver cancelling
     * @return true if successful, false otherwise
     */
    public boolean cancelRide(int rideId, int driverId) {
        Ride ride = rideDAO.getRideById(rideId);
        
        if (ride == null) {
            System.err.println("Ride not found");
            return false;
        }

        if (ride.getDriverId() != driverId) {
            System.err.println("Only the driver who posted can cancel the ride");
            return false;
        }

        if (!ride.isActive()) {
            System.err.println("Ride is not active");
            return false;
        }

        // Cancel the ride
        ride.cancelRide();
        boolean success = rideDAO.updateRide(ride);

        if (success) {
            // Notify all booked passengers
            notifyPassengersOfCancellation(ride);
        }

        return success;
    }

    // ========================================
    // RIDE BOOKING (Passenger Actions)
    // ========================================

    /**
     * Passenger books a seat on a ride
     * @param passenger the passenger booking
     * @param rideId the ride to book
     * @return true if booking successful, false otherwise
     */
    public boolean bookRide(Passenger passenger, int rideId) {
        
        // Validate passenger can book
        if (!passenger.canBookRide()) {
            System.err.println("Passenger cannot book ride (blacklisted)");
            return false;
        }

        // Get the ride
        Ride ride = rideDAO.getRideById(rideId);
        if (ride == null) {
            System.err.println("Ride not found");
            return false;
        }

        // Check if ride is active and has seats
        if (!ride.hasAvailableSeats()) {
            System.err.println("No seats available on this ride");
            return false;
        }

        // Check if passenger already booked
        if (ride.isPassengerBooked(passenger.getId())) {
            System.err.println("You have already booked this ride");
            return false;
        }

        // Book the seat
        boolean success = ride.bookSeat(passenger.getId());
        if (success) {
            // Update ride in database
            rideDAO.updateRide(ride);
            
            // Add to passenger's booked rides
            passenger.addBookedRide(rideId);
            
            // Create booking record
            createBookingRecord(passenger.getId(), rideId);
            
            return true;
        }

        return false;
    }

    /**
     * Passenger cancels their booking
     * @param passenger the passenger cancelling
     * @param rideId the ride to cancel booking from
     * @return true if successful, false otherwise
     */
    public boolean cancelBooking(Passenger passenger, int rideId) {
        Ride ride = rideDAO.getRideById(rideId);
        
        if (ride == null) {
            System.err.println("Ride not found");
            return false;
        }

        if (!ride.isPassengerBooked(passenger.getId())) {
            System.err.println("You have not booked this ride");
            return false;
        }

        // Cancel the booking
        boolean success = ride.cancelBooking(passenger.getId());
        if (success) {
            // Update ride in database
            rideDAO.updateRide(ride);
            
            // Remove from passenger's booked rides
            passenger.removeBookedRide(rideId);
            
            // Update booking record status
            updateBookingStatus(passenger.getId(), rideId, "CANCELLED");
            
            return true;
        }

        return false;
    }

    // ========================================
    // RIDE REQUEST POSTING (Passenger Actions)
    // ========================================

    /**
     * Passenger posts a ride request
     * @param passenger the passenger requesting
     * @param origin starting location
     * @param destination ending location
     * @param preferredDate preferred date
     * @param preferredTime preferred time
     * @param seatsRequested number of seats needed
     * @param notes optional notes
     * @return the created RideRequest, or null if failed
     */
    public RideRequest postRideRequest(Passenger passenger, String origin, String destination,
                                       LocalDate preferredDate, LocalTime preferredTime,
                                       int seatsRequested, String notes) {
        
        // Validate passenger can post request
        if (!passenger.canPostRideRequest()) {
            System.err.println("Passenger cannot post ride request (blacklisted)");
            return null;
        }

        // Validate inputs
        if (origin == null || origin.trim().isEmpty()) {
            System.err.println("Origin cannot be empty");
            return null;
        }
        if (destination == null || destination.trim().isEmpty()) {
            System.err.println("Destination cannot be empty");
            return null;
        }
        if (origin.equalsIgnoreCase(destination)) {
            System.err.println("Origin and destination cannot be the same");
            return null;
        }
        if (preferredDate.isBefore(LocalDate.now())) {
            System.err.println("Date cannot be in the past");
            return null;
        }
        if (seatsRequested <= 0) {
            System.err.println("Must request at least 1 seat");
            return null;
        }

        // Create ride request
        RideRequest request = new RideRequest(
            passenger.getId(),
            passenger.getName(),
            origin,
            destination,
            preferredDate,
            preferredTime,
            seatsRequested,
            notes
        );

        // Save to database
        boolean success = rideRequestDAO.createRideRequest(request);
        if (success) {
            // Add to passenger's ride requests
            passenger.addRideRequest(request.getId());
            
            // Check for matching rides
            checkAndNotifyMatchingRides(request);
            
            return request;
        }

        return null;
    }

    /**
     * Passenger cancels their ride request
     * @param requestId the request to cancel
     * @param passengerId the passenger ID
     * @return true if successful, false otherwise
     */
    public boolean cancelRideRequest(int requestId, int passengerId) {
        RideRequest request = rideRequestDAO.getRideRequestById(requestId);
        
        if (request == null) {
            System.err.println("Ride request not found");
            return false;
        }

        if (request.getPassengerId() != passengerId) {
            System.err.println("Only the passenger who posted can cancel the request");
            return false;
        }

        if (!request.isActive()) {
            System.err.println("Request is not active");
            return false;
        }

        // Cancel the request
        request.cancelRequest();
        return rideRequestDAO.updateRideRequest(request);
    }

    // ========================================
    // MATCHING LOGIC
    // ========================================

    /**
     * Find all ride requests that match a specific ride
     * @param ride the ride to match against
     * @return list of matching ride requests
     */
    public List<RideRequest> findMatchingRequestsForRide(Ride ride) {
        List<RideRequest> allRequests = rideRequestDAO.getAllPendingRequests();
        List<RideRequest> matches = new ArrayList<>();

        for (RideRequest request : allRequests) {
            if (ride.canFulfillRequest(request)) {
                matches.add(request);
            }
        }

        return matches;
    }

    /**
     * Find all rides that match a specific ride request
     * @param request the ride request to match against
     * @return list of matching rides
     */
    public List<Ride> findMatchingRidesForRequest(RideRequest request) {
        List<Ride> allRides = rideDAO.getAllActiveRides();
        List<Ride> matches = new ArrayList<>();

        for (Ride ride : allRides) {
            if (request.canBeFulfilledBy(ride)) {
                matches.add(ride);
            }
        }

        return matches;
    }

    // ========================================
    // SEARCH & FILTER
    // ========================================

    /**
     * Search rides by destination
     * @param destination the destination to search for
     * @return list of rides to that destination
     */
    public List<Ride> searchRidesByDestination(String destination) {
        return rideDAO.getRidesByDestination(destination);
    }

    /**
     * Search rides by origin and destination
     * @param origin starting location
     * @param destination ending location
     * @return list of matching rides
     */
    public List<Ride> searchRidesByRoute(String origin, String destination) {
        return rideDAO.getRidesByRoute(origin, destination);
    }

    /**
     * Search rides by date
     * @param date the departure date
     * @return list of rides on that date
     */
    public List<Ride> searchRidesByDate(LocalDate date) {
        return rideDAO.getRidesByDate(date);
    }

    /**
     * Get all active rides
     * @return list of all active rides
     */
    public List<Ride> getAllActiveRides() {
        return rideDAO.getAllActiveRides();
    }

    /**
     * Get rides posted by a specific driver
     * @param driverId the driver's ID
     * @return list of rides posted by this driver
     */
    public List<Ride> getRidesByDriver(int driverId) {
        return rideDAO.getRidesByDriver(driverId);
    }

    /**
     * Get rides booked by a specific passenger
     * @param passengerId the passenger's ID
     * @return list of rides booked by this passenger
     */
    public List<Ride> getRidesBookedByPassenger(int passengerId) {
        return rideDAO.getRidesBookedByPassenger(passengerId);
    }

    /**
     * Get all pending ride requests
     * @return list of all pending requests
     */
    public List<RideRequest> getAllPendingRequests() {
        return rideRequestDAO.getAllPendingRequests();
    }

    /**
     * Get ride requests posted by a specific passenger
     * @param passengerId the passenger's ID
     * @return list of requests posted by this passenger
     */
    public List<RideRequest> getRequestsByPassenger(int passengerId) {
        return rideRequestDAO.getRequestsByPassenger(passengerId);
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    /**
     * Check for matching ride requests when a new ride is posted
     * Notifies passengers who have matching requests
     */
    private void checkAndNotifyMatchingRequests(Ride ride) {
        List<RideRequest> matches = findMatchingRequestsForRide(ride);
        
        if (!matches.isEmpty()) {
            System.out.println("Found " + matches.size() + " matching ride request(s) for this ride!");
            for (RideRequest request : matches) {
                System.out.println("- " + request.getPassengerName() + " needs a ride from " + 
                                 request.getRouteDescription() + " on " + 
                                 request.getFormattedRequestDateTime());
            }
            // TODO: Implement actual notification system (email, in-app, etc.)
        }
    }

    /**
     * Check for matching rides when a new ride request is posted
     * Notifies passenger of available rides
     */
    private void checkAndNotifyMatchingRides(RideRequest request) {
        List<Ride> matches = findMatchingRidesForRequest(request);
        
        if (!matches.isEmpty()) {
            System.out.println("Found " + matches.size() + " matching ride(s) for your request!");
            for (Ride ride : matches) {
                System.out.println("- " + ride.getDriverName() + " is going from " + 
                                 ride.getRouteDescription() + " on " + 
                                 ride.getFormattedDepartureDateTime() + 
                                 " (" + ride.getSeatsAvailable() + " seats available)");
            }
            // TODO: Implement actual notification system
        }
    }

    /**
     * Notify all booked passengers when a ride is cancelled
     */
    private void notifyPassengersOfCancellation(Ride ride) {
        List<Integer> passengerIds = ride.getBookedPassengerIds();
        
        for (int passengerId : passengerIds) {
            System.out.println("Notifying passenger " + passengerId + 
                             " that ride " + ride.getId() + " has been cancelled");
            // TODO: Implement actual notification system
        }
    }

    /**
     * Create a booking record in the database
     */
    private void createBookingRecord(int passengerId, int rideId) {
        // TODO: Implement with BookingDAO when created
        System.out.println("Creating booking record for passenger " + passengerId + 
                         " on ride " + rideId);
    }

    /**
     * Update booking status in the database
     */
    private void updateBookingStatus(int passengerId, int rideId, String status) {
        // TODO: Implement with BookingDAO when created
        System.out.println("Updating booking status to " + status + 
                         " for passenger " + passengerId + " on ride " + rideId);
    }

    /**
     * Mark a ride as completed
     */
    public boolean completeRide(int rideId, int driverId) {
        Ride ride = rideDAO.getRideById(rideId);
        
        if (ride == null || ride.getDriverId() != driverId) {
            return false;
        }

        ride.markAsCompleted();
        return rideDAO.updateRide(ride);
    }
}

