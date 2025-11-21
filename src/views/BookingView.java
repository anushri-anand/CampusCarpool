package views;

import models.Ride;
import services.BookingService;
import utils.NotificationCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookingView extends JFrame {

    private BookingService bookingService;
    private JTable rideTable;
    private DefaultTableModel tableModel;
    private int loggedInPassengerId;

    public BookingView(int passengerId) {
        this.loggedInPassengerId = passengerId;
        this.bookingService = new BookingService();

        setTitle("Book a Ride - Campus Carpool");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadAvailableRides();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Driver", "From", "To", "Date", "Time", "Seats", "Price (AED)", "Vehicle"},
                0
        );
        rideTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rideTable);

        // Book button
        JButton bookBtn = new JButton("Book Selected Ride");
        bookBtn.addActionListener(e -> handleBooking());

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAvailableRides());

        JPanel btnPanel = new JPanel();
        btnPanel.add(bookBtn);
        btnPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadAvailableRides() {
        tableModel.setRowCount(0); // Clear previous rows

        try {
            List<Ride> rides = bookingService.getAvailableRides();
            for (Ride r : rides) {
                tableModel.addRow(new Object[]{
                        r.getId(), r.getDriverName(), r.getOrigin(), r.getDestination(),
                        r.getDepartureDate(), r.getDepartureTime(),
                        r.getSeatsAvailable(), r.getPricePerSeat(), r.getVehicleInfo()
                });
            }
        } catch (Exception ex) {
            NotificationCenter.showError("Failed to load rides.");
            ex.printStackTrace();
        }
    }

    private void handleBooking() {
        int selectedRow = rideTable.getSelectedRow();
        if (selectedRow == -1) {
            NotificationCenter.showWarning("Please select a ride to book.");
            return;
        }

        int rideId = (int) tableModel.getValueAt(selectedRow, 0);

        String seatsInput = JOptionPane.showInputDialog(this, "Enter number of seats to book:", "1");
        if (seatsInput == null) return; // cancel
        int seats;

        try {
            seats = Integer.parseInt(seatsInput);
        } catch (Exception e) {
            NotificationCenter.showError("Enter valid seat count.");
            return;
        }

        boolean success = bookingService.bookRide(loggedInPassengerId, rideId, seats);

        if (success) {
            loadAvailableRides(); // refresh seat count
        }
    }

    public void display() {
        setVisible(true);
    }
}
