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
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class ProfileController {

    @FXML private Pane root; // Links to fx:id="root"
    @FXML private Label lblName;
    @FXML private Label lblIdLabel;
    @FXML private Label lblId;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblGender;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();

        // --- DYNAMIC CSS LOADING ---
        String cssFile;
        if (session.getLoggedInStaff() != null) {
            cssFile = "/com/mycompany/airlineticketingsystem/staff-style.css";
        } else {
            cssFile = "/com/mycompany/airlineticketingsystem/customer-style.css";
        }
        root.getStylesheets().clear();
        root.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        // ---------------------------

        if (session.getLoggedInCustomer() != null) {
            Customer c = session.getLoggedInCustomer();
            lblName.setText(c.getName());
            lblIdLabel.setText("IC No:");
            lblId.setText(c.getIcNo());
            lblEmail.setText(c.getEmail());
            lblPhone.setText(c.getPhoneNumber());
            lblGender.setText(c.getGender().toString());
        } 
        else if (session.getLoggedInStaff() != null) {
            Staff s = session.getLoggedInStaff();
            lblName.setText(s.getName());
            lblIdLabel.setText("Staff ID:");
            lblId.setText(s.getStaffId());
            lblEmail.setText(s.getEmail());
            lblPhone.setText(s.getPhoneNumber());
            lblGender.setText(s.getGender().toString());
        }
    }

    @FXML
    private void handleEdit() {
        try {
            Parent editView = FXMLLoader.load(getClass().getResource("/com/mycompany/airlineticketingsystem/EditProfile.fxml"));
            BorderPane mainLayout = (BorderPane) root.getScene().getRoot();
            if (mainLayout.getCenter() instanceof StackPane) {
                ((StackPane) mainLayout.getCenter()).getChildren().setAll(editView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
