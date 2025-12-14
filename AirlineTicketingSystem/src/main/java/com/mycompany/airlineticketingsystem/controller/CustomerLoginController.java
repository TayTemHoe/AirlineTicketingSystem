package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import com.mycompany.airlineticketingsystem.session.UserSession;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.ValidationUtils;

public class CustomerLoginController {

    @FXML private TextField icField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private AuthenticationService authService = new AuthenticationService();

@FXML
    private void handleLogin() throws IOException {
        String ic = icField.getText().trim();
        String pass = passwordField.getText();

        // 1. Validation
        if (!ValidationUtils.isValidIC(ic)) {
            statusLabel.setText("Invalid IC Format! Use 000000-00-0000");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // 2. Attempt Login (Get Optional<Customer>)
        Optional<Customer> customer = authService.loginCustomer(ic, pass);

        if (customer.isPresent()) {
            // ✅ SUCCESS: Set the Session
            UserSession.getInstance().setCustomer(customer.get());
            System.out.println("Login Successful: " + customer.get().getName());

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login Success!");
            
            // Redirect to Main Layout
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
    
    @FXML
    private void goToRegister() throws IOException {
        AirlineTicketingSystem.setRoot("CustomerRegister");
    }
}