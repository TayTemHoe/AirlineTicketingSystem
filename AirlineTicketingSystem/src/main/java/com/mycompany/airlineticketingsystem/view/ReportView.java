package com.mycompany.airlineticketingsystem.view;

import com.mycompany.airlineticketingsystem.service.ReportService;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportView {

    private ReportService reportService = new ReportService();

    public void showReportScreen() {
        Stage stage = new Stage();
        stage.setTitle("Admin Reporting Studio");

        // 1. Setup Layout
        BorderPane root = new BorderPane();
        
        // 2. Header
        Label header = new Label("Admin Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20;");
        root.setTop(header);

        // 3. Create Tabs
        TabPane tabPane = new TabPane();
        
        // --- Tab 1: Financials ---
        Tab financeTab = new Tab("Financials", createFinanceTab());
        financeTab.setClosable(false);

        // --- Tab 2: Flight Ops ---
        Tab opsTab = new Tab("Flight Ops", createOpsTab());
        opsTab.setClosable(false);

        // --- Tab 3: Customers ---
        Tab custTab = new Tab("Customers", createCustomerTab());
        custTab.setClosable(false);

        tabPane.getTabs().addAll(financeTab, opsTab, custTab);
        root.setCenter(tabPane);

        // 4. Create Scene & Attach CSS
        Scene scene = new Scene(root, 1000, 700);
        
        // ⚠️ CRITICAL: This loads your report.css file
        try {
            scene.getStylesheets().add(getClass().getResource("report.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("⚠️ Could not find report.css! Make sure it is in the same folder.");
        }

        stage.setScene(scene);
        stage.show();
    }

    // --- HELPER: Tab 1 Content ---
    private HBox createFinanceTab() {
        HBox layout = new HBox(20);
        layout.setPadding(new Insets(20));

        // Chart 1: Revenue Bar Chart
        VBox chart1 = createBarChart("Total Revenue (RM)", reportService.getRevenueByMethod());
        
        // Chart 2: Revenue Pie Chart (Just for variety)
        VBox chart2 = createPieChart("Revenue Distribution", reportService.getRevenueByMethod());

        layout.getChildren().addAll(chart1, chart2);
        return layout;
    }

    // --- HELPER: Tab 2 Content ---
    private HBox createOpsTab() {
        HBox layout = new HBox(20);
        layout.setPadding(new Insets(20));

        // Chart 1: Destinations
        VBox chart1 = createPieChart("Top Destinations", reportService.getTopDestinations());

        // Chart 2: Seat Class (Biz vs Eco)
        VBox chart2 = createBarChart("Seat Class Preference", reportService.getSeatClassDistribution());

        layout.getChildren().addAll(chart1, chart2);
        return layout;
    }

    // --- HELPER: Tab 3 Content ---
    private HBox createCustomerTab() {
        HBox layout = new HBox(20);
        layout.setPadding(new Insets(20));

        // Chart 1: Gender
        VBox chart1 = createPieChart("Customer Demographics", reportService.getCustomerGenderDistribution());

        layout.getChildren().add(chart1);
        return layout;
    }

    // --- CHART GENERATOR: Bar Chart ---
    private VBox createBarChart(String title, Map<String, ? extends Number> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chart.getData().add(series);

        return wrapInCard(chart);
    }

    // --- CHART GENERATOR: Pie Chart ---
    private VBox createPieChart(String title, Map<String, ? extends Number> data) {
        PieChart chart = new PieChart();
        chart.setTitle(title);

        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            chart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
        }

        return wrapInCard(chart);
    }

    // --- CSS HELPER: Wrap chart in a white "Card" ---
    private VBox wrapInCard(Chart chart) {
        VBox card = new VBox(chart);
        card.getStyleClass().add("chart-card"); // <--- This uses the CSS class
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }
}