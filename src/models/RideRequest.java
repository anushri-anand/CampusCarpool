package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RideRequest class representing a passenger's ride request.
 * Passengers post these when they need a ride, and drivers can view and fulfill them.
 * Demonstrates: Encapsulation, Composition, Lifecycle Management
 */
public class RideRequest {
    // Request attributes
    private int id;
    private int passengerId; // Foreign key to Passenger/User
    private String passengerName; // Cached for display purposes
    private String origin;
    private String destination;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private int seatsRequested;
    private String status; // "PENDING", "MATCHED", "CANCELLED"
    private String notes; // Optional info from passenger
    private LocalDateTime createdAt;

    // Constructors

    /**
     * Default constructor
     */
    public RideRequest() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor for creating a new ride request (without ID)
     * Constructor Overloading
     */
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

    /**
     * Full constructor including ID (for database retrieval)
     * Constructor Overloading
     */
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

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalTime getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(LocalTime preferredTime) {
        this.preferredTime = preferredTime;
    }

    public int getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(int seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business Logic Methods

    /**
     * Marks the request as matched (when driver accepts it)
     */
    public void markAsMatched() {
        this.status = "MATCHED";
    }

    /**
     * Cancels the ride request
     */
    public void cancelRequest() {
        this.status = "CANCELLED";
    }

    /**
     * Checks if request is still active (pending)
     * @return true if status is PENDING, false otherwise
     */
    public boolean isActive() {
        return status.equals("PENDING");
    }

    /**
     * Checks if this request matches a given ride's route and date
     * @param ride the ride to check against
     * @return true if request matches the ride, false otherwise
     */
    public boolean matchesRide(Ride ride) {
        if (!isActive()) {
            return false;
        }

        // Check if route matches
        boolean routeMatches = this.origin.equalsIgnoreCase(ride.getOrigin()) &&
                              this.destination.equalsIgnoreCase(ride.getDestination());

        // Check if date matches
        boolean dateMatches = this.preferredDate.equals(ride.getDepartureDate());

        return routeMatches && dateMatches;
    }

    /**
     * Checks if request time is close to ride time (within tolerance window)
     * @param ride the ride to check against
     * @return true if times are compatible, false otherwise
     */
    public boolean isTimeCompatible(Ride ride) {
        LocalTime rideTime = ride.getDepartureTime();
        long minutesDiff = Math.abs(
            java.time.Duration.between(this.preferredTime, rideTime).toMinutes()
        );
        
        // Within 2 hours (120 minutes) tolerance
        return minutesDiff <= 120;
    }

    /**
     * Checks if a ride can fully satisfy this request
     * @param ride the ride to check
     * @return true if ride matches and has enough seats, false otherwise
     */
    public boolean canBeFulfilledBy(Ride ride) {
        return matchesRide(ride) && 
               ride.getSeatsAvailable() >= this.seatsRequested &&
               ride.isActive();
    }

    // Utility Methods

    /**
     * Gets formatted departure date and time
     * @return formatted string like "Dec 25, 2024 at 9:30 AM"
     */
    public String getFormattedRequestDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return preferredDate.format(dateFormatter) + " at " + preferredTime.format(timeFormatter);
    }

    /**
     * Gets route description
     * @return formatted route like "Campus → Dubai Mall"
     */
    public String getRouteDescription() {
        return origin + " → " + destination;
    }

    /**
     * Gets a summary of the request for display
     * @return summary string
     */
    public String getSummary() {
        return passengerName + " needs " + seatsRequested + " seat(s) from " +
               getRouteDescription() + " on " + getFormattedRequestDateTime();
    }

    // Override toString for debugging and display
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

    // Override equals for request comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RideRequest request = (RideRequest) obj;
        return id == request.id;
    }

    // Override hashCode for collections
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
