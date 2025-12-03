package views;


import controllers.AuthController;
import utils.NotificationCenter;


import javax.swing.*;
import java.awt.*;


public class RegisterView extends JFrame {


    // Common fields
    private JTextField textName, textRoll, textEmail;
    private JPasswordField textPassword, textConfirm;


    // Driver-only fields
    private JLabel licenseLabel, modelLabel, numberLabel, seatsLabel;
    private JTextField licenseField, vehicleModelField, vehicleNumberField;
    private JSpinner seatsSpinner;


    // Role buttons
    private JRadioButton passengerRadio, driverRadio, bothRadio;


    // Auth
    private AuthController authController;


    public RegisterView() {
        super("CampusCarpool - Register");


        authController = new AuthController(this);


        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        // HEADER
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(33, 128, 141));
        add(title, BorderLayout.NORTH);


        // FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;


        // ROLE SELECT
        passengerRadio = makeRadio("Passenger");
        driverRadio = makeRadio("Driver");
        bothRadio = makeRadio("Both");
        passengerRadio.setSelected(true);


        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(passengerRadio);
        roleGroup.add(driverRadio);
        roleGroup.add(bothRadio);


        JPanel rolePanel = new JPanel(new FlowLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(new JLabel("Register as:"));
        rolePanel.add(passengerRadio);
        rolePanel.add(driverRadio);
        rolePanel.add(bothRadio);


        gbc.gridy = 0;
        formPanel.add(rolePanel, gbc);


        // BASIC FIELDS
        gbc.gridy++;
        formPanel.add(makeLabel("Full Name"), gbc);
        gbc.gridy++;
        textName = makeTextField();
        formPanel.add(textName, gbc);


        gbc.gridy++;
        formPanel.add(makeLabel("Roll Number"), gbc);
        gbc.gridy++;
        textRoll = makeTextField();
        formPanel.add(textRoll, gbc);


        gbc.gridy++;
        formPanel.add(makeLabel("Email"), gbc);
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


        // DRIVER FIELDS (will hide initially)
        gbc.gridy++;
        licenseLabel = makeLabel("License Number");
        formPanel.add(licenseLabel, gbc);
        gbc.gridy++;
        licenseField = makeTextField();
        formPanel.add(licenseField, gbc);


        gbc.gridy++;
        modelLabel = makeLabel("Vehicle Model");
        formPanel.add(modelLabel, gbc);
        gbc.gridy++;
        vehicleModelField = makeTextField();
        formPanel.add(vehicleModelField, gbc);


        gbc.gridy++;
        numberLabel = makeLabel("Vehicle Number");
        formPanel.add(numberLabel, gbc);
        gbc.gridy++;
        vehicleNumberField = makeTextField();
        formPanel.add(vehicleNumberField, gbc);


        gbc.gridy++;
        seatsLabel = makeLabel("Seats Available");
        formPanel.add(seatsLabel, gbc);
        gbc.gridy++;
        seatsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
        seatsSpinner.setPreferredSize(new Dimension(300, 35));
        formPanel.add(seatsSpinner, gbc);


        // INITIAL ROLE SETUP — PASSENGER
        toggleFieldsForRole("PASSENGER");


        // REGISTER BUTTON
        gbc.gridy++;
        JButton btnRegister = new JButton("Create Account");
        styleButton(btnRegister);
        formPanel.add(btnRegister, gbc);


        // BACK BUTTON
        gbc.gridy++;
        JButton btnBack = new JButton("← Back to Login");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(new Color(33, 128, 141));
        btnBack.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(btnBack, gbc);


        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);


        // LISTENERS
        passengerRadio.addActionListener(e -> toggleFieldsForRole("PASSENGER"));
        driverRadio.addActionListener(e -> toggleFieldsForRole("DRIVER"));
        bothRadio.addActionListener(e -> toggleFieldsForRole("BOTH"));


        btnRegister.addActionListener(e -> handleRegisterAction());
        btnBack.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private void toggleFieldsForRole(String role) {


        boolean showDriver = role.equals("DRIVER") || role.equals("BOTH");


        // DRIVER FIELDS VISIBILITY
        licenseLabel.setVisible(showDriver);
        licenseField.setVisible(showDriver);


        modelLabel.setVisible(showDriver);
        vehicleModelField.setVisible(showDriver);


        numberLabel.setVisible(showDriver);
        vehicleNumberField.setVisible(showDriver);


        seatsLabel.setVisible(showDriver);
        seatsSpinner.setVisible(showDriver);


        revalidate();
        repaint();
    }


    // ============================================================
    // UI HELPERS
    // ============================================================
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


    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(300, 35));
        f.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }


    private void styleButton(JButton b) {
        b.setPreferredSize(new Dimension(300, 40));
        b.setBackground(new Color(33, 128, 141));
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
    }


    private JRadioButton makeRadio(String txt) {
        JRadioButton r = new JRadioButton(txt);
        r.setBackground(Color.WHITE);
        return r;
    }


    // ============================================================
    // REGISTRATION LOGIC
    // ============================================================
    private void handleRegisterAction() {


        String name = textName.getText().trim();
        String roll = textRoll.getText().trim();
        String email = textEmail.getText().trim();
        String password = new String(textPassword.getPassword());
        String confirm = new String(textConfirm.getPassword());


        if (!password.equals(confirm)) {
            showError("Passwords do not match!");
            return;
        }


        String role = passengerRadio.isSelected() ? "PASSENGER"
                : driverRadio.isSelected() ? "DRIVER"
                : "BOTH";


        // Passenger
        if (role.equals("PASSENGER")) {


            boolean ok = authController.handleRegisterAsPassenger(
                    name, roll, email, password, null
            );


            if (ok) {
                showSuccess("Passenger registered!");
                dispose();
                new LoginView().setVisible(true);
            } else showError("Registration failed.");


            return;
        }


        // Driver + Both → driver fields required
        String license = licenseField.getText().trim();
        String model = vehicleModelField.getText().trim();
        String vehicleNum = vehicleNumberField.getText().trim();
        int seats = (int) seatsSpinner.getValue();


        boolean ok = authController.handleRegisterAsDriver(
                name, roll, email, password,
                license, model, vehicleNum, seats
        );


        if (ok) {
            showSuccess(role + " registration successful!");
            dispose();
            new LoginView().setVisible(true);
        } else showError("Registration failed.");
    }


    public void showError(String msg) { NotificationCenter.showError(msg); }
    public void showSuccess(String msg) { NotificationCenter.showInfo(msg); }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
    }
}


