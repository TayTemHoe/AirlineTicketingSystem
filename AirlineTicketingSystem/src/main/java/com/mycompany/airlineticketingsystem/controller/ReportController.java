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
        sb.append("=== COMPANY REVENUE REPORT ===\n\n");
        
        Map<String, Double> revenue = reportService.getRevenueByMethod();
        double totalEarnings = 0.0;
        
        if (revenue.isEmpty()) {
            sb.append("No revenue data available.\n");
        } else {
            for (Map.Entry<String, Double> entry : revenue.entrySet()) {
                sb.append(String.format("Method: %-15s | Total: RM %.2f\n", entry.getKey(), entry.getValue()));
                totalEarnings += entry.getValue();
            }
            sb.append("\n------------------------------------------------\n");
            sb.append(String.format("TOTAL EARNINGS:         RM %.2f\n", totalEarnings));
            sb.append("------------------------------------------------\n");
        }
        
        txtSummary.setText(sb.toString());

        // 2. Load Charts
        
        // A. Revenue Bar
        barRevenue.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue by Method");
        revenue.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));
        barRevenue.getData().add(series);

        // B. Destinations Pie
        pieDestinations.getData().clear();
        Map<String, Integer> destinations = reportService.getTopDestinations();
        destinations.forEach((k, v) -> 
            pieDestinations.getData().add(new PieChart.Data(k + " (" + v + ")", v)));

        // C. Gender Pie
        pieGender.getData().clear();
        Map<String, Integer> genderDist = reportService.getCustomerGenderDistribution();
        genderDist.forEach((k, v) -> 
            pieGender.getData().add(new PieChart.Data(k + " (" + v + ")", v)));
    }
    
    
}