package services;

import models.Booking; 
import models.Ride;
import models.Passenger;
import dao.RideDAO;
import dao.BookingDAO;
import utils.NotificationCenter;

import java.util.List;

/**
 * BookingService handles ride booking operations
 * Demonstrates: Transaction Management, Business Rules
 */
public class BookingService {
    
    private RideDAO rideDAO;
    private BookingDAO bookingDAO;
    
    public BookingService() {
        this.rideDAO = new RideDAO();
        this.bookingDAO = new BookingDAO();
    }

    // ========================================
    // RIDE ACCESS
    // ========================================

    /**
     * Get all ACTIVE rides available to book
     */
    public List<Ride> getAvailableRides() {
        return rideDAO.getAllActiveRides();
    }

    // ========================================
    // BOOKING MANAGEMENT
    // ========================================

    /**
     * Passenger books a ride
     * @param passengerId the passenger booking
     * @param rideId the ride to book
     * @param seatsRequested number of seats to book
     * @return true if successful, false otherwise
     */
    public boolean bookRide(int passengerId, int rideId, int seatsRequested) {
        // Get the ride
        Ride ride = rideDAO.getRideById(rideId);
        if (ride == null) {
            NotificationCenter.showError("Ride not found.");
            return false;
        }

        // Validate seats requested
        if (seatsRequested <= 0) {
            NotificationCenter.showError("Must request at least 1 seat.");
            return false;
        }

        if (seatsRequested > ride.getSeatsAvailable()) {
            NotificationCenter.showError("Not enough seats available. Only " + 
                             ride.getSeatsAvailable() + " seat(s) left.");
            return false;
        }

        // Check if ride is active
        if (!ride.isActive()) {
            NotificationCenter.showError("Ride is not active.");
            return false;
        }

        // Check if passenger already booked
        if (bookingDAO.hasPassengerBooked(passengerId, rideId)) {
            NotificationCenter.showWarning("You have already booked this ride.");
            return false;
        }

        // Create booking
        boolean bookingCreated = bookingDAO.createBooking(passengerId, rideId, seatsRequested);
        
        if (bookingCreated) {
            // Update ride seats
            ride.setSeatsAvailable(ride.getSeatsAvailable() - seatsRequested);
            rideDAO.updateRide(ride);
            
            NotificationCenter.showInfo("Booking successful! " + seatsRequested + " seat(s) booked.");
            return true;
        }

        NotificationCenter.showError("Booking failed. Try again later.");
        return false;
    }

    /**
     * Overloaded method for single-seat booking (default for GUI)
     */
    public boolean bookRide(int passengerId, int rideId) {
        return bookRide(passengerId, rideId, 1);
    }

    /**
     * Cancel a booking
     */
    public boolean cancelBooking(int bookingId, int passengerId) {
        var booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            NotificationCenter.showError("Booking not found.");
            return false;
        }

        if (booking.getPassengerId() != passengerId) {
            NotificationCenter.showError("You can only cancel your own bookings.");
            return false;
        }

        Ride ride = rideDAO.getRideById(booking.getRideId());
        if (ride != null) {
            ride.setSeatsAvailable(ride.getSeatsAvailable() + booking.getSeatsBooked());
            rideDAO.updateRide(ride);
        }

        boolean success = bookingDAO.cancelBooking(bookingId);
        if (success) {
            NotificationCenter.showInfo("Booking cancelled successfully. Seats returned to ride.");
        } else {
            NotificationCenter.showError("Failed to cancel booking.");
        }

        return success;
    }

    /**
     * Confirm booking
     */
    public boolean confirmBooking(int bookingId, int driverId) {
        var booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            NotificationCenter.showError("Booking not found.");
            return false;
        }

        Ride ride = rideDAO.getRideById(booking.getRideId());
        if (ride == null || ride.getDriverId() != driverId) {
            NotificationCenter.showError("Only the driver can confirm bookings.");
            return false;
        }

        boolean success = bookingDAO.updateBookingStatus(bookingId, "CONFIRMED");
        if (success) {
            NotificationCenter.showInfo("Booking confirmed successfully.");
        }
        return success;
    }

    public List<Booking> getPassengerBookings(int passengerId) {
        return bookingDAO.getBookingsByPassenger(passengerId);
    }

    public List<Booking> getRideBookings(int rideId) {
        return bookingDAO.getBookingsByRide(rideId);
    }

    public List<Booking> getPendingBookingsForDriver(int driverId) {
        return bookingDAO.getPendingBookingsByDriver(driverId);
    }
}
