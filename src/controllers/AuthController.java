package controllers;

import services.AuthService;
import views.LoginView;
import views.RegisterView;

public class AuthController {

    private RegisterView registerView;
    private AuthService authService;

    public AuthController(RegisterView registerView) {
        this.registerView = registerView;
        this.authService = new AuthService();
    }

    // =============================
    // REGISTER PASSENGER
    // =============================
    public boolean handleRegisterAsPassenger(String name, String roll, String email, String password, String dest) {
        var user = authService.registerPassenger(name, roll, email, password, dest);
        return user != null;
    }

    // =============================
    // REGISTER DRIVER
    // =============================
    public boolean handleRegisterAsDriver(String name, String roll, String email, String password,
                                          String license, String model, String vehicleNum, int seats) {
        var user = authService.registerDriver(name, roll, email, password, license, model, vehicleNum, seats);
        return user != null;
    }

    // =============================
    // SHOW LOGIN PAGE
    // =============================
    public void showLoginView() {
        registerView.dispose();
        new LoginView().setVisible(true);
    }
}
