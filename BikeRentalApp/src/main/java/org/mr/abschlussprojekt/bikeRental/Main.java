package org.mr.abschlussprojekt.bikeRental;

import javafx.application.Application;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.logic.ChangeSceneManager;

import java.io.IOException;

/**
 * Main class of the Bike Rental application.
 * This class serves as the entry point for the application,
 * extending the JavaFX Application class to create the GUI.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Switch to the login form using the ChangeSceneManager
        ChangeSceneManager.getInstance().switchToLoginForm(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}