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
        return new ArrayList<>(bookedRideIds);
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

    // Passenger-specific Business Logic

    public void addBookedRide(int rideId) {
        if (!bookedRideIds.contains(rideId)) {
            bookedRideIds.add(rideId);
        }
    }

    public void removeBookedRide(int rideId) {
        bookedRideIds.remove(Integer.valueOf(rideId));
    }

    /** Adds a new ride request ID */
    public void addRideRequest(int requestId) {
        if (!rideRequestIds.contains(requestId)) {
            rideRequestIds.add(requestId);
        }
    }

    /** Removes a ride request ID */
    public void removeRideRequest(int requestId) {
        rideRequestIds.remove(Integer.valueOf(requestId));
    }

    /** Count ride requests posted */
    public int getTotalRideRequests() {
        return rideRequestIds.size();
    }

    public boolean canBookRide() {
        return canPerformAction();
    }

    public boolean hasBookedRide(int rideId) {
        return bookedRideIds.contains(rideId);
    }

    public int getTotalRidesBooked() {
        return bookedRideIds.size();
    }

    public boolean hasPreferredDestination() {
        return preferredDestination != null && !preferredDestination.isEmpty();
    }

    @Override
    public String getRoleDescription() {
        if (hasPreferredDestination()) {
            return "Passenger (Prefers: " + preferredDestination + ")";
        }
        return "Passenger";
    }

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
