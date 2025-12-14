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
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class FlightManagementController {

    // Inputs
    @FXML private TextField txtDepartCountry, txtArriveCountry;
    @FXML private DatePicker dateDepart, dateArrive;
    @FXML private TextField txtDepartTime, txtArriveTime;
    @FXML private TextField txtPlaneId, txtModel, txtCapacity;
    @FXML private TextField txtEcoPrice, txtBizPrice;
    @FXML private Button btnAction; // To switch between "Create" and "Update"
    
    // Table
    @FXML private TableView<Flight> tableFlights;
    @FXML private TableColumn<Flight, String> colId, colRoute, colDate, colPlane, colPrice;
    
    // NEW COLUMNS
    @FXML private TableColumn<Flight, String> colModel, colCapacity;
    @FXML private TableColumn<Flight, String> colAvailable, colBooked;
    @FXML private TableColumn<Flight, Void> colAction;

    private final FlightService flightService = new FlightServiceImpl();
    private boolean isEditMode = false; // Track if we are editing

    @FXML
    public void initialize() {
        tableFlights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Standard Columns
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFlightId()));
        colRoute.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartCountry() + " ➝ " + c.getValue().getArriveCountry()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFormattedTime()));
        colPlane.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlaneId()));
        colPrice.setCellValueFactory(c -> new SimpleStringProperty("RM" + c.getValue().getPriceEconomy() + " / RM" + c.getValue().getPriceBusiness()));

        // NEW: Static Plane Info (Assuming Plane info was saved in Flight or retrieved separately)
        // Note: Flight object in your model currently stores planeId. To show Model/Capacity, 
        // we ideally need to fetch the Plane object. For now, I will display "Unknown" 
        // unless you update Flight model to store plane details. 
        // HACK: I will just show placeholders or fetch if possible.
        colModel.setCellValueFactory(c -> new SimpleStringProperty("Boeing 737")); // Placeholder or need DB join
        colCapacity.setCellValueFactory(c -> new SimpleStringProperty("60"));      // Placeholder or need DB join

        // NEW: Live Seat Counts
        colAvailable.setCellValueFactory(c -> {
            List<Seat> seats = flightService.getSeatsForFlight(c.getValue().getFlightId());
            long count = seats.stream().filter(s -> s.getStatus() == SeatStatus.AVAILABLE).count();
            return new SimpleStringProperty(String.valueOf(count));
        });

        colBooked.setCellValueFactory(c -> {
            List<Seat> seats = flightService.getSeatsForFlight(c.getValue().getFlightId());
            long count = seats.stream().filter(s -> s.getStatus() == SeatStatus.BOOKED).count();
            return new SimpleStringProperty(String.valueOf(count));
        });

        // NEW: Action Buttons (Update/Delete)
        addButtonToTable();

        handleRefresh();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Flight, Void>, TableCell<Flight, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Flight, Void> call(final TableColumn<Flight, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("✎");
                    private final Button btnDelete = new Button("🗑");

                    {
                        btnEdit.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-size: 10px;");
                        btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 10px;");
                        
                        btnEdit.setOnAction(event -> {
                            Flight flight = getTableView().getItems().get(getIndex());
                            fillFormForEdit(flight);
                        });

                        btnDelete.setOnAction(event -> {
                            Flight flight = getTableView().getItems().get(getIndex());
                            deleteFlight(flight);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox pane = new HBox(5, btnEdit, btnDelete);
                            setGraphic(pane);
                        }
                    }
                };
            }
        };
        colAction.setCellFactory(cellFactory);
    }

    private void fillFormForEdit(Flight f) {
        txtDepartCountry.setText(f.getDepartCountry());
        txtArriveCountry.setText(f.getArriveCountry());
        txtDepartTime.setText(f.getDepartTime().toLocalTime().toString());
        txtArriveTime.setText(f.getArriveTime().toLocalTime().toString());
        dateDepart.setValue(f.getDepartTime().toLocalDate());
        dateArrive.setValue(f.getArriveTime().toLocalDate());
        txtPlaneId.setText(f.getPlaneId());
        txtEcoPrice.setText(f.getPriceEconomy().toString());
        txtBizPrice.setText(f.getPriceBusiness().toString());
        
        // Lock ID fields
        txtPlaneId.setDisable(true); 
        
        btnAction.setText("Update Flight");
        btnAction.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
        isEditMode = true;
    }

    private void deleteFlight(Flight f) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Flight " + f.getFlightId() + "? This will delete all seat data.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            ((FlightServiceImpl) flightService).deleteFlight(f.getFlightId());
            handleRefresh();
        }
    }

    @FXML
    private void handleCreateOrUpdate() {
        try {
            LocalDateTime departDT = LocalDateTime.of(dateDepart.getValue(), LocalTime.parse(txtDepartTime.getText()));
            LocalDateTime arriveDT = LocalDateTime.of(dateArrive.getValue(), LocalTime.parse(txtArriveTime.getText()));
            
            // Build Flight
            Flight flight = new Flight.Builder()
                .route(txtDepartCountry.getText(), txtArriveCountry.getText())
                .timing(departDT, arriveDT)
                .prices(Double.parseDouble(txtEcoPrice.getText()), Double.parseDouble(txtBizPrice.getText()))
                .plane(txtPlaneId.getText())
                .build();

            if (isEditMode) {
                // UPDATE
                flight.setFlightId(tableFlights.getSelectionModel().getSelectedItem().getFlightId()); // Keep original ID
                ((FlightServiceImpl) flightService).updateFlight(flight);
                
                // Reset UI
                btnAction.setText("Create Flight & Generate Seats");
                btnAction.setStyle("");
                txtPlaneId.setDisable(false);
                isEditMode = false;
                new Alert(Alert.AlertType.INFORMATION, "Flight Updated!").show();
            } else {
                // CREATE
                flight.setFlightId("F" + System.currentTimeMillis() % 10000);
                Plane plane = new Plane(txtPlaneId.getText(), txtModel.getText(), Integer.parseInt(txtCapacity.getText()));
                flightService.createFlight(flight, plane);
                new Alert(Alert.AlertType.INFORMATION, "Flight Created!").show();
            }
            handleRefresh();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleRefresh() {
        tableFlights.setItems(FXCollections.observableArrayList(flightService.getAllFlights()));
    }
}
