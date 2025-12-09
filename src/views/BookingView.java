package views;

import controllers.BookingController;
import models.Ride;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookingView extends JFrame {

    private BookingController controller;

    private JTextArea textArea;
    private JButton btnSearch, btnMyBookings, btnBack;

    public BookingView(BookingController controller) {
        this.controller = controller;
        controller.setView(this);

        initUI();
        controller.loadAvailableRides();
    }

    private void initUI() {
        setTitle("Ride Booking");
        setSize(600, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        btnSearch = new JButton("Search Rides");
        btnMyBookings = new JButton("My Bookings");
        btnBack = new JButton("Back");

        buttonPanel.add(btnSearch);
        buttonPanel.add(btnMyBookings);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> controller.loadAvailableRides());
        btnMyBookings.addActionListener(e -> controller.loadMyBookings());
        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    public void showRides(List<Ride> rides) {
        textArea.setText("");
        if (rides == null || rides.isEmpty()) {
            textArea.setText("No rides available.\n");
            return;
        }

        for (Ride r : rides) {
            textArea.append(
                    "Ride #" + r.getId() + "\n"
                    + "Driver: " + r.getDriverName() + "\n"
                    + "Route: " + r.getRouteDescription() + "\n"
                    + "Departure: " + r.getFormattedDepartureDateTime() + "\n"
                    + "Seats: " + r.getSeatsAvailable() + "/" + r.getSeatsTotal() + "\n"
                    + "Price: AED " + r.getPricePerSeat() + "\n"
                    + "----------------------------------------\n"
            );
        }
    }

    public void showMyBookings(List<Ride> bookings) {
        textArea.setText("");
        if (bookings == null || bookings.isEmpty()) {
            textArea.setText("You have no active bookings.\n");
            return;
        }

        for (Ride r : bookings) {
            textArea.append(
                    "BOOKED RIDE #" + r.getId() + "\n"
                    + r.getRouteDescription() + " — " + r.getFormattedDepartureDateTime() + "\n"
                    + "----------------------------------------\n"
            );
        }
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showError(String err) {
        JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
