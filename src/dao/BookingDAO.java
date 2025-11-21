package dao;

import models.Booking;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDAO handles database operations for Booking objects
 */
public class BookingDAO {

    /**
     * Create a new booking
     */
    public boolean createBooking(int passengerId, int rideId, int seatsBooked) {
        String sql = "INSERT INTO bookings (ride_id, passenger_id, seats_booked, status) " +
                     "VALUES (?, ?, ?, 'REQUESTED')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, rideId);
            pstmt.setInt(2, passengerId);
            pstmt.setInt(3, seatsBooked);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get booking by ID
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Check if passenger has booked a ride
     */
    public boolean hasPassengerBooked(int passengerId, int rideId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE passenger_id = ? AND ride_id = ? " +
                     "AND status IN ('REQUESTED', 'CONFIRMED')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, passengerId);
            pstmt.setInt(2, rideId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Cancel a booking
     */
    public boolean cancelBooking(int bookingId) {
        return updateBookingStatus(bookingId, "CANCELLED");
    }

    /**
     * Update booking status
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get all bookings for a passenger
     */
    public List<Booking> getBookingsByPassenger(int passengerId) {
        String sql = "SELECT * FROM bookings WHERE passenger_id = ? ORDER BY timestamp DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, passengerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by passenger: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }

    /**
     * Get all bookings for a ride
     */
    public List<Booking> getBookingsByRide(int rideId) {
        String sql = "SELECT * FROM bookings WHERE ride_id = ? ORDER BY timestamp";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rideId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by ride: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }

    /**
     * Get pending bookings for a driver's rides
     */
    public List<Booking> getPendingBookingsByDriver(int driverId) {
        String sql = "SELECT b.* FROM bookings b " +
                     "JOIN rides r ON b.ride_id = r.id " +
                     "WHERE r.driver_id = ? AND b.status = 'REQUESTED' " +
                     "ORDER BY b.timestamp";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, driverId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting pending bookings by driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }

    /**
     * Extract Booking from ResultSet
     */
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setRideId(rs.getInt("ride_id"));
        booking.setPassengerId(rs.getInt("passenger_id"));
        booking.setSeatsBooked(rs.getInt("seats_booked"));
        booking.setStatus(rs.getString("status"));
        
        String timestampStr = rs.getString("timestamp");
        if (timestampStr != null) {
            booking.setTimestamp(LocalDateTime.parse(timestampStr));
        }
        
        return booking;
    }

    /**
     * Delete booking
     */
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

