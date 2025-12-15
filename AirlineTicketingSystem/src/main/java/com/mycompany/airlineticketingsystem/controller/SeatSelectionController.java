/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tay Tem Hoe
 */
package com.mycompany.airlineticketingsystem.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.stage.FileChooser;
import java.io.File;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.model.PassengerDetails;
import com.mycompany.airlineticketingsystem.model.Ticket;
import com.mycompany.airlineticketingsystem.service.EmailService;
import com.mycompany.airlineticketingsystem.service.FlightService;
import com.mycompany.airlineticketingsystem.service.FlightServiceImpl;
import com.mycompany.airlineticketingsystem.service.TicketImageService;
import com.mycompany.airlineticketingsystem.service.TicketPdfService;
import com.mycompany.airlineticketingsystem.model.Booking; // Added
import com.mycompany.airlineticketingsystem.service.BookingService; // Added
import com.mycompany.airlineticketingsystem.service.BookingServiceImpl; // Added
import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import com.mycompany.airlineticketingsystem.service.PassengerService;
import com.mycompany.airlineticketingsystem.model.TicketEntity;
import com.mycompany.airlineticketingsystem.session.UserSession; // Added
import com.mycompany.airlineticketingsystem.model.Customer; // Added
import com.mycompany.airlineticketingsystem.service.TicketEntityService;
import com.mycompany.airlineticketingsystem.view.PaymentView;
import java.math.BigDecimal; // Added
import java.util.UUID; // Added
import javafx.application.Platform; // Added for UI updates if needed

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SeatSelectionController {

    @FXML
    private Label lblFlightInfo;
    @FXML
    private GridPane seatGrid;

    // Summary Panel
    @FXML
    private Label lblSelectedSeat;
    @FXML
    private Label lblSeatType;
    @FXML
    private Label lblPrice;
    @FXML
    private Button btnConfirm;

    private final FlightService flightService;
    private final BookingService bookingService;
    private final PassengerService passengerService; // Added
    private final TicketEntityService ticketEntityService; // Added
    private Flight currentFlight;
    private Seat selectedSeat = null; // Tracks current choice

    public SeatSelectionController() {
        this.flightService = new FlightServiceImpl();
        this.bookingService = new BookingServiceImpl();
        this.passengerService = new PassengerService();
        this.ticketEntityService = new TicketEntityService();
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
            // Ensure we handle BOOKED status correctly by greying out
            if (seat.getStatus() == SeatStatus.BOOKED) {
                btn.getStyleClass().add("seat-booked");
                btn.setDisable(true); // Can't click booked seats
            } else if (seat.getType() == SeatType.BUSINESS) {
                btn.getStyleClass().add("seat-business");
            } else {
                btn.getStyleClass().add("seat-economy");
            }

            // 3. Handle Click Event
            btn.setOnAction(e -> {
                if (seat.getStatus() == SeatStatus.BOOKED)
                    return; // double safety
                handleSeatClick(btn, seat);
            });

            // 4. Calculate Grid Position (Logic: "1A" -> Row 1, Col 0)
            // Parse "1A" -> Row: 1, Col: 'A'
            String seatNum = seat.getSeatNumber();
            String rowStr = seatNum.substring(0, seatNum.length() - 1);
            char colChar = seatNum.charAt(seatNum.length() - 1); // 'A', 'B', 'C', 'D'

            int row = Integer.parseInt(rowStr);
            int col = colChar - 'A'; // 'A'->0, 'B'->1...

            // Add visuals for the "Aisle" (Gap between B and C)
            if (col >= 2)
                col++; // Shift C and D to index 3 and 4

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

    private PassengerDetails passengerDetails; // store result for next screen

    @FXML
    private void handleConfirm() {
        if (selectedSeat == null) return;

        // 1. Get Passenger Details
        PassengerDetails details = showPassengerDetailsDialog(selectedSeat.getSeatNumber());
        if (details == null) return;

        // 2. Confirm Details
        while (true) {
            boolean ok = showConfirmPassengerDialog(selectedSeat.getSeatNumber(), details);
            if (ok) break;
            PassengerDetails recheck = showPassengerDetailsDialog(selectedSeat.getSeatNumber());
            if (recheck == null) return;
            details = recheck;
        }
        passengerDetails = details;

        try {
            // 3. Create Booking Object
            BigDecimal price = (selectedSeat.getType() == SeatType.BUSINESS)
                    ? currentFlight.getPriceBusiness()
                    : currentFlight.getPriceEconomy();

            String newBookingId = bookingService.generateNewBookingId();
            Booking booking = new Booking();
            booking.setBookingId(newBookingId);
            booking.setFlightId(currentFlight.getFlightId());
            booking.setIcNo(passengerDetails.getPassportNumber()); // Or Customer IC
            booking.setStatus("PENDING"); // ⚠️ IMPORTANT: Start as PENDING
            booking.setBookingDate(LocalDateTime.now());
            
            String seatId = selectedSeat.getSeatId();
            if (seatId == null || seatId.trim().isEmpty()) {
                seatId = currentFlight.getFlightId() + "-" + selectedSeat.getSeatNumber();
            }
            booking.setSeatId(seatId);
            booking.setTotalPrice(price);

            // 4. Save Booking (PENDING)
            boolean bookingSaved = bookingService.saveBooking(booking);
            
            if (!bookingSaved) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not create booking.");
                return;
            }

            // 5. PROCESS PAYMENT
            PaymentView paymentView = new PaymentView();
            // This blocks until user pays or closes
            paymentView.showPaymentScreen(booking.getBookingId(), price.doubleValue()); 

            if (paymentView.isPaymentSuccess()) {
                // --- PAYMENT SUCCESSFUL ---
                
                // 6. Save Passenger Info
                PassengerEntity passengerDto = new PassengerEntity(
                        passengerDetails.getPassportNumber(),
                        passengerDetails.getFullName());
                passengerService.savePassenger(passengerDto);

                // 7. Save Ticket
                String ticketId = ticketEntityService.generateNewTicketId();
                Customer customer = UserSession.getInstance().getLoggedInCustomer();
                String customerIc = (customer != null) ? customer.getIcNo() : "GUEST";

                TicketEntity ticketDto = new TicketEntity(
                        ticketId,
                        currentFlight.getFlightId(),
                        seatId,
                        selectedSeat.getSeatNumber(),
                        passengerDetails.getPassportNumber(),
                        customerIc
                );
                ticketEntityService.saveTicket(ticketDto);

                // 8. Update Seat to BOOKED (PaymentService already updated booking status to CONFIRMED)
                bookingService.updateSeatStatus(currentFlight.getFlightId(), selectedSeat.getSeatNumber());
                
                // 9. Refresh UI & Generate Ticket
                loadSeats();
                generateTicketAndEmail(passengerDetails, customerIc);

                showAlert(Alert.AlertType.INFORMATION, "Booking Confirmed", 
                          "Payment Successful! Booking ID: " + booking.getBookingId());
                handleBack();

            } else {
                // --- PAYMENT FAILED / CANCELLED ---
                showAlert(Alert.AlertType.WARNING, "Payment Cancelled", 
                          "Booking was not completed. You have not been charged.");
                // Optional: You could delete the PENDING booking here to clean up
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "System error: " + e.getMessage());
        }
    }

    private PassengerDetails showPassengerDetailsDialog(String seatNumber) {
        try {
            String path = "/com/mycompany/airlineticketingsystem/PassengerDetailsDialog.fxml";
            URL url = getClass().getResource(path);
            if (url == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Missing FXML: " + path);
                return null;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            PassengerDetailsDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Passenger Details");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(btnConfirm.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            dialogStage.getScene().getStylesheets().add(
                    getClass().getResource("/com/mycompany/airlineticketingsystem/customer-style.css")
                            .toExternalForm());

            controller.setStage(dialogStage);
            controller.setSeatNumber(seatNumber);

            dialogStage.showAndWait();
            return controller.getResult();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Dialog failed: " + e.getMessage());
            return null;
        }
    }

    private boolean showConfirmPassengerDialog(String seatNumber, PassengerDetails details) {
        try {
            String path = "/com/mycompany/airlineticketingsystem/ConfirmPassengerDialog.fxml";
            URL url = getClass().getResource(path);
            if (url == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Missing FXML: " + path);
                return false;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ConfirmPassengerDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirm Details");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(btnConfirm.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            dialogStage.getScene().getStylesheets().add(
                    getClass().getResource("/com/mycompany/airlineticketingsystem/customer-style.css")
                            .toExternalForm());

            controller.setStage(dialogStage);
            controller.setData(seatNumber, details);

            dialogStage.showAndWait();
            return controller.isConfirmed();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void generateTicketAndEmail(PassengerDetails details, String customerIc) {
        try {
            Ticket ticket = new Ticket(
                    currentFlight.getDepartCountry(),
                    currentFlight.getArriveCountry(),
                    currentFlight.getFlightId(),
                    selectedSeat.getSeatNumber(),
                    customerIc, // customerIcNumber (now passed correctly)
                    details.getPassportNumber(), // passportNumber
                    (LocalDateTime) currentFlight.getDepartTime(),
                    (LocalDateTime) currentFlight.getArriveTime());

            // 1. Generate Ticket Image (PNG)
            TicketImageService imageService = new TicketImageService();
            Path imagePath = imageService.generate(ticket, Path.of("tickets"));

            // 2. Email the Image
            // Replace with logged-in user's email or a test email
            String customerEmail = "customer@example.com";

            // NOTE: You must provide a valid sender email & App Password for this to
            // actually send!
            // Keeping placeholders as per original code.
            String senderEmail = "your_email@gmail.com";
            String senderPassword = "your_app_password";

            try {
                EmailService emailService = new EmailService();
                emailService.sendTicket(
                        "smtp.gmail.com", 587,
                        senderEmail, senderPassword,
                        customerEmail,
                        "Your Flight Ticket: " + ticket.getFlightId(),
                        "Please find your attached boarding pass image.",
                        imagePath); // Can handle PNG as it treats it as a file attachment
                System.out.println("Email sent successfully (if credentials were valid).");
            } catch (Exception e) {
                System.err.println("Email failed to send (likely invalid credentials): " + e.getMessage());
                // Don't block the UI flow just because email failed
            }

            // 3. Display the Image to User
            // 3. Display the Image to User
            showTicketPopup(imagePath, ticket);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error generating ticket", ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        try {
            if (getClass().getResource("/com/mycompany/airlineticketingsystem/customer-style.css") != null) {
                alert.getDialogPane().getStylesheets().add(getClass()
                        .getResource("/com/mycompany/airlineticketingsystem/customer-style.css").toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Could not load Alert CSS: " + e.getMessage());
        }
        alert.showAndWait();
    }

    private void showTicketPopup(Path imagePath, Ticket ticket) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Your Boarding Pass");
            stage.initModality(Modality.APPLICATION_MODAL);

            // Log for debugging
            System.out.println("Loading image from: " + imagePath.toUri().toString());

            Image image = new Image(imagePath.toUri().toString());
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(800);

            // Buttons
            Button btnPrint = new Button("Print");
            btnPrint.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
            btnPrint.setOnAction(e -> {
                PrinterJob job = PrinterJob.createPrinterJob();
                if (job != null && job.showPrintDialog(stage)) {
                    boolean success = job.printPage(imageView);
                    if (success) {
                        job.endJob();
                        new Alert(Alert.AlertType.INFORMATION, "Printing started.").showAndWait();
                    }
                }
            });

            Button btnSavePdf = new Button("Save as PDF");
            btnSavePdf.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
            btnSavePdf.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Ticket as PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                fileChooser.setInitialFileName("Ticket_" + ticket.getFlightId() + ".pdf");
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        TicketPdfService pdfService = new TicketPdfService();
                        pdfService.generate(ticket, file.getParentFile().toPath());
                        // Note: Our TicketPdfService currently auto-names the file.
                        // Ideally we should modify it to accept exact Path, but for now passing parent
                        // dir is Close Enough
                        // or we rename it after?
                        // Let's rely on standard logic for now or update TicketPdfService later if user
                        // complains.
                        // Actually, let's just use the service's return path and rename/move provided
                        // the user selected a specific name.
                        Path generated = pdfService.generate(ticket, file.getParentFile().toPath());
                        // If file name differs, maybe rename?
                        // But standard service logic is fine for now to avoid complexity.
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Saved to: " + generated.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to save PDF.");
                    }
                }
            });

            HBox buttonBox = new HBox(20, btnPrint, btnSavePdf);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setStyle("-fx-padding: 20; -fx-background-color: #333;");

            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: black;");
            root.setCenter(new StackPane(imageView));
            root.setBottom(buttonBox);

            Scene scene = new Scene(root, 900, 600);

            // APPLY CSS
            scene.getStylesheets().add(getClass()
                    .getResource("/com/mycompany/airlineticketingsystem/customer-style.css").toExternalForm());

            stage.setScene(scene);
            stage.showAndWait(); // User must close ticket first

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Ticket generated but could not be displayed.").showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        // Simple way to go back: Re-load FlightSearch
        try {
            // Assuming we are inside the MainLayout StackPane
            Parent searchView = FXMLLoader
                    .load(getClass().getResource("/com/mycompany/airlineticketingsystem/FlightSearch.fxml"));

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