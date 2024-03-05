package org.mr.abschlussprojekt.bikeRental.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.AppState;
import org.mr.abschlussprojekt.bikeRental.logic.ChangeSceneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the User Menu UI. This class handles navigation between different views
 * within the user's dashboard, such as home, activity, profile, and logout.
 */
public class UserMenuController implements Initializable {


    public static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #003366; -fx-text-fill: #ffffff;";


    /*
    FXML UI Elements---------------------------------------------------------*/

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button homeButton;
    @FXML
    private Button activityButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Circle databaseLight;


   /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It sets the initial state of the UI.
     */
    @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
    Attributes---------------------------------------------------------------*/
        DatabaseService databaseService = new DatabaseService();

        // Set initial button style for selected view
       homeButton.setStyle(ACTIVE_BUTTON_STYLE);

        boolean isDbConnected = databaseService.isDatabaseConnected();
        // Update database connection indicator light
        databaseLight.setFill(isDbConnected ? Color.GREEN : Color.RED);

        // Focus on the home button
       homeButton.requestFocus();

   }

    /*
    JavaFX-EventHandler---------------------------------------------------------*/


    /**
     * Handles the Home button action. Switches the center view to the Home view.
     * @param event The action event.
     */
    @FXML
    void handleHomeButton(ActionEvent event) {
        AppState.getInstance().setActiveView("Home");
        switchCenterView("/org/mr/abschlussprojekt/bikeRental/user-home-view.fxml");
       updateButtonStyles("Home");
    }


    /**
     * Handles the Activity button action. Switches the center view to the Activity view.
     * @param event The action event.
     */
    @FXML
    void handleActivityAccount(ActionEvent event) {
        AppState.getInstance().setActiveView("Activity");
        try {
            switchCenterView("/org/mr/abschlussprojekt/bikeRental/user-activity-view.fxml");
            updateButtonStyles("Activity");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the Profile button action. Switches the center view to the Profile view.
     * @param event The action event.
     */
    @FXML
    void handleProfilButton(ActionEvent event) {
        AppState.getInstance().setActiveView("Profile");
        try {
            switchCenterView("/org/mr/abschlussprojekt/bikeRental/user-profile-view.fxml");
           updateButtonStyles("Profile");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    /**
     * Handles the Logout button action. Confirms logout intention and then logs out the user.
     * @param event The action event.
     */
    @FXML
    void handleLogOut(ActionEvent event) {
        AppState.getInstance().setActiveView("Logout");
        updateButtonStyles("Logout");
        var result = AlertManager.getInstance().showConfirmationAlert("Logout Confirmation",
                "Are you sure you want to log out?",
                "Click OK to confirm, or Cancel to remain logged in."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Der Benutzer hat OK geklickt, führen Sie die Abmeldeaktion aus
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.close();
            try {
                ChangeSceneManager.getInstance().switchToLoginForm(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*
    Help Methods---------------------------------------------------------*/
    /**
     * Switches the center content of the border pane to a new FXML view.
     * @param fxmlFileName The FXML file to load and display.
     */
    public void switchCenterView(String fxmlFileName) {
        System.out.println("Loading FXML: " + fxmlFileName);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node newCenter = loader.load();
            borderPane.setCenter(newCenter);
        } catch (IOException e) {
            System.out.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Updates the style of navigation buttons based on the currently active view.
     * @param activeView The active view name to match button styles accordingly.
     */
    private void updateButtonStyles(String activeView) {

        String defaultStyle = "";
        homeButton.setStyle(defaultStyle);
        activityButton.setStyle(defaultStyle);
        profileButton.setStyle(defaultStyle);
        logOutButton.setStyle(defaultStyle);

        String activeStyle = "-fx-background-color: #003366; -fx-text-fill: #ffffff;"; // Ihr Stil für den aktiven Button
        switch (activeView) {
            case "Home" -> homeButton.setStyle(activeStyle);
            case "Activity" -> activityButton.setStyle(activeStyle);
            case "Profile" -> profileButton.setStyle(activeStyle);
            case "Logout" -> logOutButton.setStyle(activeStyle);

        }
    }

}
