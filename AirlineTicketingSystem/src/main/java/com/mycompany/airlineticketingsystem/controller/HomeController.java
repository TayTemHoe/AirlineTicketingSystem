package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.AirlineTicketingSystem;
import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void goToStaffLogin() throws IOException {
        AirlineTicketingSystem.setRoot("StaffLogin");
    }

    @FXML
    private void goToCustomerLogin() throws IOException {
        AirlineTicketingSystem.setRoot("CustomerLogin");
    }
}