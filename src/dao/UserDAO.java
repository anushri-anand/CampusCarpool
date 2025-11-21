package dao;

import models.User;
import models.Driver;
import models.Passenger;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * UserDAO handles database operations for User objects
 */
public class UserDAO {

    /**
     * Create a new user in the database
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (name, roll_number, email, password, role, warnings, " +
                     "blacklist_until, rating, total_ratings) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getRollNumber());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());
            pstmt.setInt(6, user.getWarnings());
            pstmt.setString(7, user.getBlacklistUntil() != null ? user.getBlacklistUntil().toString() : null);
            pstmt.setDouble(8, user.getRating());
            pstmt.setInt(9, user.getTotalRatings());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Update an existing user
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, password = ?, warnings = ?, " +
                     "blacklist_until = ?, rating = ?, total_ratings = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getWarnings());
            pstmt.setString(4, user.getBlacklistUntil() != null ? user.getBlacklistUntil().toString() : null);
            pstmt.setDouble(5, user.getRating());
            pstmt.setInt(6, user.getTotalRatings());
            pstmt.setInt(7, user.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get user by ID
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get user by roll number
     */
    public User getUserByRollNumber(String rollNumber) {
        String sql = "SELECT * FROM users WHERE roll_number = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by roll number: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Authenticate user with email and password
     */
    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Delete a user
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Extract User object from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        LocalDateTime blacklistUntil = null;
        String blacklistStr = rs.getString("blacklist_until");
        if (blacklistStr != null && !blacklistStr.isEmpty()) {
            blacklistUntil = LocalDateTime.parse(blacklistStr);
        }

        return new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("roll_number"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getInt("warnings"),
            blacklistUntil,
            rs.getDouble("rating"),
            rs.getInt("total_ratings")
        );
    }
}

