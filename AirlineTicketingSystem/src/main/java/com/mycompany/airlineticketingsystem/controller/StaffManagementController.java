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
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import com.mycompany.airlineticketingsystem.session.UserSession; // See helper class below
import com.mycompany.airlineticketingsystem.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Optional;

public class StaffManagementController {

    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, String> colId;
    @FXML private TableColumn<Staff, String> colName;
    @FXML private TableColumn<Staff, String> colPosition;
    @FXML private TableColumn<Staff, String> colEmail;
    @FXML private TableColumn<Staff, String> colPhone;
    @FXML private TableColumn<Staff, String> colGender;
    @FXML private Button btnAddStaff;

    private final AuthenticationService authService = new AuthenticationService();
    private final ObservableList<Staff> staffList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Setup Table Columns
        colId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // 2. Security Check: Hide/Disable "Add Staff" if not Manager
        Staff currentStaff = UserSession.getInstance().getLoggedInStaff();
        if (currentStaff == null || !"Manager".equalsIgnoreCase(currentStaff.getPosition())) {
            btnAddStaff.setDisable(true);
            btnAddStaff.setText("Managers Only");
        }

        // 3. Load Data
        handleRefresh();
    }

    @FXML
    private void handleRefresh() {
        staffList.clear();
        // NOTE: You need to add 'getAllStaff()' to AuthenticationService or UserRepository
        // For now, I will use a placeholder or assume the method exists
        // staffList.addAll(authService.getAllStaff()); 
        
        // Mock data for display testing if DB method isn't ready
        // staffList.add(new Staff("S001", "Boss", "boss@air.com", "0123", Gender.MALE, "Manager", "pass"));
        
        staffTable.setItems(staffList);
    }

    @FXML
    private void handleAddStaff() {
        // Simple Dialog for adding staff
        Dialog<Staff> dialog = new Dialog<>();
        dialog.setTitle("Register New Staff");
        dialog.setHeaderText("Enter details for new staff member");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField name = new TextField(); name.setPromptText("Name");
        TextField email = new TextField(); email.setPromptText("Email");
        TextField phone = new TextField(); phone.setPromptText("Phone");
        ComboBox<Gender> gender = new ComboBox<>(); gender.getItems().setAll(Gender.values());
        ComboBox<String> position = new ComboBox<>(); position.getItems().addAll("Clerk", "Admin", "Manager");
        PasswordField pass = new PasswordField(); pass.setPromptText("Password");

        grid.addRow(0, new Label("Name:"), name);
        grid.addRow(1, new Label("Email:"), email);
        grid.addRow(2, new Label("Phone:"), phone);
        grid.addRow(3, new Label("Gender:"), gender);
        grid.addRow(4, new Label("Position:"), position);
        grid.addRow(5, new Label("Password:"), pass);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                // Auto-generate ID is handled in Service, so pass empty or temp ID
                return new Staff("TEMP", name.getText(), email.getText(), phone.getText(), gender.getValue(), position.getValue(), pass.getText());
            }
            return null;
        });

        Optional<Staff> result = dialog.showAndWait();

        result.ifPresent(newStaff -> {
            if (!ValidationUtils.isValidPassword(newStaff.getPassword())) {
                 new Alert(Alert.AlertType.ERROR, "Staff NOT added: Password invalid.\nMust be 6-8 chars, 1 Upper, 1 Lower, 1 Number, 1 Symbol.").show();
                 return;
             }
            Staff requester = UserSession.getInstance().getLoggedInStaff();
            boolean success = authService.registerNewStaff(requester.getStaffId(), newStaff);
            
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Staff Added Successfully!");
                alert.show();
                handleRefresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add staff.").show();
            }
        });
    }

    @FXML
    private void handleBack() throws IOException {
        AirlineTicketingSystem.setRoot("FlightDashboard"); // Return to dashboard
    }
}