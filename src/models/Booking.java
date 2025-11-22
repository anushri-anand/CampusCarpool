package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Booking model representing a ride booking made by a passenger
 * Demonstrates: Encapsulation, Business Logic
 */
public class Booking {
    // Booking attributes
    private int id;
    private int rideId;
    private int passengerId;
    private String passengerName; // Cached for display
    private String origin;
    private String destination;
    private int seatsBooked;
    private String status; // "REQUESTED", "CONFIRMED", "CANCELLED"
    private LocalDateTime timestamp;

    // Constructors

    /**
     * Default constructor
     */
    public Booking() {
        this.timestamp = LocalDateTime.now();
        this.status = "REQUESTED";
        this.seatsBooked = 1;
    }

    /**
     * Constructor for creating a new booking
     */
    public Booking(int rideId, int passengerId, String passengerName, 
                   String origin, String destination, int seatsBooked) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.seatsBooked = seatsBooked;
        this.status = "REQUESTED";
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Full constructor (for database retrieval)
     */
    public Booking(int id, int rideId, int passengerId, String passengerName,
                   String origin, String destination, int seatsBooked, 
                   String status, LocalDateTime timestamp) {
        this.id = id;
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.seatsBooked = seatsBooked;
        this.status = status;
        this.timestamp = timestamp;
        
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Business Logic Methods

    /**
     * Check if booking is active (requested or confirmed)
     */
    public boolean isActive() {
        return status.equals("REQUESTED") || status.equals("CONFIRMED");
    }

    /**
     * Check if booking is confirmed
     */
    public boolean isConfirmed() {
        return status.equals("CONFIRMED");
    }

    /**
     * Check if booking is cancelled
     */
    public boolean isCancelled() {
        return status.equals("CANCELLED");
    }

    /**
     * Confirm the booking
     */
    public void confirm() {
        this.status = "CONFIRMED";
    }

    /**
     * Cancel the booking
     */
    public void cancel() {
        this.status = "CANCELLED";
    }

    /**
     * Get route description
     */
    public String getRouteDescription() {
        return origin + " → " + destination;
    }

    /**
     * Get formatted timestamp
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return timestamp.format(formatter);
    }

    /**
     * Get booking summary for display
     */
    public String getSummary() {
        return passengerName + " booked " + seatsBooked + " seat(s) - " + status;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", rideId=" + rideId +
                ", passenger='" + passengerName + "' (ID:" + passengerId + ")" +
                ", route='" + getRouteDescription() + '\'' +
                ", seatsBooked=" + seatsBooked +
                ", status='" + status + '\'' +
                ", timestamp=" + getFormattedTimestamp() +
                '}';
    }

    // Override equals for comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return id == booking.id;
    }

    // Override hashCode for collections
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
