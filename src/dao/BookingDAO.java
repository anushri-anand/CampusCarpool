package dao;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDAO handles database operations for bookings
 */
public class BookingDAO {

    // 1. Create a new booking
    public boolean createBooking(int passengerId, int rideId, int seatsBooked) {
        String sql = "INSERT INTO bookings (ride_id, passenger_id, seats_booked, status) VALUES (?, ?, ?, 'CONFIRMED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rideId);
            pstmt.setInt(2, passengerId);
            pstmt.setInt(3, seatsBooked);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 2. Check if passenger has booked a ride
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

    // 3. Cancel a booking
    public boolean cancelBooking(int bookingId) {
        return updateBookingStatus(bookingId, "CANCELLED");
    }

    // 4. Update booking status (e.g., confirm, cancel)
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

    // 5. Get ride ID by booking ID
    public Integer getRideIdByBookingId(int bookingId) {
        String sql = "SELECT ride_id FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ride_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting ride ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public List<Integer> getRideIdsByPassenger(int passengerId) {
    List<Integer> rideIds = new ArrayList<>();
    String sql = "SELECT ride_id FROM bookings WHERE passenger_id = ? AND status IN ('CONFIRMED', 'REQUESTED')";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, passengerId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            rideIds.add(rs.getInt("ride_id"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rideIds;
}


    // 6. Get passenger ID by booking ID
    public Integer getPassengerIdByBookingId(int bookingId) {
        String sql = "SELECT passenger_id FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("passenger_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting passenger ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 7. Get seats booked by booking ID
    public Integer getSeatsByBookingId(int bookingId) {
        String sql = "SELECT seats_booked FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("seats_booked");
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 8. Get status by booking ID
    public String getStatusByBookingId(int bookingId) {
        String sql = "SELECT status FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            System.err.println("Error getting status: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 9. Get booking count for a passenger
    public int getBookingCountByPassenger(int passengerId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE passenger_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // 10. Get booking count for a ride
    public int getBookingCountByRide(int rideId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE ride_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rideId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // 11. Get pending booking count for driver
    public int getPendingBookingCountByDriver(int driverId) {
        String sql = "SELECT COUNT(*) FROM bookings b " +
                     "JOIN rides r ON b.ride_id = r.id " +
                     "WHERE r.driver_id = ? AND b.status = 'REQUESTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, driverId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending booking count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // 12. Get booking ID for passenger and ride (used in controller!)
    public Integer getBookingId(int passengerId, int rideId) {
        String sql = "SELECT id FROM bookings WHERE passenger_id = ? AND ride_id = ? " +
                "AND status IN ('REQUESTED', 'CONFIRMED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerId);
            pstmt.setInt(2, rideId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 13. Delete booking
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


