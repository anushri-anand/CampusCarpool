package views;

import controllers.RideController;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DashboardView extends JFrame {
    
    private RideController controller;
    private User currentUser;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel homePanel;
    private JPanel searchPanel;
    private JPanel profilePanel;
    private JLabel welcomeLabel;
    private JLabel statsLabel;
    private JButton logoutButton;
    private Image scaledImage;
    
    public DashboardView(RideController controller) {
        this.controller = controller;
        this.currentUser = controller.getCurrentUser();
        initializeUI();
        loadDashboardData();
    }
    
    private void initializeUI() {
        ImageIcon icon = new ImageIcon("src/images/pic.png");
Image img = icon.getImage();
scaledImage = img.getScaledInstance(400, 600, Image.SCALE_SMOOTH);  
        setTitle("CampusCarpool - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
createHomePage();

if (currentUser.getRole().equals("PASSENGER") || currentUser.getRole().equals("BOTH")) {
    createSearchPage(); 
}

switch (currentUser.getRole()) {
    case "DRIVER":
        createDriverPages();      
        break;
    case "PASSENGER":
        createPassengerPages();  
        break;
    case "BOTH":
        createDriverPages();      
        createPassengerPages();  
        break;
    default:
        break;
}
createProfilePage();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 128, 141));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        statsLabel = new JLabel(getUserQuickStats());
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(e -> handleLogout());
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        leftPanel.add(welcomeLabel);
        leftPanel.add(statsLabel);
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);
        return panel;
    }
    
private void createHomePage() {
    homePanel = new JPanel(new BorderLayout(10, 10));
    homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    JLabel titleLabel = new JLabel("Dashboard Overview");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

    // Initialize infoPanel first
    JPanel infoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(scaledImage, 0, 0, getWidth(), getHeight(), this);
        }
    };
    infoPanel.setPreferredSize(new Dimension(400, 600));

    JPanel actionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
    actionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
    if (currentUser.getRole().equals("DRIVER") || currentUser.getRole().equals("BOTH")) {
        JButton postRideBtn = new JButton("Post a Ride");
        postRideBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        actionsPanel.add(postRideBtn);
    }
    if (currentUser.getRole().equals("PASSENGER") || currentUser.getRole().equals("BOTH")) {
        JButton searchRideBtn = new JButton("Search Rides");
        searchRideBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        actionsPanel.add(searchRideBtn);
        JButton postRequestBtn = new JButton("Post Ride Request");
        postRequestBtn.addActionListener(e -> showPostRequestDialog());
        actionsPanel.add(postRequestBtn);
    }

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 20));
    centerPanel.add(infoPanel);
    centerPanel.add(actionsPanel);

    homePanel.add(topPanel, BorderLayout.NORTH);
    homePanel.add(centerPanel, BorderLayout.CENTER);

    tabbedPane.addTab("Home", homePanel);
}

    
    private void createSearchPage() {
        searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel destLabel = new JLabel("Destination:");
        JComboBox<String> destCombo = new JComboBox<>(getDubaiDestinations());
        destCombo.setPreferredSize(new Dimension(200, 30));
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All Rides");
        searchControls.add(destLabel);
        searchControls.add(destCombo);
        searchControls.add(searchBtn);
        searchControls.add(showAllBtn);
        String[] columns = {"ID", "Driver", "Route", "Date & Time", "Seats", "Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookBtn = new JButton("Book Selected Ride");
        JButton viewDetailsBtn = new JButton("View Details");
        actionPanel.add(viewDetailsBtn);
        actionPanel.add(bookBtn);
        searchBtn.addActionListener(e -> {
            String destination = (String) destCombo.getSelectedItem();
            List<Ride> rides = controller.searchRidesByDestination(destination);
            updateRidesTable(tableModel, rides);
        });
        showAllBtn.addActionListener(e -> {
            List<Ride> rides = controller.searchAllRides();
            updateRidesTable(tableModel, rides);
        });
        bookBtn.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int rideId = (int) tableModel.getValueAt(selectedRow, 0);
                showBookRideDialog(rideId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a ride to book");
            }
        });
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int rideId = (int) tableModel.getValueAt(selectedRow, 0);
                showRideDetails(rideId);
            }
        });
        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(scrollPane, BorderLayout.CENTER);
        searchPanel.add(actionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Search Rides", searchPanel);
        List<Ride> rides = controller.searchAllRides();
        updateRidesTable(tableModel, rides);
    }
    
    private void createDriverPages() {
        JPanel driverPanel = new JPanel(new BorderLayout(10, 10));
        driverPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Post a New Ride"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Origin:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> originCombo = new JComboBox<>(getDubaiDestinations());
        formPanel.add(originCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> destCombo = new JComboBox<>(getDubaiDestinations());
        formPanel.add(destCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString(), 15);
        formPanel.add(dateField, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        JTextField timeField = new JTextField("09:00", 15);
        formPanel.add(timeField, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Seats Available:"), gbc);
        gbc.gridx = 1;
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
        formPanel.add(seatsSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Price per Seat (AED):"), gbc);
        gbc.gridx = 1;
        JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 100.0, 1.0));
        formPanel.add(priceSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton postBtn = new JButton("Post Ride");
        postBtn.setPreferredSize(new Dimension(200, 35));
        formPanel.add(postBtn, gbc);
        String[] columns = {"ID", "Route", "Date & Time", "Seats", "Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable myRidesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(myRidesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My Posted Rides"));
        postBtn.addActionListener(e -> {
            try {
                String origin = (String) originCombo.getSelectedItem();
                String destination = (String) destCombo.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime time = LocalTime.parse(timeField.getText());
                int seats = (int) seatsSpinner.getValue();
                double price = (double) priceSpinner.getValue();
                if (controller.postRide(origin, destination, date, time, seats, price)) {
                    JOptionPane.showMessageDialog(this, "Ride posted successfully!");
                    refreshMyRides(tableModel);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to post ride", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        driverPanel.add(formPanel, BorderLayout.NORTH);
        driverPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Post Ride", driverPanel);
        refreshMyRides(tableModel);
    }
    
    private void createPassengerPages() {
        JPanel passengerPanel = new JPanel(new BorderLayout(10, 10));
        passengerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] columns = {"Ride ID", "Driver", "Route", "Date & Time", "Seats", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable bookingsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My Booked Rides"));
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel Booking");
        actionPanel.add(cancelBtn);
        cancelBtn.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int rideId = (int) tableModel.getValueAt(selectedRow, 0);
                int bookingId = controller.getBookingId(currentUser.getId(), rideId);
                if (bookingId != -1) {
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to cancel this booking?", 
                        "Confirm Cancellation", 
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.cancelBooking(bookingId)) {
                            JOptionPane.showMessageDialog(this, "Booking cancelled successfully!");
                            refreshMyBookings(tableModel);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel");
            }
        });
        passengerPanel.add(scrollPane, BorderLayout.CENTER);
        passengerPanel.add(actionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("My Bookings", passengerPanel);
        refreshMyBookings(tableModel);
    }
    
    private void createProfilePage() {
        profilePanel = new JPanel(new BorderLayout(10, 10));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextArea profileInfo = new JTextArea(controller.getUserStatistics());
        profileInfo.setEditable(false);
        profileInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(profileInfo);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton changePasswordBtn = new JButton("Change Password");
        buttonPanel.add(changePasswordBtn);
        changePasswordBtn.addActionListener(e -> showChangePasswordDialog());
        profilePanel.add(scrollPane, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Profile", profilePanel);
    }
    
    private String[] getDubaiDestinations() {
        return new String[]{
            "BPDC Campus","Dragon Mart","City Centre Mirdif","Dubai Mall",
            "Mall of the Emirates","Ibn Battuta Mall","Dubai Festival City Mall",
            "JBR","Dubai Marina","JLT","City Walk","Global Village"
        };
    }
    
    private String getUserQuickStats() {
        return String.format("Role: %s",currentUser.getRole());
    }
    
    private void updateRidesTable(DefaultTableModel model, List<Ride> rides) {
        model.setRowCount(0);
        for (Ride ride : rides) {
            model.addRow(new Object[]{
                ride.getId(), ride.getDriverName(), ride.getRouteDescription(),
                ride.getFormattedDepartureDateTime(), ride.getSeatsAvailable() + "/" + ride.getSeatsTotal(),
                "AED " + ride.getPricePerSeat(), ride.getStatus()
            });
        }
    }
    
    private void refreshMyRides(DefaultTableModel model) {
        model.setRowCount(0);
        List<Ride> rides = controller.getMyPostedRides();
        if (rides != null) {
            for (Ride ride : rides) {
                model.addRow(new Object[]{
                    ride.getId(), ride.getRouteDescription(), ride.getFormattedDepartureDateTime(),
                    ride.getSeatsAvailable() + "/" + ride.getSeatsTotal(),
                    "AED " + ride.getPricePerSeat(), ride.getStatus()
                });
            }
        }
    }
    
    private void refreshMyBookings(DefaultTableModel model) {
        model.setRowCount(0);
        List<Ride> rides = controller.getMyBookedRides();
        if (rides != null) {
            for (Ride ride : rides) {
                model.addRow(new Object[]{
                    ride.getId(), ride.getDriverName(), ride.getRouteDescription(),
                    ride.getFormattedDepartureDateTime(), ride.getSeatsAvailable() + "/" + ride.getSeatsTotal(),
                    ride.getStatus()
                });
            }
        }
    }
    
    private void showBookRideDialog(int rideId) {
        Ride ride = controller.getRideById(rideId);
        if (ride == null) {
            JOptionPane.showMessageDialog(this, "Ride not found");
            return;
        }
        String[] options = new String[ride.getSeatsAvailable()];
        for (int i = 0; i < options.length; i++) options[i] = String.valueOf(i + 1);
        String selected = (String) JOptionPane.showInputDialog(this, "Select number of seats to book:", "Book Ride", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (selected != null) {
            int seats = Integer.parseInt(selected);
            if (controller.bookRide(rideId, seats)) {
                JOptionPane.showMessageDialog(this, "Booking successful!");
                int bookingsTabIndex = tabbedPane.indexOfTab("My Bookings");
                if (bookingsTabIndex != -1) {
                    JPanel bookingsPanel = (JPanel) tabbedPane.getComponentAt(bookingsTabIndex);
                    JScrollPane scrollPane = (JScrollPane) bookingsPanel.getComponent(0);
                    JTable bookingsTable = (JTable) scrollPane.getViewport().getView();
                    refreshMyBookings((DefaultTableModel) bookingsTable.getModel());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRideDetails(int rideId) {
        Ride ride = controller.getRideById(rideId);
        if (ride != null) {
            String details = controller.formatRideDisplay(ride);
            JOptionPane.showMessageDialog(this, details, "Ride Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showPostRequestDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JComboBox<String> originCombo = new JComboBox<>(getDubaiDestinations());
        JComboBox<String> destCombo = new JComboBox<>(getDubaiDestinations());
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString());
        JTextField timeField = new JTextField("09:00");
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField notesField = new JTextField();
        panel.add(new JLabel("Origin:")); panel.add(originCombo);
        panel.add(new JLabel("Destination:")); panel.add(destCombo);
        panel.add(new JLabel("Preferred Date:")); panel.add(dateField);
        panel.add(new JLabel("Preferred Time:")); panel.add(timeField);
        panel.add(new JLabel("Seats Needed:")); panel.add(seatsSpinner);
        panel.add(new JLabel("Notes:")); panel.add(notesField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Post Ride Request", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String origin = (String) originCombo.getSelectedItem();
                String destination = (String) destCombo.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime time = LocalTime.parse(timeField.getText());
                int seats = (int) seatsSpinner.getValue();
                String notes = notesField.getText();
                if (controller.postRideRequest(origin, destination, date, time, seats, notes)) {
                    JOptionPane.showMessageDialog(this, "Ride request posted successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        panel.add(new JLabel("Current Password:")); panel.add(oldPasswordField);
        panel.add(new JLabel("New Password:")); panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:")); panel.add(confirmPasswordField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return;
            }
            if (controller.changePassword(oldPassword, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to change password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadDashboardData() {
        statsLabel.setText(getUserQuickStats());
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.logout();
            dispose();
            new LoginView().setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RideController controller = new RideController();
            new DashboardView(controller).setVisible(true);
        });
    }
}
