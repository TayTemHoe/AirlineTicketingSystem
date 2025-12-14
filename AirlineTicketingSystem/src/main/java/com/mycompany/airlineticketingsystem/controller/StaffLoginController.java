package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StaffLoginController {

    @FXML private TextField staffIdField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() throws IOException {
        String id = staffIdField.getText();
        String pass = passwordField.getText();

        if (authService.loginStaff(id, pass)) {
            // Success: Go to Dashboard
            AirlineTicketingSystem.setRoot("FlightDashboard");
        } else {
            statusLabel.setText("Invalid Staff ID or Password");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        AirlineTicketingSystem.setRoot("Home");
    }
}