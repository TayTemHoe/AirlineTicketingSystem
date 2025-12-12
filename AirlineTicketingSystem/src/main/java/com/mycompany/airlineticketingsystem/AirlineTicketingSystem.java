/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.airlineticketingsystem;

/**
 *
 * @author Tay Tem Hoe
 */

// Uncomment and import when setting up database connection:
// import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AirlineTicketingSystem extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database connection
        // Get your JDBC URL from NetBeans: Services > Databases > Your Connection > Properties > JDBC URL
        // Then uncomment and set it here:
        // String jdbcUrl = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?user=postgres.ajdaciskaffuvaanizlw&password=YOUR_PASSWORD";
        // DatabaseConnection.setConnectionUrl(jdbcUrl);
        
        // Load the Login.fxml file we just created
        // Change "Login" to "FlightDashboard"
        scene = new Scene(loadFXML("FlightDashboard"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Airline Modernisation Project");
        stage.show();
    }

    // Helper method to switch scenes (You will use this a lot!)
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AirlineTicketingSystem.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void start() {
        launch();
    }
}
