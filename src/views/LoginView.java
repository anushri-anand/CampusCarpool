package views;

import controllers.RideController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginView - Login and Registration screen for CampusCarpool
 * Demonstrates: Swing GUI, Form Validation, Event Handling
 */
public class LoginView extends JFrame {
    
    private RideController controller;
    
    // Login components
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JButton showRegisterButton;
    
    // Main panels
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Constructor
    public LoginView() {
        this.controller = new RideController();
        initializeUI();
    }
    
    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        setTitle("CampusCarpool - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Card layout for switching between login and register
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create login and register panels
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();
        
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        
        add(mainPanel);
    }
    
    /**
     * Create login panel
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("CampusCarpool", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 128, 141));
        
        JLabel subtitleLabel = new JLabel("BITS Pilani Dubai Campus", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        
        JLabel welcomeLabel = new JLabel("Login to your account", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        headerPanel.add(welcomeLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridy = 1;
        loginEmailField = new JTextField(20);
        loginEmailField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginEmailField.setPreferredSize(new Dimension(300, 35));
        loginEmailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(loginEmailField, gbc);
        
        // Password
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 3;
        loginPasswordField = new JPasswordField(20);
        loginPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPasswordField.setPreferredSize(new Dimension(300, 35));
        loginPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(loginPasswordField, gbc);
        
        // Login button
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 10, 0);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setBackground(new Color(33, 128, 141));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(loginButton, gbc);
        
        // Register link
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerLinkPanel.setBackground(Color.WHITE);
        
        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        showRegisterButton = new JButton("Register here");
        showRegisterButton.setFont(new Font("Arial", Font.BOLD, 13));
        showRegisterButton.setForeground(new Color(33, 128, 141));
        showRegisterButton.setBorderPainted(false);
        showRegisterButton.setContentAreaFilled(false);
        showRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showRegisterButton.setFocusPainted(false);
        
        registerLinkPanel.add(noAccountLabel);
        registerLinkPanel.add(showRegisterButton);
        
        formPanel.add(registerLinkPanel, gbc);
        
        // Event handlers
        loginButton.addActionListener(e -> handleLogin());
        loginPasswordField.addActionListener(e -> handleLogin());
        showRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        
        // Layout
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create register panel
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 128, 141));
        
        JLabel subtitleLabel = new JLabel("Register as Passenger or Driver", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        
        // Role selection
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rolePanel.setBackground(Color.WHITE);
        
        JRadioButton passengerRadio = new JRadioButton("Passenger");
        JRadioButton driverRadio = new JRadioButton("Driver");
        
        passengerRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        driverRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        passengerRadio.setBackground(Color.WHITE);
        driverRadio.setBackground(Color.WHITE);
        
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(passengerRadio);
        roleGroup.add(driverRadio);
        passengerRadio.setSelected(true);
        
        rolePanel.add(new JLabel("Register as:"));
        rolePanel.add(passengerRadio);
        rolePanel.add(driverRadio);
        
        // Form panel (scrollable)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        
        // Common fields
        JTextField nameField = createFormField(formPanel, "Full Name", gbc, 0);
        JTextField rollField = createFormField(formPanel, "Roll Number (e.g., 2024A7PS0336U)", gbc, 1);
        JTextField emailField = createFormField(formPanel, "Email (e.g., f20240328@dubai.bits-pilani.ac.in)", gbc, 2);
        JPasswordField passwordField = createPasswordField(formPanel, "Password (min 6 characters)", gbc, 3);
        JPasswordField confirmField = createPasswordField(formPanel, "Confirm Password", gbc, 4);
        
        // Passenger-specific fields
        JLabel destLabel = new JLabel("Preferred Destination (Optional)");
        destLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JComboBox<String> destCombo = new JComboBox<>(getDubaiDestinations());
        destCombo.setPreferredSize(new Dimension(300, 35));
        
        // Driver-specific fields
        JPanel driverFieldsPanel = new JPanel(new GridBagLayout());
        driverFieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.fill = GridBagConstraints.HORIZONTAL;
        dgbc.insets = new Insets(5, 0, 5, 0);
        dgbc.gridx = 0;
        
        JTextField licenseField = createFormField(driverFieldsPanel, "License Number", dgbc, 0);
        JTextField vehicleModelField = createFormField(driverFieldsPanel, "Vehicle Model (e.g., Honda City)", dgbc, 1);
        JTextField vehicleNumberField = createFormField(driverFieldsPanel, "Vehicle Number (e.g., DXB-12345)", dgbc, 2);
        
        dgbc.gridy = 3;
        JLabel seatsLabel = new JLabel("Seats Available");
        seatsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        driverFieldsPanel.add(seatsLabel, dgbc);
        
        dgbc.gridy = 4;
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
        seatsSpinner.setPreferredSize(new Dimension(300, 35));
        driverFieldsPanel.add(seatsSpinner, dgbc);
        
        driverFieldsPanel.setVisible(false);
        
        // Add conditional panels
        gbc.gridy = 5;
        JPanel passengerFieldsPanel = new JPanel(new GridBagLayout());
        passengerFieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.fill = GridBagConstraints.HORIZONTAL;
        pgbc.gridx = 0;
        pgbc.gridy = 0;
        passengerFieldsPanel.add(destLabel, pgbc);
        pgbc.gridy = 1;
        passengerFieldsPanel.add(destCombo, pgbc);
        
        formPanel.add(passengerFieldsPanel, gbc);
        
        gbc.gridy = 6;
        formPanel.add(driverFieldsPanel, gbc);
        
        // Role change listeners
        passengerRadio.addActionListener(e -> {
            passengerFieldsPanel.setVisible(true);
            driverFieldsPanel.setVisible(false);
            panel.revalidate();
        });
        
        driverRadio.addActionListener(e -> {
            passengerFieldsPanel.setVisible(false);
            driverFieldsPanel.setVisible(true);
            panel.revalidate();
        });
        
        // Register button
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 10, 0);
        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 15));
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setBackground(new Color(33, 128, 141));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(registerButton, gbc);
        
        // Back to login link
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 0, 0, 0);
        JButton backButton = new JButton("← Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setForeground(new Color(33, 128, 141));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        formPanel.add(backButton, gbc);
        
        // Event handlers
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (passengerRadio.isSelected()) {
                String dest = (String) destCombo.getSelectedItem();
                handlePassengerRegistration(name, roll, email, password, dest);
            } else {
                String license = licenseField.getText().trim();
                String model = vehicleModelField.getText().trim();
                String vehicleNum = vehicleNumberField.getText().trim();
                int seats = (int) seatsSpinner.getValue();
                handleDriverRegistration(name, roll, email, password, license, model, vehicleNum, seats);
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        
        // Scroll pane for form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(rolePanel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Helper method to create form field
     */
    private JTextField createFormField(JPanel panel, String label, GridBagConstraints gbc, int row) {
        gbc.gridy = row * 2;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(fieldLabel, gbc);
        
        gbc.gridy = row * 2 + 1;
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(field, gbc);
        
        return field;
    }
    
    /**
     * Helper method to create password field
     */
    private JPasswordField createPasswordField(JPanel panel, String label, GridBagConstraints gbc, int row) {
        gbc.gridy = row * 2;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(fieldLabel, gbc);
        
        gbc.gridy = row * 2 + 1;
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(field, gbc);
        
        return field;
    }
    
    /**
     * Get Dubai destinations
     */
    private String[] getDubaiDestinations() {
        return new String[]{
            "None",
            "BPDC Campus",
            "Dragon Mart",
            "City Centre Mirdif",
            "Dubai Mall",
            "Mall of the Emirates",
            "Ibn Battuta Mall",
            "Dubai Festival City Mall",
            "JBR",
            "Dubai Marina",
            "JLT",
            "City Walk",
            "Global Village"
        };
    }
    
    /**
     * Handle login
     */
    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (controller.login(email, password)) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + controller.getCurrentUser().getName());
            dispose();
            new DashboardView(controller).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle passenger registration
     */
    private void handlePassengerRegistration(String name, String roll, String email, String password, String dest) {
        if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String preferredDest = dest.equals("None") ? null : dest;
        
        if (controller.registerPassenger(name, roll, email, password, preferredDest)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            cardLayout.show(mainPanel, "LOGIN");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle driver registration
     */
    private void handleDriverRegistration(String name, String roll, String email, String password,
                                         String license, String model, String vehicleNum, int seats) {
        if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || password.isEmpty() ||
            license.isEmpty() || model.isEmpty() || vehicleNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (controller.registerDriver(name, roll, email, password, license, model, vehicleNum, seats)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            cardLayout.show(mainPanel, "LOGIN");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
