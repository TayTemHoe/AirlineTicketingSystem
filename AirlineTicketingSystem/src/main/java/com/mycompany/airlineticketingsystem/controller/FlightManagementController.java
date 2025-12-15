/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;

public class FlightManagementController {

    // Inputs
    @FXML private TextField txtDepartCountry, txtArriveCountry;
    @FXML private DatePicker dateDepart, dateArrive;
    @FXML private TextField txtDepartTime, txtArriveTime;
    @FXML private ComboBox<Plane> comboPlane; // CHANGED to ComboBox
    @FXML private TextField txtEcoPrice, txtBizPrice;
    @FXML private Button btnAction; 
    @FXML private Label lblStatus; // Progress indicator
    
    // Table
    @FXML private TableView<Flight> tableFlights;
    @FXML private TableColumn<Flight, String> colId, colRoute, colDate, colPlane, colPrice;
    @FXML private TableColumn<Flight, String> colAvailable, colBooked;
    @FXML private TableColumn<Flight, Void> colAction;

    private final FlightServiceImpl flightService = new FlightServiceImpl();
    private boolean isEditMode = false;

    // --- VALIDATION REGEX ---
    // Allow letters, spaces, hyphens, parentheses, apostrophes. No numbers.
    private static final Pattern COUNTRY_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-\\(\\)\\']+$");
    // Standard Time HH:mm (00:00 to 23:59)
    private static final Pattern TIME_PATTERN = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    // Prices (Numbers and decimals only)
    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");

    @FXML
    public void initialize() {
        tableFlights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 1. Setup Table Columns
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFlightId()));
        colRoute.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartCountry() + " ➝ " + c.getValue().getArriveCountry()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFormattedTime()));
        colPlane.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlaneId()));
        colPrice.setCellValueFactory(c -> new SimpleStringProperty("RM" + c.getValue().getPriceEconomy() + " / RM" + c.getValue().getPriceBusiness()));

        // Seat Counts (Note: This still runs on UI thread per row, might need optimization if 1000s of flights)
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

        // 2. Setup ComboBox Converter (Display Plane ID + Model)
        comboPlane.setConverter(new StringConverter<Plane>() {
            @Override
            public String toString(Plane p) {
                return p == null ? "" : p.getPlaneId() + " (" + p.getModel() + ")";
            }
            @Override
            public Plane fromString(String string) { return null; }
        });

        // 3. Init Data
        addButtonToTable();
        loadPlanes(); // Load Dropdown data
        handleRefresh(); // Load Table data
    }

    // --- DATA LOADING WITH THREADING (Fixes Lag) ---
    private void loadPlanes() {
        Task<List<Plane>> task = new Task<>() {
            @Override protected List<Plane> call() {
                return flightService.getAllPlanes();
            }
        };
        task.setOnSucceeded(e -> comboPlane.getItems().setAll(task.getValue()));
        new Thread(task).start();
    }

    @FXML
    private void handleRefresh() {
        lblStatus.setText("Loading data...");
        
        Task<List<Flight>> task = new Task<>() {
            @Override protected List<Flight> call() {
                // FORCE REFRESH from DB
                ((FlightServiceImpl) flightService).refreshCache(); 
                return flightService.getAllFlights();
            }
        };

        task.setOnSucceeded(e -> {
            tableFlights.setItems(FXCollections.observableArrayList(task.getValue()));
            lblStatus.setText("Data loaded.");
        });

        task.setOnFailed(e -> {
            lblStatus.setText("Error loading data.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    // --- VALIDATION & CRUD ---
    @FXML
    private void handleCreateOrUpdate() {
        try {
            // 1. Validate Inputs
            if (!validateInput()) return;

            LocalDateTime departDT = LocalDateTime.of(dateDepart.getValue(), LocalTime.parse(txtDepartTime.getText()));
            LocalDateTime arriveDT = LocalDateTime.of(dateArrive.getValue(), LocalTime.parse(txtArriveTime.getText()));
            Plane selectedPlane = comboPlane.getValue();

            // 2. Build Flight
            Flight flight = new Flight.Builder()
                .route(txtDepartCountry.getText(), txtArriveCountry.getText())
                .timing(departDT, arriveDT)
                .prices(Double.parseDouble(txtEcoPrice.getText()), Double.parseDouble(txtBizPrice.getText()))
                .plane(selectedPlane.getPlaneId())
                .build();

            if (isEditMode) {
                flight.setFlightId(tableFlights.getSelectionModel().getSelectedItem().getFlightId());
                flightService.updateFlight(flight);
                resetForm();
                new Alert(Alert.AlertType.INFORMATION, "Flight Updated!").show();
            } else {
                flight.setFlightId("F" + System.currentTimeMillis() % 10000);
                flightService.createFlight(flight, selectedPlane);
                new Alert(Alert.AlertType.INFORMATION, "Flight Created!").show();
            }
            handleRefresh();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private boolean validateInput() {
        StringBuilder error = new StringBuilder();

        if (!COUNTRY_PATTERN.matcher(txtDepartCountry.getText()).matches()) error.append("- Depart Country: Words only.\n");
        if (!COUNTRY_PATTERN.matcher(txtArriveCountry.getText()).matches()) error.append("- Arrive Country: Words only.\n");
        
        if (!TIME_PATTERN.matcher(txtDepartTime.getText()).matches()) error.append("- Depart Time: Must be HH:mm (00:00-23:59).\n");
        if (!TIME_PATTERN.matcher(txtArriveTime.getText()).matches()) error.append("- Arrive Time: Must be HH:mm (00:00-23:59).\n");
        
        if (!PRICE_PATTERN.matcher(txtEcoPrice.getText()).matches()) error.append("- Eco Price: Invalid Number.\n");
        if (!PRICE_PATTERN.matcher(txtBizPrice.getText()).matches()) error.append("- Biz Price: Invalid Number.\n");

        if (comboPlane.getValue() == null) error.append("- Please select a plane.\n");

        if (error.length() > 0) {
            new Alert(Alert.AlertType.WARNING, "Validation Error:\n" + error.toString()).show();
            return false;
        }
        return true;
    }
    
    // ... [Keep existing addButtonToTable(), fillFormForEdit(), deleteFlight() methods] ...
    
    // Updated fillFormForEdit to select combo box item
    private void fillFormForEdit(Flight f) {
        txtDepartCountry.setText(f.getDepartCountry());
        txtArriveCountry.setText(f.getArriveCountry());
        txtDepartTime.setText(f.getDepartTime().toLocalTime().toString());
        txtArriveTime.setText(f.getArriveTime().toLocalTime().toString());
        dateDepart.setValue(f.getDepartTime().toLocalDate());
        dateArrive.setValue(f.getArriveTime().toLocalDate());
        txtEcoPrice.setText(f.getPriceEconomy().toString());
        txtBizPrice.setText(f.getPriceBusiness().toString());
        
        // Select the correct plane in ComboBox
        for (Plane p : comboPlane.getItems()) {
            if (p.getPlaneId().equals(f.getPlaneId())) {
                comboPlane.setValue(p);
                break;
            }
        }
        comboPlane.setDisable(true); // Cannot change plane when editing (affects seats)
        
        btnAction.setText("Update Flight");
        btnAction.setStyle("-fx-background-color: orange; -fx-text-fill: black;");
        isEditMode = true;
    }
    
    private void resetForm() {
        btnAction.setText("Create Flight & Generate Seats");
        btnAction.setStyle("");
        comboPlane.setDisable(false);
        isEditMode = false;
        // Clear fields logic...
    }
    
    // ... Copy addButtonToTable and deleteFlight from previous answer ...
    
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

    private void deleteFlight(Flight f) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Flight " + f.getFlightId() + "? This will delete all seat data.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            flightService.deleteFlight(f.getFlightId());
            handleRefresh();
        }
    }
}