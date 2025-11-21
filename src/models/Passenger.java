package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Passenger class extending User.
 * Represents a student who books rides and posts ride requests.
 * Demonstrates: Inheritance, Polymorphism, Encapsulation
 */
public class Passenger extends User {
    // Passenger-specific fields
    private String preferredDestination;
    private List<Integer> bookedRideIds;   // Track rides booked by this passenger
    private List<Integer> rideRequestIds;  // Track ride requests posted by this passenger

    // Constructors

    /**
     * Default constructor
     */
    public Passenger() {
        super();
        this.bookedRideIds = new ArrayList<>();
        this.rideRequestIds = new ArrayList<>();
    }

    /**
     * Constructor with User fields + Passenger-specific fields
     * Constructor Overloading
     */
    public Passenger(String name, String rollNumber, String email, String password,
                     String preferredDestination) {
        super(name, rollNumber, email, password, "PASSENGER");
        this.preferredDestination = preferredDestination;
        this.bookedRideIds = new ArrayList<>();
        this.rideRequestIds = new ArrayList<>();
    }

    /**
     * Full constructor including ID (for database retrieval)
     * Constructor Overloading
     */
    public Passenger(int id, String name, String rollNumber, String email, String password,
                     int warnings, LocalDateTime blacklistUntil, double rating, int totalRatings,
                     String preferredDestination) {
        super(id, name, rollNumber, email, password, "PASSENGER", warnings, blacklistUntil, rating, totalRatings);
        this.preferredDestination = preferredDestination;
        this.bookedRideIds = new ArrayList<>();
        this.rideRequestIds = new ArrayList<>();
    }

    // Getters and Setters

    public String getPreferredDestination() {
        return preferredDestination;
    }

    public void setPreferredDestination(String preferredDestination) {
        this.preferredDestination = preferredDestination;
    }

    public List<Integer> getBookedRideIds() {
        return new ArrayList<>(bookedRideIds); // Return copy for encapsulation
    }

    public void setBookedRideIds(List<Integer> bookedRideIds) {
        this.bookedRideIds = new ArrayList<>(bookedRideIds);
    }

    public List<Integer> getRideRequestIds() {
        return new ArrayList<>(rideRequestIds); // Return copy for encapsulation
    }

    public void setRideRequestIds(List<Integer> rideRequestIds) {
        this.rideRequestIds = new ArrayList<>(rideRequestIds);
    }

    // Passenger-specific Business Logic - Booked Rides

    /**
     * Adds a ride ID to the passenger's booked rides
     * @param rideId the ID of the newly booked ride
     */
    public void addBookedRide(int rideId) {
        if (!bookedRideIds.contains(rideId)) {
            bookedRideIds.add(rideId);
        }
    }

    /**
     * Removes a ride ID from the passenger's booked rides (when cancelled)
     * @param rideId the ID of the ride to remove
     */
    public void removeBookedRide(int rideId) {
        bookedRideIds.remove(Integer.valueOf(rideId));
    }

    /**
     * Checks if passenger has already booked a specific ride
     * @param rideId the ride ID to check
     * @return true if already booked, false otherwise
     */
    public boolean hasBookedRide(int rideId) {
        return bookedRideIds.contains(rideId);
    }

    /**
     * Gets total number of rides booked by this passenger
     * @return count of booked rides
     */
    public int getTotalRidesBooked() {
        return bookedRideIds.size();
    }

    // Passenger-specific Business Logic - Ride Requests

    /**
     * Adds a new ride request ID to the passenger's ride requests
     * @param requestId the ID of the newly posted ride request
     */
    public void addRideRequest(int requestId) {
        if (!rideRequestIds.contains(requestId)) {
            rideRequestIds.add(requestId);
        }
    }

    /**
     * Removes a ride request ID from the passenger's ride requests (when cancelled or matched)
     * @param requestId the ID of the request to remove
     */
    public void removeRideRequest(int requestId) {
        rideRequestIds.remove(Integer.valueOf(requestId));
    }

    /**
     * Gets total number of ride requests posted by this passenger
     * @return count of ride requests
     */
    public int getTotalRideRequests() {
        return rideRequestIds.size();
    }

    // General Business Logic

    /**
     * Checks if passenger can book a ride
     * @return true if passenger is not blacklisted, false otherwise
     */
    public boolean canBookRide() {
        return canPerformAction(); // Inherited from User
    }

    /**
     * Checks if passenger can post a ride request
     * @return true if passenger is not blacklisted, false otherwise
     */
    public boolean canPostRideRequest() {
        return canPerformAction(); // Inherited from User
    }

    /**
     * Checks if passenger has a preferred destination set
     * @return true if preferred destination exists, false otherwise
     */
    public boolean hasPreferredDestination() {
        return preferredDestination != null && !preferredDestination.isEmpty();
    }

    // Method Overriding - Polymorphism

    /**
     * Override getRoleDescription from User class
     * @return passenger-specific role description
     */
    @Override
    public String getRoleDescription() {
        if (hasPreferredDestination()) {
            return "Passenger (Prefers: " + preferredDestination + ")";
        }
        return "Passenger";
    }

    /**
     * Override toString for Passenger-specific information
     */
    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", rollNumber='" + getRollNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", preferredDestination='" + preferredDestination + '\'' +
                ", rating=" + String.format("%.2f", getRating()) +
                ", totalRidesBooked=" + getTotalRidesBooked() +
                ", totalRideRequests=" + getTotalRideRequests() +
                ", blacklisted=" + isBlacklisted() +
                '}';
    }
}
