package views;

import models.Ride;
import models.User;
import controllers.BookingController;

import javax.swing.*;
import java.awt.*;

public class RideDetailsView extends JFrame {

    private Ride ride;
    private User currentUser;
    private BookingController controller;

    private JLabel lblDriver, lblRoute, lblDateTime, lblSeats, lblPrice, lblStatus, lblVehicle;
    private JButton btnBook, btnCancel, btnBack;

    public RideDetailsView(Ride ride, User currentUser, BookingController controller) {
        this.ride = ride;
        this.currentUser = currentUser;
        this.controller = controller;

        initUI();
    }

    private void initUI() {
        setTitle("Ride Details");
        setSize(450, 350);
        setLayout(new GridLayout(10, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        lblDriver = new JLabel("Driver: " + ride.getDriverName());
        lblRoute = new JLabel("Route: " + ride.getRouteDescription());
        lblDateTime = new JLabel("Departure: " + ride.getFormattedDepartureDateTime());
        lblSeats = new JLabel("Seats: " + ride.getSeatsAvailable() + "/" + ride.getSeatsTotal());
        lblPrice = new JLabel("Price per seat: ₹" + ride.getPricePerSeat());
        lblStatus = new JLabel("Status: " + ride.getStatus());
        lblVehicle = new JLabel("Vehicle: " + ride.getVehicleInfo());

        btnBook = new JButton("Book Seat");
        btnCancel = new JButton("Cancel Booking");
        btnBack = new JButton("Back");

        add(lblDriver);
        add(lblRoute);
        add(lblDateTime);
        add(lblSeats);
        add(lblPrice);
        add(lblStatus);
        add(lblVehicle);
        add(btnBook);
        add(btnCancel);
        add(btnBack);

        // disable buttons based on state
        btnBook.setEnabled(ride.hasAvailableSeats() && !ride.isPassengerBooked(currentUser.getId()));
        btnCancel.setEnabled(ride.isPassengerBooked(currentUser.getId()));

        // listeners
        btnBook.addActionListener(e -> {
            boolean success = controller.bookSeat(ride, currentUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "Seat booked successfully!");
                refreshUI();
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> {
            boolean success = controller.cancelBooking(ride, currentUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking canceled!");
                refreshUI();
            } else {
                JOptionPane.showMessageDialog(this, "Could not cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void refreshUI() {
        lblSeats.setText("Seats: " + ride.getSeatsAvailable() + "/" + ride.getSeatsTotal());
        btnBook.setEnabled(ride.hasAvailableSeats() && !ride.isPassengerBooked(currentUser.getId()));
        btnCancel.setEnabled(ride.isPassengerBooked(currentUser.getId()));
    }
}
