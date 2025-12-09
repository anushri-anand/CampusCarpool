package services;

import models.Ride;
import models.RideRequest;
import models.Driver;
import models.Passenger;
import dao.BookingDAO;
import dao.RideDAO;
import dao.RideRequestDAO;
import dao.UserDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RideService {
    
    private RideDAO rideDAO;

    public RideService() {
        this.rideDAO = new RideDAO();
    }

    public RideService(RideDAO rideDAO, RideRequestDAO rideRequestDAO, UserDAO userDAO) {
        this.rideDAO = rideDAO;
    }

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

        ride.cancelRide();
        boolean success = rideDAO.updateRide(ride);

        if (success) {
            notifyPassengersOfCancellation(ride);
        }

        return success;
    }

    public List<Ride> getAllActiveRides() {
        return rideDAO.getAllActiveRides();
    }

    public List<Ride> getRidesByDriver(int driverId) {
        return rideDAO.getRidesByDriver(driverId);
    }

    public List<Ride> getRidesBookedByPassenger(int passengerId) {
        List<Ride> rides = new ArrayList<>();
        BookingDAO bookingDAO = new BookingDAO();
        RideDAO rideDAO = new RideDAO();

        List<Integer> rideIds = bookingDAO.getRideIdsByPassenger(passengerId);
        for (Integer rideId : rideIds) {
            Ride ride = rideDAO.getRideById(rideId);
            if (ride != null && "ACTIVE".equals(ride.getStatus())) {
                rides.add(ride);
            }
        }

        return rides;
    }

    private void notifyPassengersOfCancellation(Ride ride) {
        System.out.println("Passengers have been notified that the ride from "
                            + ride.getPickupLocation() + " to "
                                + ride.getDropoffLocation() + " has been cancelled.");
    }

    public List<Ride> searchRidesByDestination(String destination) {
        return rideDAO.getRidesByDestination(destination);
    }

    public List<Ride> searchRidesByRoute(String origin, String destination) {
        return rideDAO.getRidesByRoute(origin, destination);
    }

    public List<Ride> searchRidesByDate(LocalDate date) {
        return rideDAO.getRidesByDate(date);
    }

    public Ride postRide(Driver driver, String origin, String destination,
                     LocalDate departureDate, LocalTime departureTime,
                     int seatsAvailable, double pricePerSeat) {

        String vehicleInfo = driver.getVehicleModel() + " (" + driver.getVehicleNumber() + ")";

        Ride ride = new Ride(
                0,
                driver.getId(),
                driver.getName(),
                origin,
                destination,
                departureDate,
                departureTime,
                seatsAvailable,
                seatsAvailable,
                pricePerSeat,
                "ACTIVE",
                vehicleInfo
        );

        boolean success = rideDAO.createRide(ride);

        return success ? ride : null;
    }

    public boolean completeRide(int rideId, int driverId) {
        Ride ride = rideDAO.getRideById(rideId);
        if (ride != null && ride.getDriverId() == driverId) {
            ride.markAsCompleted();
            return rideDAO.updateRide(ride);
        }
        return false;
    }

    public RideRequest postRideRequest(Passenger passenger, String origin, String destination,
                                    LocalDate preferredDate, LocalTime preferredTime,
                                    int seatsRequested, String notes) {
        return null;
    }

    public boolean cancelRideRequest(int requestId, int passengerId) {
        return false;
    }

    public List<RideRequest> getAllPendingRequests() {
        return new ArrayList<>();
    }

    public List<RideRequest> getRequestsByPassenger(int passengerId) {
        return new ArrayList<>();
    }

    public List<Ride> findMatchingRidesForRequest(RideRequest request) {
        return new ArrayList<>();
    }

    public List<RideRequest> findMatchingRequestsForRide(Ride ride) {
        return new ArrayList<>();
    }

}
