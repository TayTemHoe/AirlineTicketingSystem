package com.mycompany.airlineticketingsystem.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tay Tem Hoe
 */

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import util.ValidationUtils;

public class CustomerRegisterController {

    @FXML private TextField nameField;
    @FXML private TextField icField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<Gender> genderCombo;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    public void initialize() {
        // Populate the Gender ComboBox
        genderCombo.getItems().setAll(Gender.values());
    }

    @FXML
    private void handleRegister() {
        // 1. Extract Inputs
        String name = nameField.getText();
        String ic = icField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        Gender gender = genderCombo.getValue();
        String pass = passwordField.getText();

        // 2. Validate Inputs (Using ValidationUtils)
        if (!ValidationUtils.isNotEmpty(name) || !ValidationUtils.isNotEmpty(pass)) {
            showError("Please fill in all fields.");
            return;
        }

        if (!ValidationUtils.isValidIC(ic)) {
            showError("Invalid IC Format! Use 990101-14-1234.");
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            showError("Invalid Email Address.");
            return;
        }
        
        if (gender == null) {
            showError("Please select a gender.");
            return;
        }

        // 3. Create Model Object
        Customer newCustomer = new Customer(ic, name, email, phone, gender, pass);

        // 4. Call Service
        boolean success = authService.registerCustomer(newCustomer);

        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Registration Successful! Redirecting to Login...");
            
            // Redirect after short delay (or immediately)
            try {
                AirlineTicketingSystem.setRoot("CustomerLogin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showError("Registration failed. IC may already exist.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        AirlineTicketingSystem.setRoot("CustomerLogin");
    }

    private void showError(String msg) {
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setText(msg);
    }
}
