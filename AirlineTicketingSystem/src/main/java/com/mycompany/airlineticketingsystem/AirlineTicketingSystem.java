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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class AirlineTicketingSystem extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Keep a reference to the stage

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // 1. Load your initial view (Change "FlightDashboard" to "Login" later when ready)
        Parent root = loadFXML("Home");
        
        // 2. Get the screen size of the computer running the app
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // 3. Create the scene with the screen's dimensions
        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        stage.setScene(scene);
        stage.setTitle("Airline Ticketing System");

        // --- AUTO-SIZE CONFIGURATION ---
        
        // OPTION A: Maximized Window (Recommended for this Assignment)
        // This fills the available space but keeps the taskbar/title bar visible.
        stage.setMaximized(true);

        // OPTION B: True Full Screen (Like a Game)
        // Uncomment the line below if you want to hide the taskbar completely.
        // stage.setFullScreen(true);

        stage.show();
    }

    // Helper method to switch views (e.g., from Login -> Dashboard)
    public static void setRoot(String fxml) throws IOException {
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
