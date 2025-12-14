package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import com.mycompany.airlineticketingsystem.session.UserSession;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.mycompany.airlineticketingsystem.util.ValidationUtils;

public class StaffLoginController {

    @FXML private TextField staffIdField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    
    private AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() throws IOException {
        String id = staffIdField.getText();
        String pass = passwordField.getText();

        // 1. Validation
        if (!ValidationUtils.isValidStaffId(id)) {
            statusLabel.setText("Invalid ID Format! Use example, S001");
            return;
        }
        
        // 2. Attempt Login (Get Optional<Staff>)
        Optional<Staff> staff = authService.loginStaff(id, pass);

        if (staff.isPresent()) {
            // ✅ SUCCESS: Set the Session
            UserSession.getInstance().setStaff(staff.get());
            System.out.println("Login Successful: " + staff.get().getName());

            // Redirect
            AirlineTicketingSystem.setRoot("StaffMainLayout"); 
        } else {
            statusLabel.setText("Invalid Staff ID or Password");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        AirlineTicketingSystem.setRoot("Home");
    }
}