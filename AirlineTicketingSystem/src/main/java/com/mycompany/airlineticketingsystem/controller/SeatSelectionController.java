/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tay Tem Hoe
 */
package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class SeatSelectionController {

    @FXML private Label lblFlightInfo;
    @FXML private GridPane seatGrid;
    
    // Summary Panel
    @FXML private Label lblSelectedSeat;
    @FXML private Label lblSeatType;
    @FXML private Label lblPrice;
    @FXML private Button btnConfirm;

    private final FlightService flightService;
    private Flight currentFlight;
    private Seat selectedSeat = null; // Tracks current choice

    public SeatSelectionController() {
        this.flightService = new FlightServiceImpl();
    }

    /**
     * CRITICAL: This method is called by the previous screen (FlightSearch)
     * to pass the data BEFORE showing this screen.
     */
    public void setFlight(Flight flight) {
        this.currentFlight = flight;
        this.lblFlightInfo.setText("Select Seats for " + flight.getFlightId() + 
                                   " (" + flight.getDepartCountry() + " -> " + flight.getArriveCountry() + ")");
        
        loadSeats();
    }

    private void loadSeats() {
        seatGrid.getChildren().clear();
        
        List<Seat> seats = flightService.getSeatsForFlight(currentFlight.getFlightId());

        for (Seat seat : seats) {
            // 1. Create a Button for the Seat
            Button btn = new Button(seat.getSeatNumber());
            btn.getStyleClass().add("seat-btn");
            btn.setPrefSize(50, 50);

            // 2. Style based on Status & Type
            if (seat.getStatus() == SeatStatus.BOOKED) {
                btn.getStyleClass().add("seat-booked");
                btn.setDisable(true); // Can't click booked seats
            } else if (seat.getType() == SeatType.BUSINESS) {
                btn.getStyleClass().add("seat-business");
            } else {
                btn.getStyleClass().add("seat-economy");
            }

            // 3. Handle Click Event
            btn.setOnAction(e -> handleSeatClick(btn, seat));

            // 4. Calculate Grid Position (Logic: "1A" -> Row 1, Col 0)
            // Parse "1A" -> Row: 1, Col: 'A'
            String seatNum = seat.getSeatNumber(); 
            String rowStr = seatNum.substring(0, seatNum.length() - 1);
            char colChar = seatNum.charAt(seatNum.length() - 1); // 'A', 'B', 'C', 'D'

            int row = Integer.parseInt(rowStr);
            int col = colChar - 'A'; // 'A'->0, 'B'->1...

            // Add visuals for the "Aisle" (Gap between B and C)
            if (col >= 2) col++; // Shift C and D to index 3 and 4

            seatGrid.add(btn, col, row);
        }
    }

    private void handleSeatClick(Button clickedBtn, Seat seat) {
        // 1. Reset previous selection Visuals
        seatGrid.getChildren().forEach(node -> {
            node.getStyleClass().remove("seat-selected");
        });

        // 2. Set new selection
        clickedBtn.getStyleClass().add("seat-selected");
        this.selectedSeat = seat;

        // 3. Update Summary Panel
        lblSelectedSeat.setText(seat.getSeatNumber());
        lblSeatType.setText(seat.getType().toString());
        
        // Show correct price
        if (seat.getType() == SeatType.BUSINESS) {
            lblPrice.setText("RM " + currentFlight.getPriceBusiness());
        } else {
            lblPrice.setText("RM " + currentFlight.getPriceEconomy());
        }

        // Enable Confirm Button
        btnConfirm.setDisable(false);
    }

    @FXML
    private void handleConfirm() {
        if (selectedSeat != null) {
            System.out.println("Confirmed Seat: " + selectedSeat.getSeatNumber());
            // TODO: Move to PassengerDetails Screen
            // loadPassengerDetails(selectedSeat, currentFlight);
        }
    }

    @FXML
    private void handleBack() {
        // Simple way to go back: Re-load FlightSearch
        try {
             // Assuming we are inside the MainLayout StackPane
            Parent searchView = FXMLLoader.load(getClass().getResource("/com/mycompany/airlineticketingsystem/FlightSearch.fxml"));
            
            // Get the parent BorderPane (CustomerMainLayout)
            BorderPane mainLayout = (BorderPane) lblFlightInfo.getScene().getRoot();
            StackPane contentArea = (StackPane) mainLayout.getCenter();
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(searchView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}