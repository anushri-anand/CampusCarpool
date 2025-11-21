package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Driver class extending User.
 * Represents a student who offers rides and can view ride requests.
 * Demonstrates: Inheritance, Polymorphism, Encapsulation
 */
public class Driver extends User {
    // Driver-specific fields
    private String licenseNumber;
    private String vehicleModel;
    private String vehicleNumber;
    private int seatsAvailable;
    private List<Integer> postedRideIds; // Track rides posted by this driver

    // Constructors

    /**
     * Default constructor
     */
    public Driver() {
        super();
        this.postedRideIds = new ArrayList<>();
    }

    /**
     * Constructor with User fields + Driver-specific fields
     * Constructor Overloading
     */
    public Driver(String name, String rollNumber, String email, String password,
                  String licenseNumber, String vehicleModel, String vehicleNumber, int seatsAvailable) {
        super(name, rollNumber, email, password, "DRIVER");
        this.licenseNumber = licenseNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.seatsAvailable = seatsAvailable;
        this.postedRideIds = new ArrayList<>();
    }

    /**
     * Full constructor including ID (for database retrieval)
     * Constructor Overloading
     */
    public Driver(int id, String name, String rollNumber, String email, String password,
                  int warnings, LocalDateTime blacklistUntil, double rating, int totalRatings,
                  String licenseNumber, String vehicleModel, String vehicleNumber, int seatsAvailable) {
        super(id, name, rollNumber, email, password, "DRIVER", warnings, blacklistUntil, rating, totalRatings);
        this.licenseNumber = licenseNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.seatsAvailable = seatsAvailable;
        this.postedRideIds = new ArrayList<>();
    }

    // Getters and Setters

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public List<Integer> getPostedRideIds() {
        return new ArrayList<>(postedRideIds); // Return copy for encapsulation
    }

    public void setPostedRideIds(List<Integer> postedRideIds) {
        this.postedRideIds = new ArrayList<>(postedRideIds);
    }

    // Driver-specific Business Logic

    /**
     * Adds a ride ID to the driver's posted rides
     * @param rideId the ID of the newly posted ride
     */
    public void addPostedRide(int rideId) {
        if (!postedRideIds.contains(rideId)) {
            postedRideIds.add(rideId);
        }
    }

    /**
     * Removes a ride ID from the driver's posted rides (when cancelled)
     * @param rideId the ID of the ride to remove
     */
    public void removePostedRide(int rideId) {
        postedRideIds.remove(Integer.valueOf(rideId));
    }

    /**
     * Checks if driver can post a new ride
     * @return true if driver is not blacklisted and has valid vehicle info, false otherwise
     */
    public boolean canPostRide() {
        return canPerformAction() && hasValidVehicleInfo();
    }

    /**
     * Validates driver's vehicle information
     * @return true if all vehicle info is valid, false otherwise
     */
    public boolean hasValidVehicleInfo() {
        return licenseNumber != null && !licenseNumber.isEmpty() &&
               vehicleModel != null && !vehicleModel.isEmpty() &&
               vehicleNumber != null && !vehicleNumber.isEmpty() &&
               seatsAvailable > 0 && seatsAvailable <= 8; // Reasonable seat limit
    }

    /**
     * Gets total number of rides posted by this driver
     * @return count of posted rides
     */
    public int getTotalRidesPosted() {
        return postedRideIds.size();
    }

    /**
     * Checks if driver can view and respond to ride requests
     * @return true if driver is not blacklisted, false otherwise
     */
    public boolean canViewRideRequests() {
        return !isBlacklisted();
    }

    /**
     * Checks if driver can accept a ride request
     * Must not be blacklisted and must have valid vehicle info
     * @return true if driver can accept requests, false otherwise
     */
    public boolean canAcceptRequest() {
        return !isBlacklisted() && hasValidVehicleInfo();
    }

    /**
     * Placeholder for driver preferences (future expansion)
     * @return true if driver wants to view requests, false otherwise
     */
    public boolean wantsToServeRequests() {
        return true; // Always yes for now, can be made configurable later
    }

    // Method Overriding - Polymorphism

    /**
     * Override getRoleDescription from User class
     * @return driver-specific role description
     */
    @Override
    public String getRoleDescription() {
        return "Driver (" + vehicleModel + ")";
    }

    /**
     * Override toString for Driver-specific information
     */
    @Override
    public String toString() {
        return "Driver{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", rollNumber='" + getRollNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", rating=" + String.format("%.2f", getRating()) +
                ", totalRidesPosted=" + getTotalRidesPosted() +
                ", blacklisted=" + isBlacklisted() +
                '}';
    }
}
