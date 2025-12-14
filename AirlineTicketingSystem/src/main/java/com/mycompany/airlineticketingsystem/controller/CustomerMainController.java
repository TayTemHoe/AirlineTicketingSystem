/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;

public class CustomerMainController {

    @FXML private StackPane contentArea;
    @FXML private Label lblWelcome;
    
    // Navigation Buttons (to highlight active one)
    @FXML private Button btnBook;
    @FXML private Button btnTrips;
    @FXML private Button btnProfile;

    // Simulate logged-in user (Replace with actual User Session later)
    private Customer currentUserName = UserSession.getInstance().getLoggedInCustomer(); 

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, " + currentUserName.getName());
        
        // Load the default page (Flight Search) immediately
        showFlightSearch();
    }

    // --- Navigation Actions ---

    @FXML
    private void showFlightSearch() {
        loadView("FlightSearch");
        setActiveButton(btnBook);
    }

    @FXML
    private void showMyBookings() {
        loadView("MyBookings"); // You need to create MyBookings.fxml later
        setActiveButton(btnTrips);
    }

    @FXML
    private void showProfile() {
        loadView("UserProfile"); // You need to create UserProfile.fxml later
        setActiveButton(btnProfile);
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");
        // Logic to return to Login.fxml goes here
    }

    // --- Helper Methods ---

    private void loadView(String fxmlFileName) {
        try {
            // Note: Ensure your FXMLs are in the correct package structure!
            URL fileUrl = getClass().getResource("/com/mycompany/airlineticketingsystem/" + fxmlFileName + ".fxml");
            
            if (fileUrl == null) {
                System.err.println("❌ Setup Error: Cannot find file " + fxmlFileName + ".fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent view = loader.load();
            
            // Clear previous view and add new one
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeButton) {
        // Reset all buttons to default style
        btnBook.getStyleClass().remove("nav-button-active");
        btnTrips.getStyleClass().remove("nav-button-active");
        btnProfile.getStyleClass().remove("nav-button-active");

        // Set active style
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }
}
