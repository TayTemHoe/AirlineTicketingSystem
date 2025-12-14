/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;

public class StaffMainController {

    @FXML private StackPane contentArea;
    @FXML private Label lblPageTitle;
    @FXML private Label lblUserInfo;

    @FXML private Button btnDashboard;
    @FXML private Button btnFlights;
    @FXML private Button btnReports;
    @FXML private Button btnStaff;
    @FXML private Button btnProfile;

    @FXML
    public void initialize() {
        // 1. Set User Info
        Staff currentStaff = UserSession.getInstance().getLoggedInStaff();
        if (currentStaff != null) {
            lblUserInfo.setText("Logged in as: " + currentStaff.getName() + " (" + currentStaff.getPosition() + ")");
            
            // 2. Role Check: Disable Staff Management if not Manager
            if (!"Manager".equalsIgnoreCase(currentStaff.getPosition())) {
                btnStaff.setDisable(true);
                btnStaff.setOpacity(0.5);
            }
        }

        // 3. Load Default View
        showDashboard();
    }

    // --- NAVIGATION ---
    @FXML private void showDashboard() {
        loadView("FlightDashboard", "Flight Inventory Dashboard");
        setActive(btnDashboard);
    }

    @FXML private void showFlightManagement() {
        loadView("FlightManagement", "Flight & Plane Management");
        setActive(btnFlights);
    }

    @FXML private void showReports() {
        loadView("ReportDashboard", "Analytics & Reports");
        setActive(btnReports);
    }

    @FXML private void showStaffManagement() {
        loadView("StaffManagement", "Staff User Management");
        setActive(btnStaff);
    }

    @FXML private void showProfile() {
        loadView("UserProfile", "My Profile");
        setActive(btnProfile);
    }

    @FXML private void handleLogout() throws IOException {
        UserSession.getInstance().cleanUserSession();
        AirlineTicketingSystem.setRoot("StaffLogin");
    }

    // --- HELPER ---
    private void loadView(String fxml, String title) {
        try {
            lblPageTitle.setText(title);
            URL url = getClass().getResource("/com/mycompany/airlineticketingsystem/" + fxml + ".fxml");
            if (url == null) {
                System.err.println("❌ FXML Not Found: " + fxml);
                return;
            }
            Parent view = FXMLLoader.load(url);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActive(Button btn) {
        // Reset Styles
        btnDashboard.getStyleClass().remove("nav-button-active");
        btnFlights.getStyleClass().remove("nav-button-active");
        btnReports.getStyleClass().remove("nav-button-active");
        btnStaff.getStyleClass().remove("nav-button-active");
        btnProfile.getStyleClass().remove("nav-button-active");

        // Set Active
        if (!btn.getStyleClass().contains("nav-button-active")) {
            btn.getStyleClass().add("nav-button-active");
        }
    }
}
