/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.controller;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TextArea;
import java.util.Map;
import javafx.scene.chart.XYChart;

public class ReportController {

    @FXML private TextArea txtSummary;
    @FXML private PieChart pieDestinations;
    @FXML private BarChart<String, Number> barRevenue;
    @FXML private PieChart pieGender;

    private final ReportService reportService = new ReportService();

    @FXML
    public void initialize() {
        loadData();
    }

    @FXML
    private void loadData() {
        // 1. Load Text Summary (Total Earnings)
        StringBuilder sb = new StringBuilder();
        sb.append("=== REVENUE SUMMARY REPORT ===\n");
        
        Map<String, Double> revenue = reportService.getRevenueByMethod();
        double totalEarnings = 0.0;
        
        for (Map.Entry<String, Double> entry : revenue.entrySet()) {
            sb.append(String.format("Method: %-15s | Total: RM %.2f\n", entry.getKey(), entry.getValue()));
            totalEarnings += entry.getValue();
        }
        sb.append("------------------------------------------------\n");
        sb.append(String.format("TOTAL COMPANY EARNINGS:     RM %.2f\n", totalEarnings));
        
        txtSummary.setText(sb.toString());

        // 2. Load Charts
        // A. Revenue Bar
        barRevenue.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");
        revenue.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));
        barRevenue.getData().add(series);

        // B. Destinations Pie
        pieDestinations.getData().clear();
        reportService.getTopDestinations().forEach((k, v) -> 
            pieDestinations.getData().add(new PieChart.Data(k + " (" + v + ")", v)));

        // C. Gender Pie
        pieGender.getData().clear();
        reportService.getCustomerGenderDistribution().forEach((k, v) -> 
            pieGender.getData().add(new PieChart.Data(k, v)));
    }
}
