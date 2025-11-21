package views;

import models.Ride;
import models.User;
import services.BookingService;
import utils.NotificationCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * BookingView allows passengers to see available rides and book them.
 */
public class BookingView extends JFrame {

    private JTable ridesTable;
    private JButton btnBook, btnRefresh, btnBack;
    private DefaultTableModel tableModel;
    private User passenger;
    private BookingService bookingService;

    public BookingView(User passenger) {
        this.passenger = passenger;
        this.bookingService = new BookingService(); // connects to DAO

        setTitle("CampusCarpool - Book Ride");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table columns
        String[] columns = {"Ride ID", "Driver", "Origin", "Destination", "Date", "Time", "Seats Available", "Price"};

        tableModel = new DefaultTableModel(columns, 0);
        ridesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ridesTable);

        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        btnBook = new JButton("Book Selected Ride");
        btnRefresh = new JButton("Refresh");
        btnBack = new JButton("Back");

        buttonPanel.add(btnBook);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        btnRefresh.addActionListener(e -> loadRides());
        btnBook.addActionListener(e -> bookSelectedRide());
        btnBack.addActionListener(e -> dispose());

        loadRides();
    }

    // Load rides into table
    private void loadRides() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Ride> rides = bookingService.getAvailableRides();

        for (Ride ride : rides) {
            tableModel.addRow(new Object[]{
                    ride.getId(),
                    ride.getDriverName(),
                    ride.getOrigin(),
                    ride.getDestination(),
                    ride.getDepartureDate(),
                    ride.getDepartureTime(),
                    ride.getSeatsAvailable(),
                    ride.getPricePerSeat()
            });
        }
    }

    // Book selected ride
    private void bookSelectedRide() {
        int selectedRow = ridesTable.getSelectedRow();
        if (selectedRow == -1) {
            NotificationCenter.showWarning("Please select a ride to book!");
            return;
        }

        int rideId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = bookingService.bookRide(passenger.getId(), rideId);

        if (success) {
            NotificationCenter.showInfo("Ride booked successfully!");
            loadRides(); // refresh table
        } else {
            NotificationCenter.showError("Failed to book ride. Maybe no seats available.");
        }
    }
}
