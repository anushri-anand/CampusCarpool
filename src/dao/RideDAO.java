package dao;

import models.Ride;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RideDAO {

    public boolean createRide(Ride ride) {
        String sql = "INSERT INTO rides (driver_id, driver_name, origin, destination, " +
                     "departure_date, departure_time, seats_available, seats_total, " +
                     "price_per_seat, status, vehicle_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, ride.getDriverId());
            pstmt.setString(2, ride.getDriverName());
            pstmt.setString(3, ride.getOrigin());
            pstmt.setString(4, ride.getDestination());
            pstmt.setString(5, ride.getDepartureDate().toString());
            pstmt.setString(6, ride.getDepartureTime().toString());
            pstmt.setInt(7, ride.getSeatsAvailable());
            pstmt.setInt(8, ride.getSeatsTotal());
            pstmt.setDouble(9, ride.getPricePerSeat());
            pstmt.setString(10, ride.getStatus());
            pstmt.setString(11, ride.getVehicleInfo());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ride.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating ride: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean updateRide(Ride ride) {
        String sql = "UPDATE rides SET seats_available = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ride.getSeatsAvailable());
            pstmt.setString(2, ride.getStatus());
            pstmt.setInt(3, ride.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating ride: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public Ride getRideById(int id) {
        String sql = "SELECT * FROM rides WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRideFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ride by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Ride> getAllActiveRides() {
        String sql = "SELECT * FROM rides WHERE status = 'ACTIVE' ORDER BY departure_date, departure_time";
        List<Ride> rides = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rides.add(extractRideFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active rides: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rides;
    }

    public List<Ride> getRidesByDestination(String destination) {
        String sql = "SELECT * FROM rides WHERE destination = ? AND status = 'ACTIVE' " +
                     "ORDER BY departure_date, departure_time";
        List<Ride> rides = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, destination);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                rides.add(extractRideFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rides by destination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rides;
    }

    public List<Ride> getRidesByRoute(String origin, String destination) {
        String sql = "SELECT * FROM rides WHERE origin = ? AND destination = ? AND status = 'ACTIVE' " +
                     "ORDER BY departure_date, departure_time";
        List<Ride> rides = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                rides.add(extractRideFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rides by route: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rides;
    }

    public List<Ride> getRidesByDate(LocalDate date) {
        String sql = "SELECT * FROM rides WHERE departure_date = ? AND status = 'ACTIVE' " +
                     "ORDER BY departure_time";
        List<Ride> rides = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                rides.add(extractRideFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rides by date: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rides;
    }

    public List<Ride> getRidesByDriver(int driverId) {
        String sql = "SELECT * FROM rides WHERE driver_id = ? ORDER BY departure_date DESC, departure_time DESC";
        List<Ride> rides = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, driverId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                rides.add(extractRideFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rides by driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rides;
    }

public List<Ride> getRidesBookedByPassenger(int passengerId) {
    List<Ride> rides = new ArrayList<>();
    String sql = "SELECT r.* FROM rides r " +
             "JOIN bookings b ON r.id = b.ride_id " +
             "WHERE b.passenger_id = ? AND b.status = 'CONFIRMED'";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, passengerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Ride ride = new Ride();
            ride.setId(rs.getInt("id"));
            ride.setDriverId(rs.getInt("driver_id"));
            ride.setOrigin(rs.getString("origin"));
            ride.setDestination(rs.getString("destination"));
            ride.setDepartureDate(rs.getDate("departure_date").toLocalDate());
            ride.setDepartureTime(rs.getTime("departure_time").toLocalTime());
            ride.setSeatsAvailable(rs.getInt("seats_available"));
            ride.setSeatsTotal(rs.getInt("seats_total"));
            ride.setPricePerSeat(rs.getDouble("price_per_seat"));
            ride.setStatus(rs.getString("status"));
            rides.add(ride);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return rides;
}

    private Ride extractRideFromResultSet(ResultSet rs) throws SQLException {
        return new Ride(
            rs.getInt("id"),
            rs.getInt("driver_id"),
            rs.getString("driver_name"),
            rs.getString("origin"),
            rs.getString("destination"),
            LocalDate.parse(rs.getString("departure_date")),
            LocalTime.parse(rs.getString("departure_time")),
            rs.getInt("seats_available"),
            rs.getInt("seats_total"),
            rs.getDouble("price_per_seat"),
            rs.getString("status"),
            rs.getString("vehicle_info")
        );
    }
    
    public boolean deleteRide(int rideId) {
        String sql = "DELETE FROM rides WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rideId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting ride: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

