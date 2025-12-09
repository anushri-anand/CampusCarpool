package dao;

import models.Report;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public boolean createReport(int reportedBy, int reportedUser, Integer rideId, String reason) {
        String sql = "INSERT INTO reports (reported_by, reported_user, ride_id, reason, status) " +
                     "VALUES (?, ?, ?, ?, 'PENDING')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reportedBy);
            pstmt.setInt(2, reportedUser);
            if (rideId != null) {
                pstmt.setInt(3, rideId);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, reason);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public Report getReportById(int reportId) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractReportFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting report: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Report> getPendingReports() {
        String sql = "SELECT * FROM reports WHERE status = 'PENDING' ORDER BY timestamp DESC";
        List<Report> reports = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting pending reports: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reports;
    }

    public List<Report> getReportsByReportedUser(int userId) {
        String sql = "SELECT * FROM reports WHERE reported_user = ? ORDER BY timestamp DESC";
        List<Report> reports = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reports by reported user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reports;
    }


    public List<Report> getReportsByReporter(int userId) {
        String sql = "SELECT * FROM reports WHERE reported_by = ? ORDER BY timestamp DESC";
        List<Report> reports = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reports by reporter: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reports;
    }

    public int getReportCountForUser(int userId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE reported_user = ? AND status IN ('PENDING', 'RESOLVED')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting report count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }

    public boolean updateReportStatus(int reportId, String status) {
        String sql = "UPDATE reports SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating report status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Report extractReportFromResultSet(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getInt("id"));
        report.setReportedBy(rs.getInt("reported_by"));
        report.setReportedUserId(rs.getInt("reported_user"));
        
        int rideId = rs.getInt("ride_id");
        if (!rs.wasNull()) {
            report.setRideId(rideId);
        }
        
        report.setReason(rs.getString("reason"));
        report.setStatus(rs.getString("status"));
        
        String timestampStr = rs.getString("timestamp");
        if (timestampStr != null) {
            report.setTimestamp(LocalDateTime.parse(timestampStr));
        }
        
        return report;
    }

    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting report: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

