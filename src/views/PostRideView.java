package views;

import models.Ride;
import services.RideService;
import utils.NotificationCenter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class PostRideView extends JFrame {

    private RideService rideService;
    private int driverId;
    private String driverName;

    private JTextField originField;
    private JTextField destinationField;
    private JTextField vehicleField;
    private JTextField seatsField;
    private JTextField priceField;
    private JTextField dateField;
    private JTextField timeField;

    public PostRideView(int driverId, String driverName) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.rideService = new RideService();

        setTitle("Post a Ride - Campus Carpool");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(10, 2, 8, 8));

        add(new JLabel("Origin:"));
        originField = new JTextField();
        add(originField);

        add(new JLabel("Destination:"));
        destinationField = new JTextField();
        add(destinationField);

        add(new JLabel("Departure Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Departure Time (HH:MM):"));
        timeField = new JTextField();
        add(timeField);

        add(new JLabel("Total Seats:"));
        seatsField = new JTextField();
        add(seatsField);

        add(new JLabel("Price per Seat (AED):"));
        priceField = new JTextField();
        add(priceField);

        add(new JLabel("Vehicle Info (e.g., Tesla Model 3):"));
        vehicleField = new JTextField();
        add(vehicleField);

        JButton postBtn = new JButton("Post Ride");
        postBtn.addActionListener(e -> handlePostRide());
        add(postBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        add(clearBtn);
    }

    private void handlePostRide() {
        try {
            String origin = originField.getText();
            String destination = destinationField.getText();
            LocalDate date = LocalDate.parse(dateField.getText());
            LocalTime time = LocalTime.parse(timeField.getText());
            int seats = Integer.parseInt(seatsField.getText());
            double price = Double.parseDouble(priceField.getText());
            String vehicle = vehicleField.getText();

            if (origin.isEmpty() || destination.isEmpty() || vehicle.isEmpty()) {
                NotificationCenter.showWarning("Please complete all fields.");
                return;
            }

            Ride ride = new Ride(
                    0, driverId, driverName, origin, destination,
                    date, time, seats, seats, price, "ACTIVE", vehicle
            );

            boolean success = rideService.createRide(ride);

            if (success) {
                NotificationCenter.showInfo("Ride posted successfully!");
                clearFields();
            } else {
                NotificationCenter.showError("Failed to post ride.");
            }

        } catch (Exception ex) {
            NotificationCenter.showError("Invalid input. Check values and try again.");
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        originField.setText("");
        destinationField.setText("");
        dateField.setText("");
        timeField.setText("");
        seatsField.setText("");
        priceField.setText("");
        vehicleField.setText("");
    }

    public void display() {
        setVisible(true);
    }
}
