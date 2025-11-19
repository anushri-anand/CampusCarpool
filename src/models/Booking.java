package models;

import java.time.LocalDateTime;

/**
 * Booking class representing a passenger's booking request for a ride.
 * Demonstrates: Encapsulation, Composition, Enum, Time API
 */
public class Booking {

    // Booking status enumeration
    public enum Status {
        REQUESTED,
        CONFIRMED,
        CANCELLED
    }

    // Fields
    private int bookingId;
    private Ride ride;               // Association
    private Passenger passenger;     // Association
    private Status status;
    private LocalDateTime timestamp;

    // Constructors

    /** Default constructor */
    public Booking() {
        this.status = Status.REQUESTED;
        this.timestamp = LocalDateTime.now();
    }

    /** Constructor for new booking */
    public Booking(int bookingId, Ride ride, Passenger passenger) {
        this.bookingId = bookingId;
        this.ride = ride;
        this.passenger = passenger;
        this.status = Status.REQUESTED;
        this.timestamp = LocalDateTime.now();
    }

    // Getters & Setters

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Business Logic

    /** Confirms the booking */
    public void confirm() {
        this.status = Status.CONFIRMED;
    }

    /** Cancels the booking */
    public void cancel() {
        this.status = Status.CANCELLED;
    }

    // Overrides

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", rideId=" + (ride != null ? ride.getId() : "N/A") +
                ", passenger='" + (passenger != null ? passenger.getName() : "N/A") + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
