package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private int id;
    private int rideId;
    private int passengerId;
    private String passengerName;
    private String origin;
    private String destination;
    private int seatsBooked;
    private String status;
    private LocalDateTime timestamp;

    public Booking() {
        this.timestamp = LocalDateTime.now();
        this.status = "REQUESTED";
        this.seatsBooked = 1;
    }

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

    public boolean isActive() {
        return status.equals("REQUESTED") || status.equals("CONFIRMED");
    }

    public boolean isConfirmed() {
        return status.equals("CONFIRMED");
    }

    public boolean isCancelled() {
        return status.equals("CANCELLED");
    }

    public void confirm() {
        this.status = "CONFIRMED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public String getRouteDescription() {
        return origin + " → " + destination;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return timestamp.format(formatter);
    }

    public String getSummary() {
        return passengerName + " booked " + seatsBooked + " seat(s) - " + status;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
