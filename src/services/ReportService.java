package services;

import models.Report;
import models.User;
import dao.ReportDAO;
import dao.UserDAO;

import java.util.List;

/**
 * ReportService handles user reports and moderation
 * Demonstrates: Safety Features, Moderation, Admin Functions
 */
public class ReportService {
    
    private ReportDAO reportDAO;
    private UserDAO userDAO;
    
    public ReportService() {
        this.reportDAO = new ReportDAO();
        this.userDAO = new UserDAO();
    }

    // ========================================
    // REPORTING
    // ========================================

    /**
     * Submit a report against a user
     * @param reporterId user submitting the report
     * @param reportedUserId user being reported
     * @param rideId associated ride (optional, can be null)
     * @param reason reason for report
     * @return true if successful
     */
    public boolean submitReport(int reporterId, int reportedUserId, 
                               Integer rideId, String reason) {
        // Validate inputs
        if (reporterId == reportedUserId) {
            System.err.println("Cannot report yourself");
            return false;
        }

        if (reason == null || reason.trim().isEmpty()) {
            System.err.println("Reason is required");
            return false;
        }

        // Check if users exist
        if (userDAO.getUserById(reporterId) == null) {
            System.err.println("Reporter not found");
            return false;
        }
        if (userDAO.getUserById(reportedUserId) == null) {
            System.err.println("Reported user not found");
            return false;
        }

        // Create report
        boolean success = reportDAO.createReport(reporterId, reportedUserId, rideId, reason);
        
        if (success) {
            System.out.println("Report submitted successfully. Our team will review it.");
            
            // Auto-warning system: if user has 3+ reports, add warning
            int reportCount = reportDAO.getReportCountForUser(reportedUserId);
            if (reportCount >= 3) {
                addWarningToUser(reportedUserId);
            }
        }
        
        return success;
    }

    /**
     * Get all pending reports (admin view)
     * @return list of pending reports
     */
    public List<Report> getPendingReports() {
        return reportDAO.getPendingReports();
    }

    /**
     * Get reports against a specific user
     * @param userId user ID
     * @return list of reports
     */
    public List<Report> getReportsForUser(int userId) {
        return reportDAO.getReportsByReportedUser(userId);
    }

    /**
     * Get reports submitted by a user
     * @param userId user ID
     * @return list of reports
     */
    public List<Report> getReportsByUser(int userId) {
        return reportDAO.getReportsByReporter(userId);
    }

    // ========================================
    // MODERATION (Admin Functions)
    // ========================================

    /**
     * Review and resolve a report
     * @param reportId report ID
     * @param approved true if report is valid
     * @param adminNotes admin's notes
     * @return true if successful
     */
    public boolean reviewReport(int reportId, boolean approved, String adminNotes) {
        Report report = reportDAO.getReportById(reportId);
        if (report == null) {
            System.err.println("Report not found");
            return false;
        }

        if (approved) {
            // Add warning to reported user
            addWarningToUser(report.getReportedUserId());
            reportDAO.updateReportStatus(reportId, "RESOLVED");
        } else {
            reportDAO.updateReportStatus(reportId, "REVIEWED");
        }

        System.out.println("Report reviewed. Status: " + (approved ? "Approved" : "Rejected"));
        return true;
    }

    /**
     * Add warning to a user
     * @param userId user to warn
     */
    private void addWarningToUser(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            user.addWarning(); // This also triggers blacklist if warnings >= 3
            userDAO.updateUser(user);
            
            if (user.isBlacklisted()) {
                System.out.println("User " + userId + " has been blacklisted until: " + 
                                 user.getBlacklistUntil());
            }
        }
    }

    /**
     * Manually blacklist a user (admin function)
     * @param userId user to blacklist
     * @param days number of days
     * @return true if successful
     */
    public boolean blacklistUser(int userId, int days) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        user.setBlacklistUntil(java.time.LocalDateTime.now().plusDays(days));
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            System.out.println("User blacklisted for " + days + " days");
        }
        
        return success;
    }

    /**
     * Remove blacklist from user (admin function)
     * @param userId user to unblacklist
     * @return true if successful
     */
    public boolean removeBlacklist(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        user.clearWarnings(); // Also clears blacklist
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            System.out.println("User blacklist removed and warnings cleared");
        }
        
        return success;
    }

    /**
     * Get count of reports for a user
     * @param userId user ID
     * @return number of reports
     */
    public int getReportCount(int userId) {
        return reportDAO.getReportCountForUser(userId);
    }
}
