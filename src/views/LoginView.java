package views;

import controllers.RideController;
import controllers.AuthController;

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

        // REGISTER PANEL STILL CREATED BUT DISABLED (Option A)
        JPanel registerPanel = createRegisterPanel();  // <- still created
        registerPanel.setEnabled(false);
        registerPanel.setVisible(false);              // <- but never shown

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");      // <- but we never switch to it

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

        // OVERRIDE: open the REAL RegisterView
        showRegisterButton.addActionListener(e -> {
            dispose();
            new RegisterView().setVisible(true);
        });

        // Layout
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Dummy register panel (disabled)
     */
    private JPanel createRegisterPanel() {
        return new JPanel(); // kept but disabled
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
