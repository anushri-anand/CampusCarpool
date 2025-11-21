package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RideRequest class representing a passenger's ride request.
 * Demonstrates: Encapsulation, Composition, Lifecycle Management
 */
public class RideRequest {
    // Request attributes
    private int id;
    private int passengerId; // Foreign key to Passenger/User
    private String origin;
    private String destination;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private int seatsRequested;
    private String status; // "PENDING", "MATCHED", "CANCELLED"
    private String notes; // Optional info
    private LocalDateTime createdAt;

    // Constructors
    public RideRequest() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public RideRequest(int passengerId, String origin, String destination,
                       LocalDate preferredDate, LocalTime preferredTime,
                       int seatsRequested, String notes) {
        this.passengerId = passengerId;
        this.origin = origin;
        this.destination = destination;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.seatsRequested = seatsRequested;
        this.notes = notes;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public RideRequest(int id, int passengerId, String origin, String destination,
                       LocalDate preferredDate, LocalTime preferredTime,
                       int seatsRequested, String status, String notes,
                       LocalDateTime createdAt) {
        this.id = id;
        this.passengerId = passengerId;
        this.origin = origin;
        this.destination = destination;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.seatsRequested = seatsRequested;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }

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

    // Business Logic Methods
    public void markAsMatched() { this.status = "MATCHED"; }
    public void cancelRequest() { this.status = "CANCELLED"; }
    public boolean isActive() { return status.equals("PENDING"); }

    // Utility
    public String getFormattedRequestDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return preferredDate.format(dateFormatter) + " at " + preferredTime.format(timeFormatter);
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "id=" + id +
                ", passengerId=" + passengerId +
                ", route='" + origin + " → " + destination + '\'' +
                ", preferredDateTime='" + getFormattedRequestDateTime() + '\'' +
                ", seatsRequested=" + seatsRequested +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
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
