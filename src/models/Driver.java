package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Driver extends User {
    private String licenseNumber;
    private String vehicleModel;
    private String vehicleNumber;
    private int seatsAvailable;
    private List<Integer> postedRideIds;

    public Driver() {
        super();
        this.postedRideIds = new ArrayList<>();
    }

    public Driver(String name, String rollNumber, String email, String password,
                  String licenseNumber, String vehicleModel, String vehicleNumber, int seatsAvailable) {
        super(name, rollNumber, email, password, "DRIVER");
        this.licenseNumber = licenseNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.seatsAvailable = seatsAvailable;
        this.postedRideIds = new ArrayList<>();
    }

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
        return new ArrayList<>(postedRideIds);
    }

    public void setPostedRideIds(List<Integer> postedRideIds) {
        this.postedRideIds = new ArrayList<>(postedRideIds);
    }

    public void addPostedRide(int rideId) {
        if (!postedRideIds.contains(rideId)) {
            postedRideIds.add(rideId);
        }
    }

    public void removePostedRide(int rideId) {
        postedRideIds.remove(Integer.valueOf(rideId));
    }

    public boolean canPostRide() {
        return canPerformAction();
    }

    public boolean hasValidVehicleInfo() {
        return licenseNumber != null && !licenseNumber.isEmpty() &&
               vehicleModel != null && !vehicleModel.isEmpty() &&
               vehicleNumber != null && !vehicleNumber.isEmpty() &&
               seatsAvailable > 0 && seatsAvailable <= 8;
    }

    public int getTotalRidesPosted() {
        return postedRideIds.size();
    }

    @Override
    public String getRoleDescription() {
        return "Driver (" + vehicleModel + ")";
    }

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
