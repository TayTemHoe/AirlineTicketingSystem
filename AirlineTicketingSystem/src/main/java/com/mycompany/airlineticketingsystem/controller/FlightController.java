package com.mycompany.airlineticketingsystem.controller;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;

public class FlightController {

    // --- FXML UI Components ---
    @FXML private TableView<Flight> flightTable;
    @FXML private TableColumn<Flight, String> colId;
    @FXML private TableColumn<Flight, String> colFrom;
    @FXML private TableColumn<Flight, String> colTo;
    @FXML private TableColumn<Flight, LocalDateTime> colDepart;
    @FXML private TableColumn<Flight, LocalDateTime> colArrive;
    @FXML private TableColumn<Flight, String> colPlane;

    // --- Service Dependencies ---
    private final FlightService flightService;
    
    // Data for the Table
    private final ObservableList<Flight> flightData = FXCollections.observableArrayList();

    // Constructor (JavaFX calls this first if you use a custom factory, but we init service here)
    public FlightController() {
        // In a real Spring app, this would be @Autowired
        this.flightService = new FlightServiceImpl();
    }

    // --- Initialization ---
    @FXML
    public void initialize() {
        // 1. Link Table Columns to Flight Object properties
        // These names must match the variable names in Flight.java exactly!
        colId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        colFrom.setCellValueFactory(new PropertyValueFactory<>("departCountry"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("arriveCountry"));
        colDepart.setCellValueFactory(new PropertyValueFactory<>("departTime"));
        colArrive.setCellValueFactory(new PropertyValueFactory<>("arriveTime"));
        colPlane.setCellValueFactory(new PropertyValueFactory<>("planeId"));

        // 2. Load Data
        loadFlights();
    }

    @FXML
    private void handleRefresh() {
        loadFlights();
    }

    @FXML
    private void handleAddFlight() {
        System.out.println("Switching to Add Flight Screen... (To be implemented)");
        // createDummyFlight(); // Uncomment this to test "Create" logic immediately
    }

    private void loadFlights() {
        flightData.clear();
        flightData.addAll(flightService.getAllFlights());
        flightTable.setItems(flightData);
    }
    
    // Temporary helper to test your "Create Flight + Generate Seats" logic
    private void createDummyFlight() {
        try {
            // Create a Dummy Plane (Make sure this plane exists in DB first!)
            Plane p = new Plane("PL01", "Boeing 737", 60); 
            
            // Use Builder Pattern
            Flight f = new Flight.Builder()
                    .id("F" + System.currentTimeMillis() % 1000) // Random ID
                    .route("Malaysia", "Japan")
                    .timing(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(7))
                    .prices(500.0, 1200.0)
                    .plane(p.getPlaneId())
                    .build();

            // This calls your Service -> Repository -> Database
            flightService.createFlight(f, p);
            
            // Refresh UI
            loadFlights();
            System.out.println("Dummy Flight Created!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

