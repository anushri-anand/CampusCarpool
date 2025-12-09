package services;

import models.Ride;
import dao.RideDAO;
import dao.BookingDAO;
import utils.NotificationCenter;

import java.util.List;

public class BookingService {
    
    private RideDAO rideDAO;
    private BookingDAO bookingDAO;
    
    public BookingService() {
        this.rideDAO = new RideDAO();
        this.bookingDAO = new BookingDAO();
    }

    public List<Ride> getAvailableRides() {
        return rideDAO.getAllActiveRides();
    }

    public boolean bookRide(int passengerId, int rideId, int seatsRequested) {
        Ride ride = rideDAO.getRideById(rideId);
        if (ride == null) {
            NotificationCenter.showError("Ride not found.");
            return false;
        }

        if (seatsRequested <= 0) {
            NotificationCenter.showError("Must request at least 1 seat.");
            return false;
        }

        if (seatsRequested > ride.getSeatsAvailable()) {
            NotificationCenter.showError("Not enough seats available. Only " + 
                             ride.getSeatsAvailable() + " seat(s) left.");
            return false;
        }

        if (!ride.isActive()) {
            NotificationCenter.showError("Ride is not active.");
            return false;
        }

        if (bookingDAO.hasPassengerBooked(passengerId, rideId)) {
            NotificationCenter.showWarning("You have already booked this ride.");
            return false;
        }

        boolean bookingCreated = bookingDAO.createBooking(passengerId, rideId, seatsRequested);
        
        if (bookingCreated) {
            ride.setSeatsAvailable(ride.getSeatsAvailable() - seatsRequested);
            rideDAO.updateRide(ride);
            
            NotificationCenter.showInfo("Booking successful! " + seatsRequested + " seat(s) booked.");
            return true;
        }

        NotificationCenter.showError("Booking failed. Try again later.");
        return false;
    }

    public boolean bookRide(int passengerId, int rideId) {
        return bookRide(passengerId, rideId, 1);
    }

    public boolean cancelBooking(int bookingId, int passengerId) {
        Integer rideId = bookingDAO.getRideIdByBookingId(bookingId);
        Integer bookedPassengerId = bookingDAO.getPassengerIdByBookingId(bookingId);
        Integer seatsBooked = bookingDAO.getSeatsByBookingId(bookingId);
        
        if (rideId == null || bookedPassengerId == null || seatsBooked == null) {
            System.err.println("Booking not found");
            return false;
        }

        if (bookedPassengerId != passengerId) {
            System.err.println("You can only cancel your own bookings");
            return false;
        }

        Ride ride = rideDAO.getRideById(rideId);
        if (ride != null) {
            ride.setSeatsAvailable(ride.getSeatsAvailable() + seatsBooked);
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

    public boolean confirmBooking(int bookingId, int driverId) {
        Integer rideId = bookingDAO.getRideIdByBookingId(bookingId);
        
        if (rideId == null) {
            System.err.println("Booking not found");
            return false;
        }

        Ride ride = rideDAO.getRideById(rideId);
        if (ride == null || ride.getDriverId() != driverId) {
            NotificationCenter.showError("Only the driver can confirm bookings.");
            return false;
        }

        return bookingDAO.updateBookingStatus(bookingId, "CONFIRMED");
    }

    public int getPassengerBookingCount(int passengerId) {
        return bookingDAO.getBookingCountByPassenger(passengerId);
    }

    public int getRideBookingCount(int rideId) {
        return bookingDAO.getBookingCountByRide(rideId);
    }

    public int getPendingBookingsCountForDriver(int driverId) {
        return bookingDAO.getPendingBookingCountByDriver(driverId);
    }

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

    public boolean hasPassengerBookedRide(int passengerId, int rideId) {
        return bookingDAO.hasPassengerBooked(passengerId, rideId);
    }

    public int getBookingId(int passengerId, int rideId) {
        Integer bookingId = bookingDAO.getBookingId(passengerId, rideId);
        return bookingId != null ? bookingId : -1;
    }

    public List<Ride> getPassengerBookings(int passengerId) {
        RideDAO rideDAO = new RideDAO();
        return rideDAO.getRidesBookedByPassenger(passengerId);
    }
}