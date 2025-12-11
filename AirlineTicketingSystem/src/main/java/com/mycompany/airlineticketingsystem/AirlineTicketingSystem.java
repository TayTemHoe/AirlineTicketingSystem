/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.airlineticketingsystem;

/**
 *
 * @author Tay Tem Hoe
 */

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
        // Load the Login.fxml file we just created
        scene = new Scene(loadFXML("Login"), 640, 480);
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
