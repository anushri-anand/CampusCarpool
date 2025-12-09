package dao;

import models.Passenger;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;


public class PassengerDAO {

    public boolean createPassenger(Passenger passenger) {
        String sql = "INSERT INTO passengers (user_id, preferred_destination) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, passenger.getId());
            pstmt.setString(2, passenger.getPreferredDestination());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating passenger: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean updatePassenger(Passenger passenger) {
        String sql = "UPDATE passengers SET preferred_destination = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passenger.getPreferredDestination());
            pstmt.setInt(2, passenger.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating passenger: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public Passenger getPassengerByUserId(int userId) {
        String sql = "SELECT u.*, p.preferred_destination " +
                     "FROM users u JOIN passengers p ON u.id = p.user_id WHERE u.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPassengerFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting passenger by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    private Passenger extractPassengerFromResultSet(ResultSet rs) throws SQLException {
        LocalDateTime blacklistUntil = null;
        String blacklistStr = rs.getString("blacklist_until");
        if (blacklistStr != null && !blacklistStr.isEmpty()) {
            blacklistUntil = LocalDateTime.parse(blacklistStr);
        }

        return new Passenger(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("roll_number"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("warnings"),
            blacklistUntil,
            rs.getDouble("rating"),
            rs.getInt("total_ratings"),
            rs.getString("preferred_destination")
        );
    }

    public boolean deletePassenger(int userId) {
        String sql = "DELETE FROM passengers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting passenger: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

