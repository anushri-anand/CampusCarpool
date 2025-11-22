package controllers;

import services.AuthService;
import views.LoginView;
import views.RegisterView;

public class AuthController {

    private RegisterView registerView;
    private LoginView loginView;
    private AuthService authService;

    // Constructor for RegisterView
    public AuthController(RegisterView registerView) {
        this.registerView = registerView;
        this.authService = new AuthService();
    }

    // Constructor for LoginView
    public AuthController(LoginView loginView) {
        this.loginView = loginView;
        this.authService = new AuthService();
    }

    // =============================
    // HANDLE REGISTER
    // =============================
    public void handleRegister() {

        String name = registerView.getNameField();
        String roll = registerView.getRollField();
        String email = registerView.getEmailField();
        String password = registerView.getPasswordField();

        // Call AuthService → create passenger by default
        var user = authService.registerPassenger(name, roll, email, password, null);

        if (user == null) {
            registerView.showError("Registration failed. Check your inputs.");
            return;
        }

        registerView.showSuccess("Registration successful!");
        registerView.dispose();
        new LoginView().setVisible(true);
    }

    // =============================
    // SHOW LOGIN PAGE
    // =============================
    public void showLoginView() {
        registerView.dispose();
        new LoginView().setVisible(true);
    }
}
