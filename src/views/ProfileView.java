import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProfileView extends JFrame {

    private JLabel lblName, lblRoll, lblEmail, lblWarnings, lblBlacklist, lblRatings, lblRideHistory;
    private JButton btnChangePass, btnEditProfile, btnBack;
    private ProfileController controller;

    public ProfileView(ProfileController controller) {
        this.controller = controller;
        controller.setView(this);     // CONNECT VIEW TO CONTROLLER
        initUI();
        controller.loadProfile();     // LOAD USER DATA FROM SERVICE
    }

    // Initialize Swing GUI Components
    private void initUI() {
        setTitle("My Profile");
        setSize(450, 400);
        setLayout(new GridLayout(10, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        lblName = new JLabel();
        lblRoll = new JLabel();
        lblEmail = new JLabel();
        lblWarnings = new JLabel();
        lblBlacklist = new JLabel();
        lblRatings = new JLabel();
        lblRideHistory = new JLabel();

        btnChangePass = new JButton("Change Password");
        btnEditProfile = new JButton("Edit Profile");
        btnBack = new JButton("Back to Dashboard");

        add(lblName);
        add(lblRoll);
        add(lblEmail);
        add(lblWarnings);
        add(lblBlacklist);
        add(lblRatings);
        add(lblRideHistory);
        add(btnChangePass);
        add(btnEditProfile);
        add(btnBack);

        // Listeners
        btnChangePass.addActionListener(e -> new ChangePasswordView(controller));
        btnEditProfile.addActionListener(e -> new EditProfileView(controller));
        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Called by Controller AFTER DATA IS LOADED
    public void showProfileDetails(User u, List<Rating> ratings, List<Ride> rides) {
        lblName.setText("Name: " + u.getName());
        lblRoll.setText("Roll No: " + u.getRollNumber());
        lblEmail.setText("Email: " + u.getEmail());
        lblWarnings.setText("Warnings: " + u.getWarnings());
        lblBlacklist.setText("Blacklist Until: " + u.getBlacklistUntil());
        lblRatings.setText("Total Ratings: " + ratings.size());
        lblRideHistory.setText("Ride History Count: " + rides.size());
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showError(String err) {
        JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
