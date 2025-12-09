package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RideRequest {
    private int id;
    private int passengerId;
    private String passengerName;
    private String origin;
    private String destination;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private int seatsRequested;
    private String status;
    private String notes;
    private LocalDateTime createdAt;

    public RideRequest() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public RideRequest(int passengerId, String passengerName, String origin, String destination,
                       LocalDate preferredDate, LocalTime preferredTime,
                       int seatsRequested, String notes) {
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.seatsRequested = seatsRequested;
        this.notes = notes;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public RideRequest(int id, int passengerId, String passengerName, String origin, String destination,
                       LocalDate preferredDate, LocalTime preferredTime,
                       int seatsRequested, String status, String notes,
                       LocalDateTime createdAt) {
        this.id = id;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.seatsRequested = seatsRequested;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(LocalDate preferredDate) { this.preferredDate = preferredDate; }
    public LocalTime getPreferredTime() { return preferredTime; }
    public void setPreferredTime(LocalTime preferredTime) { this.preferredTime = preferredTime; }
    public int getSeatsRequested() { return seatsRequested; }
    public void setSeatsRequested(int seatsRequested) { this.seatsRequested = seatsRequested; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void markAsMatched() { this.status = "MATCHED"; }
    public void cancelRequest() { this.status = "CANCELLED"; }
    public boolean isActive() { return status.equals("PENDING"); }

    public boolean matchesRide(Ride ride) {
        if (!isActive()) return false;
        boolean routeMatches = this.origin.equalsIgnoreCase(ride.getOrigin()) &&
                               this.destination.equalsIgnoreCase(ride.getDestination());
        boolean dateMatches = this.preferredDate.equals(ride.getDepartureDate());
        return routeMatches && dateMatches;
    }

    public boolean isTimeCompatible(Ride ride) {
        LocalTime rideTime = ride.getDepartureTime();
        long minutesDiff = Math.abs(java.time.Duration.between(this.preferredTime, rideTime).toMinutes());
        return minutesDiff <= 120;
    }

    public boolean canBeFulfilledBy(Ride ride) {
        return matchesRide(ride) && ride.getSeatsAvailable() >= this.seatsRequested && ride.isActive();
    }

    public String getFormattedRequestDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return preferredDate.format(dateFormatter) + " at " + preferredTime.format(timeFormatter);
    }

    public String getRouteDescription() {
        return origin + " → " + destination;
    }

    public String getSummary() {
        return passengerName + " needs " + seatsRequested + " seat(s) from " +
               getRouteDescription() + " on " + getFormattedRequestDateTime();
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "id=" + id +
                ", passenger='" + passengerName + "' (ID:" + passengerId + ")" +
                ", route='" + getRouteDescription() + '\'' +
                ", preferredDateTime='" + getFormattedRequestDateTime() + '\'' +
                ", seatsRequested=" + seatsRequested +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RideRequest request = (RideRequest) obj;
        return id == request.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
