package dao;

import models.RideRequest;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RideRequestDAO handles all database operations for RideRequest objects
 */
public class RideRequestDAO {

    /**
     * Create a new ride request in the database
     */
    public boolean createRideRequest(RideRequest request) {
        String sql = "INSERT INTO ride_requests (passenger_id, passenger_name, origin, destination, " +
                     "preferred_date, preferred_time, seats_requested, status, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, request.getPassengerId());
            pstmt.setString(2, request.getPassengerName());
            pstmt.setString(3, request.getOrigin());
            pstmt.setString(4, request.getDestination());
            pstmt.setString(5, request.getPreferredDate().toString());
            pstmt.setString(6, request.getPreferredTime().toString());
            pstmt.setInt(7, request.getSeatsRequested());
            pstmt.setString(8, request.getStatus());
            pstmt.setString(9, request.getNotes());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        request.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating ride request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Update an existing ride request
     */
    public boolean updateRideRequest(RideRequest request) {
        String sql = "UPDATE ride_requests SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, request.getStatus());
            pstmt.setInt(2, request.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating ride request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get a ride request by ID
     */
    public RideRequest getRideRequestById(int id) {
        String sql = "SELECT * FROM ride_requests WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRideRequestFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ride request by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get all pending ride requests
     */
    public List<RideRequest> getAllPendingRequests() {
        String sql = "SELECT * FROM ride_requests WHERE status = 'PENDING' " +
                     "ORDER BY preferred_date, preferred_time";
        List<RideRequest> requests = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractRideRequestFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting pending requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }

    /**
     * Get ride requests by passenger
     */
    public List<RideRequest> getRequestsByPassenger(int passengerId) {
        String sql = "SELECT * FROM ride_requests WHERE passenger_id = ? " +
                     "ORDER BY preferred_date DESC, preferred_time DESC";
        List<RideRequest> requests = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, passengerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractRideRequestFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting requests by passenger: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }

    /**
     * Helper method to extract RideRequest from ResultSet
     */
    private RideRequest extractRideRequestFromResultSet(ResultSet rs) throws SQLException {
        return new RideRequest(
            rs.getInt("id"),
            rs.getInt("passenger_id"),
            rs.getString("passenger_name"),
            rs.getString("origin"),
            rs.getString("destination"),
            LocalDate.parse(rs.getString("preferred_date")),
            LocalTime.parse(rs.getString("preferred_time")),
            rs.getInt("seats_requested"),
            rs.getString("status"),
            rs.getString("notes"),
            LocalDateTime.parse(rs.getString("created_at"))
        );
    }

    /**
     * Delete a ride request
     */
    public boolean deleteRideRequest(int requestId) {
        String sql = "DELETE FROM ride_requests WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting ride request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

