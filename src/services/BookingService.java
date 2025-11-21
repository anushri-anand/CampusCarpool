package services;

import models.Ride;
import models.Passenger;
import dao.RideDAO;
import dao.BookingDAO;

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
            System.err.println("Ride not found");
            return false;
        }

        // Validate seats requested
        if (seatsRequested <= 0) {
            System.err.println("Must request at least 1 seat");
            return false;
        }

        if (seatsRequested > ride.getSeatsAvailable()) {
            System.err.println("Not enough seats available. Only " + 
                             ride.getSeatsAvailable() + " seat(s) left");
            return false;
        }

        // Check if ride is active
        if (!ride.isActive()) {
            System.err.println("Ride is not active");
            return false;
        }

        // Check if passenger already booked
        if (bookingDAO.hasPassengerBooked(passengerId, rideId)) {
            System.err.println("You have already booked this ride");
            return false;
        }

        // Create booking
        boolean bookingCreated = bookingDAO.createBooking(passengerId, rideId, seatsRequested);
        
        if (bookingCreated) {
            // Update ride seats
            ride.setSeatsAvailable(ride.getSeatsAvailable() - seatsRequested);
            rideDAO.updateRide(ride);
            
            System.out.println("Booking successful! " + seatsRequested + " seat(s) booked.");
            return true;
        }

        return false;
    }

    /**
     * Cancel a booking
     * @param bookingId the booking to cancel
     * @param passengerId the passenger cancelling
     * @return true if successful, false otherwise
     */
    public boolean cancelBooking(int bookingId, int passengerId) {
        // Get booking details
        Integer rideId = bookingDAO.getRideIdByBookingId(bookingId);
        Integer bookedPassengerId = bookingDAO.getPassengerIdByBookingId(bookingId);
        Integer seatsBooked = bookingDAO.getSeatsByBookingId(bookingId);
        
        if (rideId == null || bookedPassengerId == null || seatsBooked == null) {
            System.err.println("Booking not found");
            return false;
        }

        // Verify passenger owns this booking
        if (bookedPassengerId != passengerId) {
            System.err.println("You can only cancel your own bookings");
            return false;
        }

        // Get associated ride
        Ride ride = rideDAO.getRideById(rideId);
        if (ride != null) {
            // Restore seats to ride
            ride.setSeatsAvailable(ride.getSeatsAvailable() + seatsBooked);
            rideDAO.updateRide(ride);
        }

        // Cancel booking
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            System.out.println("Booking cancelled successfully. Seats returned to ride.");
        }
        
        return success;
    }

    /**
     * Confirm a booking (driver accepts)
     * @param bookingId the booking to confirm
     * @param driverId the driver confirming
     * @return true if successful, false otherwise
     */
    public boolean confirmBooking(int bookingId, int driverId) {
        Integer rideId = bookingDAO.getRideIdByBookingId(bookingId);
        
        if (rideId == null) {
            System.err.println("Booking not found");
            return false;
        }

        // Verify driver owns this ride
        Ride ride = rideDAO.getRideById(rideId);
        if (ride == null || ride.getDriverId() != driverId) {
            System.err.println("Only the driver can confirm bookings");
            return false;
        }

        // Confirm booking
        return bookingDAO.updateBookingStatus(bookingId, "CONFIRMED");
    }

    /**
     * Get all bookings for a passenger
     * @param passengerId the passenger ID
     * @return count of bookings
     */
    public int getPassengerBookingCount(int passengerId) {
        return bookingDAO.getBookingCountByPassenger(passengerId);
    }

    /**
     * Get all bookings for a ride (driver view)
     * @param rideId the ride ID
     * @return count of bookings
     */
    public int getRideBookingCount(int rideId) {
        return bookingDAO.getBookingCountByRide(rideId);
    }

    /**
     * Get pending booking requests for driver's rides
     * @param driverId the driver ID
     * @return count of pending bookings
     */
    public int getPendingBookingsCountForDriver(int driverId) {
        return bookingDAO.getPendingBookingCountByDriver(driverId);
    }

    /**
     * Get booking details as string
     * @param bookingId the booking ID
     * @return formatted booking details
     */
    public String getBookingDetails(int bookingId) {
        Integer rideId = bookingDAO.getRideIdByBookingId(bookingId);
        Integer passengerId = bookingDAO.getPassengerIdByBookingId(bookingId);
        Integer seatsBooked = bookingDAO.getSeatsByBookingId(bookingId);
        String status = bookingDAO.getStatusByBookingId(bookingId);
        
        if (rideId == null) {
            return "Booking not found";
        }
        
        return String.format("Booking #%d: Passenger %d booked %d seat(s) on Ride #%d - Status: %s",
                           bookingId, passengerId, seatsBooked, rideId, status);
    }

    /**
     * Check if a passenger has booked a specific ride
     * @param passengerId passenger ID
     * @param rideId ride ID
     * @return true if booked, false otherwise
     */
    public boolean hasPassengerBookedRide(int passengerId, int rideId) {
        return bookingDAO.hasPassengerBooked(passengerId, rideId);
    }

    /**
     * Get booking ID for a passenger on a specific ride
     * @param passengerId passenger ID
     * @param rideId ride ID
     * @return booking ID, or -1 if not found
     */
    public int getBookingId(int passengerId, int rideId) {
        Integer bookingId = bookingDAO.getBookingId(passengerId, rideId);
        return bookingId != null ? bookingId : -1;
    }
}

