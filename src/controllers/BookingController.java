package controllers;

import models.Ride;
import models.User;
import services.BookingService;
import services.RideService;
import utils.NotificationCenter;
import views.BookingView;
import java.util.List;
  public class BookingController {

  private BookingService bookingService;
  private RideService rideService;
  private BookingView bookingView;
  private User currentUser;


  public BookingController(BookingService bookingService, RideService rideService, User currentUser) {
  this.bookingService = bookingService;
  this.rideService = rideService;
  this.currentUser = currentUser;

  }

  public void setView(BookingView view) {
  this.bookingView = view;
  }

  public void loadAvailableRides() {
  List<Ride> rides = rideService.getAllActiveRides();
  if (bookingView != null) bookingView.showRides(rides);
  }

  public boolean bookSeat(Ride ride, User user, int seats) {
  if (user == null) {
  NotificationCenter.showError("User not logged in.");
  return false;
  }
  boolean success = bookingService.bookRide(user.getId(), ride.getId(), seats);
  if (success) {
  NotificationCenter.showInfo("Successfully booked " + seats + " seat(s)!");
  loadMyBookings();
  } else {
  NotificationCenter.showError("Failed to book ride. Check availability.");
  }
  return success;
  }

  public boolean cancelBooking(Ride ride, User user) {
  if (user == null) {
  NotificationCenter.showError("User not logged in.");
  return false;
  }
  int bookingId = bookingService.getBookingId(user.getId(), ride.getId());
  if (bookingId <= 0) {
  NotificationCenter.showError("No booking found for this ride.");
  return false;
  }
  boolean success = bookingService.cancelBooking(bookingId, user.getId());
  if (success) {
  NotificationCenter.showInfo("Booking cancelled successfully.");
  loadMyBookings();
  } else {
  NotificationCenter.showError("Failed to cancel booking.");
  }
  return success;
  }

  public void loadMyBookings() {
  List<Ride> bookedRides = rideService.getRidesBookedByPassenger(currentUser.getId());
  if (bookingView != null) bookingView.showMyBookings(bookedRides);
  }

  public boolean hasBookedRide(int rideId) {
  return bookingService.hasPassengerBookedRide(currentUser.getId(), rideId);
  }

  public int getBookingId(int rideId) {
  return bookingService.getBookingId(currentUser.getId(), rideId);
  }

  public void showMessage(String message) {
  if (bookingView != null) bookingView.showMessage(message);
  }

  public void showError(String error) {
  if (bookingView != null) bookingView.showError(error);
  }

  }

