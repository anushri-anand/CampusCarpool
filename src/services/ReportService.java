package services;

import models.Report;
import models.User;
import dao.ReportDAO;
import dao.UserDAO;

import java.util.List;

public class ReportService {
    
    private ReportDAO reportDAO;
    private UserDAO userDAO;
    
    public ReportService() {
        this.reportDAO = new ReportDAO();
        this.userDAO = new UserDAO();
    }

    public boolean submitReport(int reporterId, int reportedUserId, 
                               Integer rideId, String reason) {
        if (reporterId == reportedUserId) {
            System.err.println("Cannot report yourself");
            return false;
        }

        if (reason == null || reason.trim().isEmpty()) {
            System.err.println("Reason is required");
            return false;
        }

        if (userDAO.getUserById(reporterId) == null) {
            System.err.println("Reporter not found");
            return false;
        }
        if (userDAO.getUserById(reportedUserId) == null) {
            System.err.println("Reported user not found");
            return false;
        }

        boolean success = reportDAO.createReport(reporterId, reportedUserId, rideId, reason);
        
        if (success) {
            System.out.println("Report submitted successfully. Our team will review it.");
            int reportCount = reportDAO.getReportCountForUser(reportedUserId);
            if (reportCount >= 3) {
                addWarningToUser(reportedUserId);
            }
        }
        
        return success;
    }

    public List<Report> getPendingReports() {
        return reportDAO.getPendingReports();
    }

    public List<Report> getReportsForUser(int userId) {
        return reportDAO.getReportsByReportedUser(userId);
    }

    public List<Report> getReportsByUser(int userId) {
        return reportDAO.getReportsByReporter(userId);
    }

    public boolean reviewReport(int reportId, boolean approved, String adminNotes) {
        Report report = reportDAO.getReportById(reportId);
        if (report == null) {
            System.err.println("Report not found");
            return false;
        }

        if (approved) {
            addWarningToUser(report.getReportedUserId());
            reportDAO.updateReportStatus(reportId, "RESOLVED");
        } else {
            reportDAO.updateReportStatus(reportId, "REVIEWED");
        }

        System.out.println("Report reviewed. Status: " + (approved ? "Approved" : "Rejected"));
        return true;
    }

    private void addWarningToUser(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            user.addWarning();
            userDAO.updateUser(user);
            
            if (user.isBlacklisted()) {
                System.out.println("User " + userId + " has been blacklisted until: " + 
                                 user.getBlacklistUntil());
            }
        }
    }

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

    public boolean removeBlacklist(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }

        user.clearWarnings();
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            System.out.println("User blacklist removed and warnings cleared");
        }
        
        return success;
    }

    public int getReportCount(int userId) {
        return reportDAO.getReportCountForUser(userId);
    }
}
