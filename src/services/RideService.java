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
     * Create ride directly (used by GUI)
     * @param ride ride object
     * @return true if created
     */
    public boolean createRide(Ride ride) {
        return rideDAO.createRide(ride);
    }

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
            notifyPassengersOfCancellation(ride);
        }

        return success;
    }

    // ========================================
    // RIDE SEARCH & DISPLAY
    // ========================================

    public List<Ride> getAllActiveRides() {
        return rideDAO.getAllActiveRides();
    }

    public List<Ride> getRidesByDriver(int driverId) {
        return rideDAO.getRidesByDriver(driverId);
    }

    public List<Ride> getRidesBookedByPassenger(int passengerId) {
        return rideDAO.getRidesBookedByPassenger(passengerId);
    }

    // ... rest of your RideRequest and helper methods unchanged

    /**
    * Notifies passengers when a ride is cancelled.
    * For now, this is just a simple placeholder method used by GUI & logging.
    * Later you can expand it to notify users via email/SMS/notification center.
    */
    private void notifyPassengersOfCancellation(Ride ride) {
        System.out.println("Passengers have been notified that the ride from "
                            + ride.getPickupLocation() + " to "
                                + ride.getDropoffLocation() + " has been cancelled.");
    }

}
