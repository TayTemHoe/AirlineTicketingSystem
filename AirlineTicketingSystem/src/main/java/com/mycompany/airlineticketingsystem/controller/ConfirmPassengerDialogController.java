package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.model.PassengerDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmPassengerDialogController {

    @FXML private Label seatValue;
    @FXML private Label nameValue;
    @FXML private Label passportValue;

    private Stage stage;
    private boolean confirmed = false;

    public void setStage(Stage stage) { this.stage = stage; }

    public void setData(String seatNo, PassengerDetails details) {
        seatValue.setText(seatNo);
        nameValue.setText(details.getFullName());
        passportValue.setText(details.getPassportNumber());
    }

    public boolean isConfirmed() { return confirmed; }

    @FXML
    private void handleConfirm() {
        confirmed = true;
        stage.close();
    }

    @FXML
    private void handleRecheck() {
        confirmed = false;
        stage.close();
    }
}
