package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Ride class representing a carpool ride posted by a driver.
 * Demonstrates: Encapsulation, Composition, Collections
 */
public class Ride {
    // Ride attributes
    private int id;
    private int driverId; // Foreign key to Driver
    private String driverName; // Cached for display
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private int seatsAvailable;
    private int seatsTotal;
    private double pricePerSeat;
    private String status; // "ACTIVE", "COMPLETED", "CANCELLED"
    private String vehicleInfo; // e.g., "Honda City - KA01AB1234"
    private List<Integer> bookedPassengerIds; // List of passenger IDs who booked

    // Constructors

    /**
     * Default constructor
     */
    public Ride() {
        this.bookedPassengerIds = new ArrayList<>();
        this.status = "ACTIVE";
    }

    /**
     * Constructor for creating a new ride (without ID)
     * Constructor Overloading
     */
    public Ride(int driverId, String driverName, String origin, String destination,
                LocalDate departureDate, LocalTime departureTime, int seatsAvailable,
                double pricePerSeat, String vehicleInfo) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.seatsAvailable = seatsAvailable;
        this.seatsTotal = seatsAvailable;
        this.pricePerSeat = pricePerSeat;
        this.vehicleInfo = vehicleInfo;
        this.status = "ACTIVE";
        this.bookedPassengerIds = new ArrayList<>();
    }

    /**
     * Full constructor including ID (for database retrieval)
     * Constructor Overloading
     */
    public Ride(int id, int driverId, String driverName, String origin, String destination,
                LocalDate departureDate, LocalTime departureTime, int seatsAvailable,
                int seatsTotal, double pricePerSeat, String status, String vehicleInfo) {
        this.id = id;
        this.driverId = driverId;
        this.driverName = driverName;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.seatsAvailable = seatsAvailable;
        this.seatsTotal = seatsTotal;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
        this.vehicleInfo = vehicleInfo;
        this.bookedPassengerIds = new ArrayList<>();
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public List<Integer> getBookedPassengerIds() {
        return new ArrayList<>(bookedPassengerIds); // Return copy for encapsulation
    }

    public void setBookedPassengerIds(List<Integer> bookedPassengerIds) {
        this.bookedPassengerIds = new ArrayList<>(bookedPassengerIds);
    }

    // Business Logic Methods

    /**
     * Books a seat for a passenger
     * @param passengerId the ID of the passenger booking the ride
     * @return true if booking successful, false if no seats available or already booked
     */
    public boolean bookSeat(int passengerId) {
        // Check if passenger already booked
        if (bookedPassengerIds.contains(passengerId)) {
            return false; // Already booked
        }

        // Check if seats available
        if (seatsAvailable <= 0) {
            return false; // No seats available
        }

        // Check if ride is active
        if (!status.equals("ACTIVE")) {
            return false; // Ride not active
        }

        // Book the seat
        bookedPassengerIds.add(passengerId);
        seatsAvailable--;
        return true;
    }

    /**
     * Cancels a booking for a passenger
     * @param passengerId the ID of the passenger cancelling
     * @return true if cancellation successful, false if passenger not found
     */
    public boolean cancelBooking(int passengerId) {
        if (bookedPassengerIds.remove(Integer.valueOf(passengerId))) {
            seatsAvailable++;
            return true;
        }
        return false; // Passenger not found in bookings
    }

    /**
     * Checks if the ride has available seats
     * @return true if seats available, false otherwise
     */
    public boolean hasAvailableSeats() {
        return seatsAvailable > 0 && status.equals("ACTIVE");
    }

    /**
     * Checks if a specific passenger has booked this ride
     * @param passengerId the passenger ID to check
     * @return true if passenger has booked, false otherwise
     */
    public boolean isPassengerBooked(int passengerId) {
        return bookedPassengerIds.contains(passengerId);
    }

    /**
     * Gets the number of seats currently booked
     * @return count of booked seats
     */
    public int getBookedSeatsCount() {
        return seatsTotal - seatsAvailable;
    }

    /**
     * Checks if the ride is full
     * @return true if no seats available, false otherwise
     */
    public boolean isFull() {
        return seatsAvailable == 0;
    }

    /**
     * Marks the ride as completed
     */
    public void markAsCompleted() {
        this.status = "COMPLETED";
    }

    /**
     * Cancels the ride (driver cancels)
     */
    public void cancelRide() {
        this.status = "CANCELLED";
    }

    /**
     * Checks if ride is active
     * @return true if status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return status.equals("ACTIVE");
    }

    /**
     * Gets formatted departure date and time
     * @return formatted string like "Dec 25, 2024 at 9:30 AM"
     */
    public String getFormattedDepartureDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return departureDate.format(dateFormatter) + " at " + departureTime.format(timeFormatter);
    }

    /**
     * Gets route description
     * @return formatted route like "Campus → MG Road"
     */
    public String getRouteDescription() {
        return origin + " → " + destination;
    }

    /**
     * Calculates total price for all booked seats
     * @return total revenue from this ride
     */
    public double getTotalRevenue() {
        return getBookedSeatsCount() * pricePerSeat;
    }

    // Override toString for debugging and display
    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", driver='" + driverName + '\'' +
                ", route='" + getRouteDescription() + '\'' +
                ", departure='" + getFormattedDepartureDateTime() + '\'' +
                ", seats=" + seatsAvailable + "/" + seatsTotal +
                ", price=₹" + pricePerSeat +
                ", status='" + status + '\'' +
                ", passengers=" + bookedPassengerIds.size() +
                '}';
    }

    // Override equals for ride comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ride ride = (Ride) obj;
        return id == ride.id;
    }

    // Override hashCode for collections
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
