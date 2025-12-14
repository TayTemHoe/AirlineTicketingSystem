/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FlightSearchController {

    @FXML private ComboBox<String> comboFrom;
    @FXML private ComboBox<String> comboTo;
    @FXML private DatePicker datePicker;
    @FXML private VBox flightListContainer;
    @FXML private Label lblResultCount;

    private final FlightService flightService;
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public FlightSearchController() {
        this.flightService = new FlightServiceImpl();
    }

    @FXML
    public void initialize() {
        // 1. Populate ComboBoxes
        List<Flight> allFlights = flightService.getAllFlights();
        
        List<String> origins = allFlights.stream().map(Flight::getDepartCountry).distinct().collect(Collectors.toList());
        List<String> destinations = allFlights.stream().map(Flight::getArriveCountry).distinct().collect(Collectors.toList());
        
        comboFrom.getItems().addAll(origins);
        comboTo.getItems().addAll(destinations);

        // 2. Load ALL flights initially
        renderFlightCards(allFlights);
    }

    @FXML
    private void handleSearch() {
        String from = comboFrom.getValue();
        String to = comboTo.getValue();
        java.time.LocalDate date = datePicker.getValue();

        List<Flight> results = flightService.getAllFlights().stream()
                .filter(f -> from == null || f.getDepartCountry().equals(from))
                .filter(f -> to == null || f.getArriveCountry().equals(to))
                .filter(f -> date == null || f.getDepartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        renderFlightCards(results);
    }

    @FXML
    private void handleClear() {
        // 1. Clear UI Inputs
        comboFrom.setValue(null);
        comboTo.setValue(null);
        datePicker.setValue(null);

        // 2. Reset List to show everything
        renderFlightCards(flightService.getAllFlights());
    }

    private void renderFlightCards(List<Flight> flights) {
        flightListContainer.getChildren().clear();
        lblResultCount.setText(flights.size() + " Flight(s) found");

        if (flights.isEmpty()) {
            Label emptyMsg = new Label("No flights found matching your criteria.");
            emptyMsg.setStyle("-fx-font-size: 16px; -fx-text-fill: #999; -fx-padding: 20;");
            flightListContainer.getChildren().add(emptyMsg);
            return;
        }

        for (Flight f : flights) {
            // Stats Logic
            List<Seat> seats = flightService.getSeatsForFlight(f.getFlightId());
            long availableCount = seats.stream().filter(s -> s.getStatus() == SeatStatus.AVAILABLE).count();
            
            // --- CARD CONTAINER ---
            HBox card = new HBox(20);
            card.getStyleClass().add("flight-card");
            card.setAlignment(Pos.CENTER_LEFT);

            // --- A: TIME & ROUTE ---
            VBox routeBox = new VBox(5);
            Label lblTime = new Label(f.getDepartTime().format(timeFmt) + " - " + f.getArriveTime().format(timeFmt));
            lblTime.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #333;");
            
            Label lblDate = new Label(f.getDepartTime().format(dateFmt));
            lblDate.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

            Label lblRoute = new Label(f.getDepartCountry() + " ➝ " + f.getArriveCountry());
            lblRoute.getStyleClass().add("flight-route-label");

            routeBox.getChildren().addAll(lblDate, lblTime, lblRoute);

            // --- B: INFO ---
            VBox infoBox = new VBox(5);
            Label lblPlane = new Label("✈ " + f.getPlaneId());
            lblPlane.setStyle("-fx-text-fill: #0078D7; -fx-font-weight: bold;");
            Label lblSeats = new Label(availableCount + " seats left");
            lblSeats.setStyle(availableCount < 10 ? "-fx-text-fill: #dc3545;" : "-fx-text-fill: #28a745;");
            infoBox.getChildren().addAll(lblPlane, lblSeats);

            // --- SPACER ---
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // --- C: PRICES (Updated!) ---
            VBox priceBox = new VBox(4);
            priceBox.setAlignment(Pos.CENTER_RIGHT);

            Label lblEco = new Label("Economy: RM " + f.getPriceEconomy());
            lblEco.getStyleClass().add("price-economy"); // Greenish
            
            Label lblBus = new Label("Business: RM " + f.getPriceBusiness());
            lblBus.getStyleClass().add("price-business"); // Gold/Premium

            priceBox.getChildren().addAll(lblEco, lblBus);

            // --- D: BUTTON ---
            Button btnBook = new Button("Book Now");
            btnBook.getStyleClass().addAll("button", "btn-primary");
            btnBook.setOnAction(e -> handleBookClick(f));

            // Add all to card
            card.getChildren().addAll(routeBox, infoBox, spacer, priceBox, btnBook);
            flightListContainer.getChildren().add(card);
        }
    }

    // ... inside FlightSearchController class ...

    private void handleBookClick(Flight f) {
        try {
            // 1. Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/airlineticketingsystem/SeatSelection.fxml"));
            Parent seatView = loader.load();

            // 2. Get the Controller and PASS DATA
            SeatSelectionController controller = loader.getController();
            controller.setFlight(f); // <--- THIS IS KEY

            // 3. Switch the View (Find the Main Layout's content area)
            // Tip: We assume the FlightSearch view is inside the StackPane of CustomerMainLayout
            // We can get the Scene, find the root, and swap center.
            
            if (flightListContainer.getScene() != null) {
                BorderPane mainLayout = (BorderPane) flightListContainer.getScene().getRoot();
                StackPane contentArea = (StackPane) mainLayout.getCenter();
                
                contentArea.getChildren().clear();
                contentArea.getChildren().add(seatView);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading SeatSelection screen: " + e.getMessage());
        }
    }
}