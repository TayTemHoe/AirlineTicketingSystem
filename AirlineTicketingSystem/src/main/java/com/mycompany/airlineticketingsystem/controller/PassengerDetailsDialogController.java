package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.model.PassengerDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PassengerDetailsDialogController {

    @FXML private Label seatLabel;
    @FXML private TextField nameField;
    @FXML private TextField passportField;
    @FXML private Label errorLabel;

    private Stage stage;
    private PassengerDetails result; // null = cancelled

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSeatNumber(String seatNo) {
        seatLabel.setText(seatNo);
    }

    public PassengerDetails getResult() {
        return result;
    }

    @FXML
    private void handleOk() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String passport = passportField.getText() == null ? "" : passportField.getText().trim();

        if (name.isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }
        if (passport.isEmpty()) {
            showError("Passport number cannot be empty.");
            return;
        }
        if (!passport.matches("[A-Za-z0-9]{5,20}")) {
            showError("Passport number must be 5–20 letters/numbers (no spaces).");
            return;
        }

        result = new PassengerDetails(name, passport.toUpperCase());
        stage.close();
    }

    @FXML
    private void handleCancel() {
        result = null;
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }
}
