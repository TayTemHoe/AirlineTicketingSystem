package com.mycompany.airlineticketingsystem.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tay Tem Hoe
 */

import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import com.mycompany.airlineticketingsystem.session.UserSession;
import com.mycompany.airlineticketingsystem.util.ValidationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class EditProfileController {

    @FXML private ScrollPane root; // Links to fx:id="root" in FXML

    // Info Fields
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private ComboBox<Gender> comboGender;

    // Password Fields
    @FXML private PasswordField txtCurrentPass;
    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;
    @FXML private Label Status;
    @FXML private Label lblStatus;

    private final AuthenticationService authService = new AuthenticationService();
    private final UserSession session = UserSession.getInstance();

    @FXML
    public void initialize() {
        comboGender.getItems().setAll(Gender.values());

        // --- DYNAMIC CSS LOADING ---
        String cssFile;
        if (session.getLoggedInStaff() != null) {
            cssFile = "/com/mycompany/airlineticketingsystem/staff-style.css";
        } else {
            cssFile = "/com/mycompany/airlineticketingsystem/customer-style.css";
        }
        // Force the stylesheet onto the root node
        root.getStylesheets().clear();
        root.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        // ---------------------------

        // Pre-fill fields with current data
        if (session.getLoggedInCustomer() != null) {
            Customer c = session.getLoggedInCustomer();
            txtName.setText(c.getName());
            txtEmail.setText(c.getEmail());
            txtPhone.setText(c.getPhoneNumber());
            comboGender.setValue(c.getGender());
        } 
        else if (session.getLoggedInStaff() != null) {
            Staff s = session.getLoggedInStaff();
            txtName.setText(s.getName());
            txtEmail.setText(s.getEmail());
            txtPhone.setText(s.getPhoneNumber());
            comboGender.setValue(s.getGender());
        }
    }

    // ... (Keep existing handleSaveInfo, handleUpdatePassword, etc.) ...

    @FXML
    private void handleBack() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/com/mycompany/airlineticketingsystem/UserProfile.fxml"));
            // Get the main layout from the current scene root
            BorderPane mainLayout = (BorderPane) root.getScene().getRoot();
            if (mainLayout.getCenter() instanceof StackPane) {
                ((StackPane) mainLayout.getCenter()).getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ... (Keep helper methods setStatus, setStatusI) ...
    private void setStatusI(String msg, boolean isError) {
        Status.setText(msg);
        Status.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    private void setStatus(String msg, boolean isError) {
        lblStatus.setText(msg);
        lblStatus.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
    
    // --- PART 1: SAVE INFO ONLY ---
    @FXML
    private void handleSaveInfo() {
        String newName = txtName.getText();
        String newEmail = txtEmail.getText();
        String newPhone = txtPhone.getText();
        Gender newGender = comboGender.getValue();

        if (!ValidationUtils.isValidEmail(newEmail)) {
            setStatus("Invalid Email format", true);
            return;
        }
        if (!ValidationUtils.isNotEmpty(newName)) {
            setStatus("Name cannot be empty", true);
            return;
        }

        boolean success = false;

        // Update Logic based on User Type
        if (session.getLoggedInCustomer() != null) {
            Customer c = session.getLoggedInCustomer();
            // Create updated object (keeping same IC and Password)
            Customer updated = new Customer(c.getIcNo(), newName, newEmail, newPhone, newGender, c.getPassword());
            if (authService.updateCustomerProfile(updated)) {
                session.setCustomer(updated); // Update Session!
                success = true;
            }
        } 
        else if (session.getLoggedInStaff() != null) {
            Staff s = session.getLoggedInStaff();
            Staff updated = new Staff(s.getStaffId(), newName, newEmail, newPhone, newGender, s.getPosition(), s.getPassword());
            if (authService.updateStaffProfile(updated)) {
                session.setStaff(updated); // Update Session!
                success = true;
            }
        }

        if (success) setStatusI("Profile Information Updated Successfully!", false);
        else setStatusI("Database Error: Could not save profile.", true);
    }

    // --- PART 2: UPDATE PASSWORD ONLY ---
    @FXML
    private void handleUpdatePassword() {
        String current = txtCurrentPass.getText();
        String newPass = txtNewPass.getText();
        String confirm = txtConfirmPass.getText();

        if (!ValidationUtils.isNotEmpty(newPass)) {
            setStatus("New Password cannot be empty", true);
            return;
        }
        
        // NEW: Strict Password Check
        if (!ValidationUtils.isValidPassword(newPass)) {
            setStatus("Password must be 6-8 chars, with Upper, Lower, Number & Symbol.", true);
            return;
        }

        if (!newPass.equals(confirm)) {
            setStatus("New Passwords do not match", true);
            return;
        }

        boolean success = false;

        if (session.getLoggedInCustomer() != null) {
            Customer c = session.getLoggedInCustomer();
            if (!c.getPassword().equals(current)) {
                setStatus("Current password is incorrect", true);
                return;
            }
            // Update Object with NEW Password
            Customer updated = new Customer(c.getIcNo(), c.getName(), c.getEmail(), c.getPhoneNumber(), c.getGender(), newPass);
            if (authService.updateCustomerProfile(updated)) {
                session.setCustomer(updated);
                success = true;
            }
        } 
        else if (session.getLoggedInStaff() != null) {
            Staff s = session.getLoggedInStaff();
            if (!s.getPassword().equals(current)) {
                setStatus("Current password is incorrect", true);
                return;
            }
            Staff updated = new Staff(s.getStaffId(), s.getName(), s.getEmail(), s.getPhoneNumber(), s.getGender(), s.getPosition(), newPass);
            if (authService.updateStaffProfile(updated)) {
                session.setStaff(updated);
                success = true;
            }
        }

        if (success) {
            setStatus("Password Changed Successfully!", false);
            txtCurrentPass.clear(); txtNewPass.clear(); txtConfirmPass.clear();
        } else {
            setStatus("Error changing password.", true);
        }
    }
}