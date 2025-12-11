/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.airlineticketingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    // These names MUST match the fx:id in your FXML file
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    // This method is called when the button is clicked
    @FXML
    private void handleLoginButton() throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        // --- SIMULATED DATABASE CHECK ---
        // In the real app, you would call: staffService.login(user, pass);
        boolean isValid = mockLoginCheck(user, pass); 

        if (isValid) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login Successful! Loading Dashboard...");
            
            // Code to switch to the next screen would go here
            // App.setRoot("Dashboard"); 
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Invalid Username or Password.");
        }
    }

    private boolean mockLoginCheck(String u, String p) {
        // Simple check for testing UI functions
        return "admin".equals(u) && "1234".equals(p);
    }
}
