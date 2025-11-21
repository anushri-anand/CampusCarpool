package dao;

import models.Driver;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * DriverDAO handles database operations for Driver-specific data
 */
public class DriverDAO {

    /**
     * Create driver record
     */
    public boolean createDriver(Driver driver) {
        String sql = "INSERT INTO drivers (user_id, license_number, vehicle_model, " +
                     "vehicle_number, seats_available) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, driver.getId());
            pstmt.setString(2, driver.getLicenseNumber());
            pstmt.setString(3, driver.getVehicleModel());
            pstmt.setString(4, driver.getVehicleNumber());
            pstmt.setInt(5, driver.getSeatsAvailable());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Update driver information
     */
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE drivers SET license_number = ?, vehicle_model = ?, " +
                     "vehicle_number = ?, seats_available = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, driver.getLicenseNumber());
            pstmt.setString(2, driver.getVehicleModel());
            pstmt.setString(3, driver.getVehicleNumber());
            pstmt.setInt(4, driver.getSeatsAvailable());
            pstmt.setInt(5, driver.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get driver by user ID
     */
    public Driver getDriverByUserId(int userId) {
        String sql = "SELECT u.*, d.license_number, d.vehicle_model, d.vehicle_number, d.seats_available " +
                     "FROM users u JOIN drivers d ON u.id = d.user_id WHERE u.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDriverFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting driver by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Extract Driver object from ResultSet
     */
    private Driver extractDriverFromResultSet(ResultSet rs) throws SQLException {
        LocalDateTime blacklistUntil = null;
        String blacklistStr = rs.getString("blacklist_until");
        if (blacklistStr != null && !blacklistStr.isEmpty()) {
            blacklistUntil = LocalDateTime.parse(blacklistStr);
        }

        return new Driver(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("roll_number"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("warnings"),
            blacklistUntil,
            rs.getDouble("rating"),
            rs.getInt("total_ratings"),
            rs.getString("license_number"),
            rs.getString("vehicle_model"),
            rs.getString("vehicle_number"),
            rs.getInt("seats_available")
        );
    }

    /**
     * Delete driver record
     */
    public boolean deleteDriver(int userId) {
        String sql = "DELETE FROM drivers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

