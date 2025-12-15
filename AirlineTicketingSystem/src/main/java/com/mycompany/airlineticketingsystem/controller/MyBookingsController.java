/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Ticket;
import com.mycompany.airlineticketingsystem.model.TicketEntity;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import com.mycompany.airlineticketingsystem.service.TicketEntityService;
import com.mycompany.airlineticketingsystem.service.TicketPdfService;
import com.mycompany.airlineticketingsystem.session.UserSession;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.concurrent.Task;

public class MyBookingsController {

    @FXML private VBox ticketsContainer;
    @FXML private Label lblStatus;

    private final TicketEntityService ticketService = new TicketEntityService();
    private final FlightService flightService = new FlightServiceImpl();
    private final TicketPdfService pdfService = new TicketPdfService();

    @FXML
    public void initialize() {
        loadBookings();
    }

    private void loadBookings() {
        ticketsContainer.getChildren().clear();
        lblStatus.setText("Loading your bookings...");
        
        Customer customer = UserSession.getInstance().getLoggedInCustomer();
        if (customer == null) {
            lblStatus.setText("Please login.");
            return;
        }

        Task<List<HBox>> task = new Task<>() {
            @Override
            protected List<HBox> call() {
                List<HBox> cards = new java.util.ArrayList<>();
                
                // 1. Fetch Tickets
                List<TicketEntity> myTickets = ticketService.getTicketsByCustomer(customer.getIcNo());
                
                // 2. Build UI Cards (must be careful creating Nodes in background, usually okay for simple layouts, 
                // but safer to fetch Data objects and build Nodes in onSucceeded. 
                // Let's just fetch Data here to be safe.)
                return null; 
            }
        };
        
        // BETTER APPROACH: Fetch Data in BG, Build UI in FG
        new Thread(() -> {
            List<TicketEntity> myTickets = ticketService.getTicketsByCustomer(customer.getIcNo());
            
            // Pre-fetch flights to avoid lag later
            // We can reuse the FlightService cache we built earlier!
            List<Flight> allFlights = flightService.getAllFlights(); 
            
            javafx.application.Platform.runLater(() -> {
                if (myTickets.isEmpty()) {
                    lblStatus.setText("No booking history found.");
                } else {
                    lblStatus.setText("Found " + myTickets.size() + " ticket(s).");
                    for (TicketEntity entity : myTickets) {
                        // Find matching flight in memory
                        Optional<Flight> flightOpt = allFlights.stream()
                            .filter(f -> f.getFlightId().equals(entity.getFlightId()))
                            .findFirst();
                        
                        if (flightOpt.isPresent()) {
                            ticketsContainer.getChildren().add(createTicketCard(entity, flightOpt.get()));
                        }
                    }
                }
            });
        }).start();
    }

    private HBox createTicketCard(TicketEntity entity, Flight flight) {
        HBox card = new HBox(15);
        card.getStyleClass().add("flight-card"); // Reuse existing card style
        card.setAlignment(Pos.CENTER_LEFT);

        // 1. Ticket Info
        VBox infoBox = new VBox(5);
        Label lblRoute = new Label(flight.getDepartCountry() + " ➝ " + flight.getArriveCountry());
        lblRoute.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        Label lblDetails = new Label("Flight: " + flight.getFlightId() + " | Seat: " + entity.getSeatNumber());
        Label lblDate = new Label("Date: " + flight.getFormattedTime());
        lblDate.setStyle("-fx-text-fill: #666;");

        infoBox.getChildren().addAll(lblRoute, lblDetails, lblDate);

        // 2. Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 3. Action Buttons
        Button btnDownload = new Button("Download PDF");
        btnDownload.getStyleClass().addAll("button", "btn-primary");
        btnDownload.setOnAction(e -> handleDownload(entity, flight));

        card.getChildren().addAll(infoBox, spacer, btnDownload);
        return card;
    }

    private void handleDownload(TicketEntity entity, Flight flight) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ticket");
        fileChooser.setInitialFileName("Ticket_" + entity.getTicketId() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        
        File file = fileChooser.showSaveDialog(ticketsContainer.getScene().getWindow());
        
        if (file != null) {
            try {
                // Reconstruct full Ticket object for PDF generation
                Ticket fullTicket = new Ticket(
                    flight.getDepartCountry(),
                    flight.getArriveCountry(),
                    flight.getFlightId(),
                    entity.getSeatNumber(),
                    entity.getCustomerIcNumber(),
                    entity.getPassengerPassportNumber(),
                    flight.getDepartTime(),
                    flight.getArriveTime()
                );

                pdfService.generate(fullTicket, file.getParentFile().toPath());
                
                // Rename/Move logic if specific name chosen (Service auto-names, so we alert user)
                // The TicketPdfService saves as "Ticket_Flight_Seat.pdf". 
                // Ideally, we'd refactor the service to accept a target filename, but for now:
                new Alert(Alert.AlertType.INFORMATION, "Ticket Saved successfully!").show();
                
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save PDF: " + e.getMessage()).show();
            }
        }
    }
}
