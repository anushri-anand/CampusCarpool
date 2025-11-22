package views;

import controllers.AuthController;
import utils.NotificationCenter;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {

    private JTextField textName;
    private JTextField textRoll;
    private JTextField textEmail;
    private JPasswordField textPassword;
    private JButton btnRegister;
    private JButton btnLogin;

    private AuthController authController;

    public RegisterView() {
        super("CampusCarpool - Register");

        authController = new AuthController(this);

        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(110, 20, 300, 40);
        add(title);

        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(50, 90, 150, 25);
        add(lblName);

        textName = new JTextField();
        textName.setBounds(180, 90, 200, 25);
        add(textName);

        JLabel lblRoll = new JLabel("Roll Number:");
        lblRoll.setBounds(50, 140, 150, 25);
        add(lblRoll);

        textRoll = new JTextField();
        textRoll.setBounds(180, 140, 200, 25);
        add(textRoll);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 190, 150, 25);
        add(lblEmail);

        textEmail = new JTextField();
        textEmail.setBounds(180, 190, 200, 25);
        add(textEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 240, 150, 25);
        add(lblPassword);

        textPassword = new JPasswordField();
        textPassword.setBounds(180, 240, 200, 25);
        add(textPassword);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(150, 300, 140, 35);
        add(btnRegister);

        btnLogin = new JButton("Back to Login");
        btnLogin.setBounds(150, 350, 140, 30);
        add(btnLogin);

        // ACTION LISTENERS  
        btnRegister.addActionListener(e -> authController.handleRegister());
        btnLogin.addActionListener(e -> authController.showLoginView());
    }

    // ---- Getters for controller ----
    public String getNameField() { return textName.getText().trim(); }
    public String getRollField() { return textRoll.getText().trim(); }
    public String getEmailField() { return textEmail.getText().trim(); }
    public String getPasswordField() { return new String(textPassword.getPassword()).trim(); }

    // ---- UI helper methods ----
    public void showError(String msg) {
        NotificationCenter.showError(msg);
    }

    public void showSuccess(String msg) {
        NotificationCenter.showInfo(msg);
    }
}
