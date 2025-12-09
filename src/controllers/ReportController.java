package controllers;

import models.Report;
import services.ReportService;
import utils.NotificationCenter;
import java.util.List;

  public class ReportController {

  private ReportService reportService;
  private int currentUserId;

  public ReportController(ReportService reportService, int currentUserId) {
  this.reportService = reportService;
  this.currentUserId = currentUserId;
  }

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
    public List<Report> getMyReports() {
    return reportService.getReportsByUser(currentUserId);
    }

    public List<Report> getReportsAgainstUser(int userId) {
    return reportService.getReportsForUser(userId);
    }

    public int getReportCount(int userId) {
    return reportService.getReportCount(userId);
    }
    }
