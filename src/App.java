import utils.DBConnection;
import views.LoginView;

import javax.swing.*;

public class App {

    public static void main(String[] args) {

        if (!initializeDatabase()) {
            JOptionPane.showMessageDialog(null,
                "Database initialization failed.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        configureUI();

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    private static boolean initializeDatabase() {
        try {
            DBConnection.initializeDatabase();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void configureUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}
