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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigDecimal;

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
    
    // CHANGED: Use a Set to track multiple seats (prevents duplicates)
    private Set<Seat> selectedSeats = new HashSet<>(); 

    public SeatSelectionController() {
        this.flightService = new FlightServiceImpl();
    }

    public void setFlight(Flight flight) {
        this.currentFlight = flight;
        this.lblFlightInfo.setText("Select Seats for " + flight.getFlightId() + 
                                   " (" + flight.getDepartCountry() + " -> " + flight.getArriveCountry() + ")");
        loadSeats();
    }

    private void loadSeats() {
        seatGrid.getChildren().clear();
        selectedSeats.clear(); // Reset on reload
        updateSummary();       // Reset labels

        List<Seat> seats = flightService.getSeatsForFlight(currentFlight.getFlightId());

        for (Seat seat : seats) {
            Button btn = new Button(seat.getSeatNumber());
            btn.getStyleClass().add("seat-btn");
            btn.setPrefSize(50, 50);

            if (seat.getStatus() == SeatStatus.BOOKED) {
                btn.getStyleClass().add("seat-booked");
                btn.setDisable(true);
            } else if (seat.getType() == SeatType.BUSINESS) {
                btn.getStyleClass().add("seat-business");
            } else {
                btn.getStyleClass().add("seat-economy");
            }

            btn.setOnAction(e -> handleSeatClick(btn, seat));

            // Grid Position Logic
            String seatNum = seat.getSeatNumber(); 
            String rowStr = seatNum.substring(0, seatNum.length() - 1);
            char colChar = seatNum.charAt(seatNum.length() - 1);
            int row = Integer.parseInt(rowStr);
            int col = colChar - 'A';
            if (col >= 2) col++; // Aisle gap

            seatGrid.add(btn, col, row);
        }
    }

    private void handleSeatClick(Button clickedBtn, Seat seat) {
        // TOGGLE LOGIC:
        if (selectedSeats.contains(seat)) {
            // Deselect
            selectedSeats.remove(seat);
            clickedBtn.getStyleClass().remove("seat-selected");
        } else {
            // Select
            selectedSeats.add(seat);
            clickedBtn.getStyleClass().add("seat-selected");
        }

        updateSummary();
    }

    private void updateSummary() {
        if (selectedSeats.isEmpty()) {
            lblSelectedSeat.setText("None");
            lblSeatType.setText("-");
            lblPrice.setText("RM 0.00");
            btnConfirm.setDisable(true);
            return;
        }

        // 1. Show Seat Numbers (e.g., "1A, 1B")
        String seatLabels = selectedSeats.stream()
                .map(Seat::getSeatNumber)
                .sorted()
                .collect(Collectors.joining(", "));
        
        // Truncate if too long to prevent UI breaking
        if (seatLabels.length() > 20) {
            seatLabels = selectedSeats.size() + " seats selected";
        }
        lblSelectedSeat.setText(seatLabels);

        // 2. Show Types (Mixed or specific)
        boolean hasBusiness = selectedSeats.stream().anyMatch(s -> s.getType() == SeatType.BUSINESS);
        boolean hasEconomy = selectedSeats.stream().anyMatch(s -> s.getType() == SeatType.ECONOMY);
        
        if (hasBusiness && hasEconomy) lblSeatType.setText("Mixed Class");
        else if (hasBusiness) lblSeatType.setText("Business");
        else lblSeatType.setText("Economy");

        // 3. Calculate Total Price
        BigDecimal total = BigDecimal.ZERO;
        for (Seat s : selectedSeats) {
            if (s.getType() == SeatType.BUSINESS) {
                total = total.add(currentFlight.getPriceBusiness());
            } else {
                total = total.add(currentFlight.getPriceEconomy());
            }
        }
        lblPrice.setText("RM " + total.toString());
        btnConfirm.setDisable(false);
    }

    @FXML
    private void handleConfirm() {
        if (!selectedSeats.isEmpty()) {
            System.out.println("Proceeding with seats: " + selectedSeats);
            // TODO: Pass the List<Seat> to the next screen (Passenger Details)
            // loadPassengerDetails(new ArrayList<>(selectedSeats), currentFlight);
        }
    }

    @FXML
    private void handleBack() {
        try {
            Parent searchView = FXMLLoader.load(getClass().getResource("/com/mycompany/airlineticketingsystem/FlightSearch.fxml"));
            BorderPane mainLayout = (BorderPane) lblFlightInfo.getScene().getRoot();
            StackPane contentArea = (StackPane) mainLayout.getCenter();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(searchView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}