package models;

import java.time.LocalDateTime;

/**
 * Report class for handling user complaints.
 * Demonstrates: Encapsulation, Associations, Exception Handling, Business Rules
 */
public class Report {

    private int reportId;
    private User reportedBy;
    private User reportedUser;
    private String reason;
    private LocalDateTime timestamp;

    // Constructors

    public Report() {
        this.timestamp = LocalDateTime.now();
    }

    public Report(int reportId, User reportedBy, User reportedUser, String reason) {
        this.reportId = reportId;
        this.reportedBy = reportedBy;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Business Logic

    /** Adds a warning to the reported user */
    public void applyWarning() {
        if (reportedUser != null) {
            reportedUser.addWarning();
        }
    }

    /** Check if reported user is newly blacklisted */
    public boolean checkBlacklist() {
        return reportedUser != null && reportedUser.isBlacklisted();
    }

    // Overrides

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", reportedBy='" + (reportedBy != null ? reportedBy.getName() : "N/A") + '\'' +
                ", reportedUser='" + (reportedUser != null ? reportedUser.getName() : "N/A") + '\'' +
                ", reason='" + reason + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
