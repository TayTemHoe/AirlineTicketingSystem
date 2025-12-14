package com.mycompany.airlineticketingsystem.view;

import com.mycompany.airlineticketingsystem.service.PaymentService;
import com.mycompany.airlineticketingsystem.model.Payment;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentView {

    private PaymentService paymentService = new PaymentService();
    
    // Data
    private String selectedPaymentMethod = "Credit Card"; 
    
    // UI Controls
    private VBox cardBox, debitBox, walletBox;
    private Label lblDynamicInput; 
    private Label lblName; // Made global so we can change the text dynamically
    private TextField txtDynamicInput;
    private TextField txtName; 
    private Stage stage;

    public void showPaymentScreen(int bookingID, double totalAmount) {
        stage = new Stage();
        stage.setTitle("Payment");

        // 1. ROOT CONTAINER
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        
        // 2. THE MAIN CARD
        HBox mainCard = new HBox();
        mainCard.getStyleClass().add("payment-card");

        // ============================================
        // LEFT PANE: INPUTS
        // ============================================
        VBox leftPane = new VBox(15);
        leftPane.getStyleClass().add("left-pane");

        // Title
        Label lblTitle = new Label("Payment");
        lblTitle.getStyleClass().add("main-title");

        // Payment Methods (Images)
        HBox methodContainer = new HBox(10);
        methodContainer.setAlignment(Pos.CENTER_LEFT);
        
        // ⚠️ UPDATED: Using the filenames you provided
        cardBox = createOptionBox("Credit Card", "credit_card.png");
        debitBox = createOptionBox("Debit Card", "debit_card.jpg");
        walletBox = createOptionBox("E-Wallet", "ewallet.png");

        // Logic for clicking
        cardBox.setOnMouseClicked(e -> selectMethod("Credit Card", cardBox));
        debitBox.setOnMouseClicked(e -> selectMethod("Debit Card", debitBox));
        walletBox.setOnMouseClicked(e -> selectMethod("E-Wallet", walletBox));
        
        methodContainer.getChildren().addAll(cardBox, debitBox, walletBox);

        // Input Fields
        // A. Name Field (Now Global variable 'lblName')
        lblName = new Label("Cardholder Name");
        lblName.getStyleClass().add("input-label");
        txtName = new TextField();
        txtName.setPromptText("Enter name");

        // B. Dynamic Field (Card No OR Phone No)
        lblDynamicInput = new Label("Card Number");
        lblDynamicInput.getStyleClass().add("input-label");
        txtDynamicInput = new TextField();
        txtDynamicInput.setPromptText("xxxx-xxxx-xxxx-xxxx");

        // Pay Button
        Button btnPay = new Button("Confirm Payment");
        btnPay.setMaxWidth(Double.MAX_VALUE);
        btnPay.getStyleClass().add("pay-button");
        VBox.setMargin(btnPay, new Insets(20, 0, 0, 0)); 

        btnPay.setOnAction(e -> handlePayment(bookingID, totalAmount));

        // Add everything to left pane
        leftPane.getChildren().addAll(lblTitle, methodContainer, lblName, txtName, lblDynamicInput, txtDynamicInput, btnPay);

        // ============================================
        // VERTICAL SEPARATOR
        // ============================================
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        // ============================================
        // RIGHT PANE: AMOUNT SUMMARY
        // ============================================
        VBox rightPane = new VBox(10);
        rightPane.getStyleClass().add("right-pane");
        rightPane.setAlignment(Pos.CENTER_LEFT);

        Label lblSummary = new Label("Total to pay");
        lblSummary.getStyleClass().add("summary-title");

        Label lblTotal = new Label(String.format("RM %.2f", totalAmount));
        lblTotal.getStyleClass().add("total-amount");

        rightPane.getChildren().addAll(lblSummary, lblTotal);

        // ============================================
        // ASSEMBLE
        // ============================================
        
        // Apply Default Selection (Credit Card)
        selectMethod("Credit Card", cardBox);

        mainCard.getChildren().addAll(leftPane, separator, rightPane);
        root.getChildren().add(mainCard);

        Scene scene = new Scene(root, 900, 600);
        
        // LOAD CSS
        try {
            scene.getStylesheets().add(getClass().getResource("payment.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("⚠️ CSS Error: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.show();
    }

    // --- LOGIC: SWITCHING METHODS ---
    private void selectMethod(String method, VBox selectedBox) {
        this.selectedPaymentMethod = method;
        
        // Reset Styles (Check if objects exist first)
        if (cardBox != null) cardBox.getStyleClass().remove("selected-option");
        if (debitBox != null) debitBox.getStyleClass().remove("selected-option");
        if (walletBox != null) walletBox.getStyleClass().remove("selected-option");
        
        selectedBox.getStyleClass().add("selected-option");

        // Change Labels based on selection
        if (lblDynamicInput != null && txtDynamicInput != null && lblName != null) {
            if (method.equals("E-Wallet")) {
                // ⚠️ UPDATED: Ask for "Name" and "Phone Number"
                lblName.setText("Name");
                lblDynamicInput.setText("Phone Number");
                txtDynamicInput.setPromptText("01x-xxxxxxx");
            } else {
                // Ask for "Cardholder Name" and "Card Number"
                lblName.setText("Cardholder Name");
                lblDynamicInput.setText("Card Number");
                txtDynamicInput.setPromptText("xxxx-xxxx-xxxx-xxxx");
            }
            txtDynamicInput.clear();
        }
    }

    // --- LOGIC: HANDLE PAY BUTTON ---
    private void handlePayment(int bookingID, double amount) {
        String input = txtDynamicInput.getText();
        String name = txtName.getText();

        if (input.isEmpty() || name.isEmpty()) {
            showAlert("Missing Details", "Please fill in all fields.");
            return;
        }

        String last4 = input.length() > 4 ? input.substring(input.length() - 4) : input;
        
        Payment p = new Payment(bookingID, amount, last4, selectedPaymentMethod);

        boolean success = paymentService.processPayment(p);
        if (success) {
            showAlert("Success", "Payment Approved! Enjoy your flight.");
            stage.close();
        } else {
            showAlert("Failed", "Payment Declined. Please try again.");
        }
    }

    // --- UI HELPER: IMAGE BOX ---
    private VBox createOptionBox(String name, String imageName) {
        VBox box = new VBox(5);
        box.getStyleClass().add("payment-option-box");

        ImageView icon = new ImageView();
        try {
            // ⚠️ UPDATED: Using the absolute standard resource path "/assignment/images/"
            String path = "/assignment/images/" + imageName; 
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                icon.setImage(new Image(is));
                icon.setFitWidth(40);
                icon.setFitHeight(40);
                icon.setPreserveRatio(true);
            } else {
                System.out.println("❌ Image not found: " + path);
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }

        Label lbl = new Label(name);
        lbl.getStyleClass().add("option-label");

        box.getChildren().addAll(icon, lbl);
        return box;
    }

    private void showAlert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}