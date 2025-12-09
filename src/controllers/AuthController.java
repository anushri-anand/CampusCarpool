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

    public boolean handleRegisterAsPassenger(String name, String roll, String email, String password, String dest) {
        var user = authService.registerPassenger(name, roll, email, password, dest);
        return user != null;
    }


    public boolean handleRegisterAsDriver(String name, String roll, String email, String password,
                                      String license, String model, String vehicleNum, int seats,
                                      String role) {  
    var user = authService.registerDriver(name, roll, email, password,
                                          license, model, vehicleNum, seats,
                                          role);      
    return user != null;
}

    public void showLoginView() {
        registerView.dispose();
        new LoginView().setVisible(true);
    }
    
}
