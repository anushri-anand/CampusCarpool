package views;

import controllers.AuthController;
import utils.NotificationCenter;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {

    private JTextField textName, textRoll, textEmail;
    private JPasswordField textPassword, textConfirm;
    private JComboBox<String> destinationCombo;

    private JTextField licenseField, vehicleModelField, vehicleNumberField;
    private JSpinner seatsSpinner;

    private JRadioButton passengerRadio, driverRadio, bothRadio;
    private JButton btnRegister, btnBack;

    private AuthController authController;

    public RegisterView() {
        super("CampusCarpool - Register");
        authController = new AuthController(this);

        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ----- HEADER -----
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(33, 128, 141));
        add(title, BorderLayout.NORTH);

        // ----- FORM PANEL -----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        // ----- ROLE SELECTION -----
        passengerRadio = new JRadioButton("Passenger");
        driverRadio = new JRadioButton("Driver");
        bothRadio = new JRadioButton("Both");
        passengerRadio.setSelected(true);

        passengerRadio.setBackground(Color.WHITE);
        driverRadio.setBackground(Color.WHITE);
        bothRadio.setBackground(Color.WHITE);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(passengerRadio);
        roleGroup.add(driverRadio);
        roleGroup.add(bothRadio);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 5));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(new JLabel("Register as:"));
        rolePanel.add(passengerRadio);
        rolePanel.add(driverRadio);
        rolePanel.add(bothRadio);

        gbc.gridy = 0;
        formPanel.add(rolePanel, gbc);

        // ----- FORM FIELDS -----
        gbc.gridy++;
        formPanel.add(makeLabel("Full Name"), gbc);
        gbc.gridy++;
        textName = makeTextField();
        formPanel.add(textName, gbc);

        gbc.gridy++;
        formPanel.add(makeLabel("Roll Number (e.g., 2024A7PS0328U)"), gbc);
        gbc.gridy++;
        textRoll = makeTextField();
        formPanel.add(textRoll, gbc);

        gbc.gridy++;
        formPanel.add(makeLabel("Email (e.g., f20240328@dubai.bits-pilani.ac.in)"), gbc);
        gbc.gridy++;
        textEmail = makeTextField();
        formPanel.add(textEmail, gbc);

        gbc.gridy++;
        formPanel.add(makeLabel("Password"), gbc);
        gbc.gridy++;
        textPassword = new JPasswordField(20);
        styleField(textPassword);
        formPanel.add(textPassword, gbc);

        gbc.gridy++;
        formPanel.add(makeLabel("Confirm Password"), gbc);
        gbc.gridy++;
        textConfirm = new JPasswordField(20);
        styleField(textConfirm);
        formPanel.add(textConfirm, gbc);

        // Passenger optional destination
        gbc.gridy++;
        formPanel.add(makeLabel("Preferred Destination (Optional)"), gbc);
        gbc.gridy++;
        destinationCombo = new JComboBox<>(getDubaiDestinations());
        destinationCombo.setPreferredSize(new Dimension(300, 35));
        formPanel.add(destinationCombo, gbc);

        // Driver fields
        gbc.gridy++;
        licenseField = createDriverField(formPanel, "License Number", gbc);
        gbc.gridy++;
        vehicleModelField = createDriverField(formPanel, "Vehicle Model (e.g., Honda City)", gbc);
        gbc.gridy++;
        vehicleNumberField = createDriverField(formPanel, "Vehicle Number (e.g., DXB-12345)", gbc);
        gbc.gridy++;
        JLabel seatsLabel = makeLabel("Seats Available");
        formPanel.add(seatsLabel, gbc);
        gbc.gridy++;
        seatsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
        seatsSpinner.setPreferredSize(new Dimension(300, 35));
        formPanel.add(seatsSpinner, gbc);

        toggleDriverFields(false);

        // ----- REGISTER BUTTON -----
        gbc.gridy++;
        btnRegister = new JButton("Create Account");
        styleButton(btnRegister);
        formPanel.add(btnRegister, gbc);

        // ----- BACK BUTTON -----
        gbc.gridy++;
        btnBack = new JButton("← Back to Login");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(new Color(33, 128, 141));
        btnBack.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(btnBack, gbc);

        // ----- SCROLL PANE -----
        JScrollPane scrollPane = new JScrollPane(formPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ----- ACTION LISTENERS -----
        passengerRadio.addActionListener(e -> {
            toggleDriverFields(false);
            destinationCombo.setEnabled(true);
        });

        driverRadio.addActionListener(e -> {
            toggleDriverFields(true);
            destinationCombo.setEnabled(false);
        });

        bothRadio.addActionListener(e -> {
            toggleDriverFields(true);
            destinationCombo.setEnabled(true);
        });

        btnRegister.addActionListener(e -> handleRegisterAction());
        btnBack.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private JTextField createDriverField(JPanel panel, String labelText, GridBagConstraints gbc) {
        panel.add(makeLabel(labelText), gbc);
        gbc.gridy++;
        JTextField tf = makeTextField();
        panel.add(tf, gbc);
        return tf;
    }

    private void toggleDriverFields(boolean visible) {
        licenseField.setVisible(visible);
        vehicleModelField.setVisible(visible);
        vehicleNumberField.setVisible(visible);
        seatsSpinner.setVisible(visible);
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField(20);
        styleField(tf);
        return tf;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton b) {
        b.setPreferredSize(new Dimension(300, 40));
        b.setBackground(new Color(33, 128, 141));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleRegisterAction() {
        String name = textName.getText().trim();
        String roll = textRoll.getText().trim();
        String email = textEmail.getText().trim();
        String password = new String(textPassword.getPassword()).trim();
        String confirm = new String(textConfirm.getPassword()).trim();

        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        String role = passengerRadio.isSelected() ? "PASSENGER" :
                      driverRadio.isSelected() ? "DRIVER" : "BOTH";

        if (role.equals("PASSENGER")) {
            String dest = destinationCombo.getSelectedItem().equals("None") ? null :
                    (String) destinationCombo.getSelectedItem();
            boolean ok = authController.handleRegisterAsPassenger(name, roll, email, password, dest);
            if (ok) {
                showSuccess("Registration successful! Please login.");
                dispose();
                new LoginView().setVisible(true);
            } else showError("Registration failed. Check your inputs.");
            return;
        }

        String license = licenseField.getText().trim();
        String model = vehicleModelField.getText().trim();
        String vehicleNum = vehicleNumberField.getText().trim();
        int seats = (int) seatsSpinner.getValue();

        if (role.equals("DRIVER") || role.equals("BOTH")) {
            if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    license.isEmpty() || model.isEmpty() || vehicleNum.isEmpty()) {
                showError("Please fill all required fields for driver registration.");
                return;
            }
            boolean ok = authController.handleRegisterAsDriver(name, roll, email, password,
                    license, model, vehicleNum, seats);
            if (ok) {
                showSuccess("Driver registration successful! Please login.");
                dispose();
                new LoginView().setVisible(true);
            } else showError("Registration failed. Check your inputs.");
        }
    }

    private String[] getDubaiDestinations() {
        return new String[]{
                "None", "BPDC Campus", "Dragon Mart", "City Centre Mirdif", "Dubai Mall",
                "Mall of the Emirates", "Ibn Battuta Mall", "JBR", "Dubai Marina", "JLT",
                "City Walk", "Global Village"
        };
    }

    public void showError(String msg) { NotificationCenter.showError(msg); }
    public void showSuccess(String msg) { NotificationCenter.showInfo(msg); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
    }
}
