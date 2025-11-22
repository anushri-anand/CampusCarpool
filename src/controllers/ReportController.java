package controllers;

import models.Report;
import services.ReportService;
import utils.NotificationCenter;

import java.util.List;

/**

* ReportController handles all reporting-related operations between the view and ReportService
  */
  public class ReportController {

  private ReportService reportService;
  private int currentUserId;

  public ReportController(ReportService reportService, int currentUserId) {
  this.reportService = reportService;
  this.currentUserId = currentUserId;
  }

  /**

  * Submit a report against a user
  * @param reportedUserId the user being reported
  * @param rideId optional ride ID
  * @param reason reason for reporting
  * @return true if submission successful
    */
    public boolean submitReport(int reportedUserId, Integer rideId, String reason) {
    if (reason == null || reason.trim().isEmpty()) {
    NotificationCenter.showError("Please provide a reason for reporting.");
    return false;
    }

    boolean success = reportService.submitReport(currentUserId, reportedUserId, rideId, reason);
    if (success) {
    NotificationCenter.showInfo("Report submitted successfully.");
    } else {
    NotificationCenter.showError("Failed to submit report.");
    }
    return success;
    }

  /**

  * Get all reports filed by current user
    */
    public List<Report> getMyReports() {
    return reportService.getReportsByUser(currentUserId);
    }

  /**

  * Get all reports against a specific user
  * @param userId the user being checked
    */
    public List<Report> getReportsAgainstUser(int userId) {
    return reportService.getReportsForUser(userId);
    }

  /**

  * Get total report count against a user
    */
    public int getReportCount(int userId) {
    return reportService.getReportCount(userId);
    }
    }
