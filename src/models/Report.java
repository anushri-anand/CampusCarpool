package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {
   
    private int id;
    private int reportedBy;
    private int reportedUserId;
    private Integer rideId; 
    private String reason;
    private String status; 
    private LocalDateTime timestamp;

    public Report() {
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }
    public Report(int reportedBy, int reportedUserId, Integer rideId, String reason) {
        this.reportedBy = reportedBy;
        this.reportedUserId = reportedUserId;
        this.rideId = rideId;
        this.reason = reason;
        this.status = "PENDING";
        this.timestamp = LocalDateTime.now();
    }

    public Report(int id, int reportedBy, int reportedUserId, Integer rideId, 
                  String reason, String status, LocalDateTime timestamp) {
        this.id = id;
        this.reportedBy = reportedBy;
        this.reportedUserId = reportedUserId;
        this.rideId = rideId;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(int reportedBy) {
        this.reportedBy = reportedBy;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    public boolean isPending() {
        return status.equals("PENDING");
    }

    public boolean isReviewed() {
        return status.equals("REVIEWED");
    }

    public boolean isResolved() {
        return status.equals("RESOLVED");
    }

    public void markAsReviewed() {
        this.status = "REVIEWED";
    }

    public void markAsResolved() {
        this.status = "RESOLVED";
    }

    public boolean hasAssociatedRide() {
        return rideId != null;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return timestamp.format(formatter);
    }

    public String getSummary() {
        String rideInfo = hasAssociatedRide() ? " (Ride ID: " + rideId + ")" : "";
        return "Report against User " + reportedUserId + rideInfo + " - " + status;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportedBy=" + reportedBy +
                ", reportedUser=" + reportedUserId +
                ", rideId=" + (rideId != null ? rideId : "N/A") +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + getFormattedTimestamp() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Report report = (Report) obj;
        return id == report.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
