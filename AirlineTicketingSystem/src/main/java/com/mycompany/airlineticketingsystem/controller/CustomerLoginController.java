package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CustomerLoginController {

    @FXML private TextField icField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() throws IOException {
        String ic = icField.getText();
        String pass = passwordField.getText();

        if (authService.loginCustomer(ic, pass)) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login Success!");
            // 2. REDIRECT to the Main Customer Layout (Top Bar + Content Area)
            // This loads CustomerMainLayout.fxml, which defaults to showing FlightSearch
            AirlineTicketingSystem.setRoot("CustomerMainLayout");
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Invalid IC or Password");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        AirlineTicketingSystem.setRoot("Home");
    }
}