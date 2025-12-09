package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

  public class Ride {
  private int id;
  private int driverId;
  private String driverName;
  private String origin;
  private String destination;
  private LocalDate departureDate;
  private LocalTime departureTime;
  private int seatsAvailable;
  private int seatsTotal;
  private double pricePerSeat;
  private String status;
  private String vehicleInfo;
  private List<Integer> bookedPassengerIds;

  public Ride() {
  this.bookedPassengerIds = new ArrayList<>();
  this.status = "ACTIVE";
  }

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

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  public int getDriverId() { return driverId; }
  public void setDriverId(int driverId) { this.driverId = driverId; }
  public String getDriverName() { return driverName; }
  public void setDriverName(String driverName) { this.driverName = driverName; }
  public String getOrigin() { return origin; }
  public void setOrigin(String origin) { this.origin = origin; }
  public String getDestination() { return destination; }
  public void setDestination(String destination) { this.destination = destination; }
  public LocalDate getDepartureDate() { return departureDate; }
  public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }
  public LocalTime getDepartureTime() { return departureTime; }
  public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }
  public int getSeatsAvailable() { return seatsAvailable; }
  public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }
  public int getSeatsTotal() { return seatsTotal; }
  public void setSeatsTotal(int seatsTotal) { this.seatsTotal = seatsTotal; }
  public double getPricePerSeat() { return pricePerSeat; }
  public void setPricePerSeat(double pricePerSeat) { this.pricePerSeat = pricePerSeat; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public String getVehicleInfo() { return vehicleInfo; }
  public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }
  public List<Integer> getBookedPassengerIds() { return new ArrayList<>(bookedPassengerIds); }
  public void setBookedPassengerIds(List<Integer> bookedPassengerIds) { this.bookedPassengerIds = new ArrayList<>(bookedPassengerIds); }

  public boolean bookSeat(int passengerId) {
  if (bookedPassengerIds.contains(passengerId) || seatsAvailable <= 0 || !status.equals("ACTIVE")) return false;
  bookedPassengerIds.add(passengerId);
  seatsAvailable--;
  return true;
  }

  public boolean cancelBooking(int passengerId) {
  if (bookedPassengerIds.remove(Integer.valueOf(passengerId))) {
  seatsAvailable++;
  return true;
  }
  return false;
  }

  public boolean hasAvailableSeats() { return seatsAvailable > 0 && status.equals("ACTIVE"); }
  public boolean isPassengerBooked(int passengerId) { return bookedPassengerIds.contains(passengerId); }
  public int getBookedSeatsCount() { return seatsTotal - seatsAvailable; }
  public boolean isFull() { return seatsAvailable == 0; }
  public void markAsCompleted() { this.status = "COMPLETED"; }
  public void cancelRide() { this.status = "CANCELLED"; }
  public boolean isActive() { return status.equals("ACTIVE"); }

  public boolean canFulfillRequest(RideRequest request) {
  if (!isActive() || seatsAvailable < request.getSeatsRequested()) return false;
  boolean routeMatches = this.origin.equalsIgnoreCase(request.getOrigin()) &&
  this.destination.equalsIgnoreCase(request.getDestination());
  boolean dateMatches = this.departureDate.equals(request.getPreferredDate());
  return routeMatches && dateMatches;
  }

  public String getFormattedDepartureDateTime() {
  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
  return departureDate.format(dateFormatter) + " at " + departureTime.format(timeFormatter);
  }

  public String getRouteDescription() { return origin + " → " + destination; }
  public double getTotalRevenue() { return getBookedSeatsCount() * pricePerSeat; }

  public String getPickupLocation() { return origin; }
  public String getDropLocation() { return destination; }

  public String getDropoffLocation() {
    return getDropLocation(); 
    }

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

  @Override
  public boolean equals(Object obj) {
  if (this == obj) return true;
  if (obj == null || getClass() != obj.getClass()) return false;
  Ride ride = (Ride) obj;
  return id == ride.id;
  }

  @Override
  public int hashCode() { return Integer.hashCode(id); }
  }
