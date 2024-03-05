package org.mr.abschlussprojekt.bikeRental.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Singleton class to manage alert dialogs in the application.
 * Provides methods to display error, confirmation, and information alerts.
 */
public class AlertManager {

    private static AlertManager instance; // Singleton instance

    // Private constructor to prevent instantiation.
    private AlertManager() {
    }


    /**
     * Returns the single instance of this class, creating it if it does not yet exist.
     *
     * @return The single instance of AlertManager.
     */
    public static synchronized AlertManager getInstance() {
        if (instance == null) instance = new AlertManager();
        return instance;
    }

    /**
     * Displays an error alert dialog with a specified title and error message.
     *
     * @param title        The title of the alert dialog.
     * @param errorMessage The error message to be displayed in the dialog.
     */
    public void showErrorAlert(String title, String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(errorMessage);
        errorAlert.show();
    }

    /**
     * Displays a confirmation alert dialog with a specified title, header, and content message.
     * Waits for the user to either confirm or cancel the action.
     *
     * @param title      The title of the alert dialog.
     * @param headerTxt  The header text of the dialog.
     * @param contentTxt The content message of the dialog.
     * @return An Optional containing the user's response (ButtonType).
     */
    public Optional<ButtonType> showConfirmationAlert(String title, String headerTxt, String contentTxt) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(title);
        confirmAlert.setHeaderText(headerTxt);
        confirmAlert.setContentText(contentTxt);
        return confirmAlert.showAndWait();
    }

    /**
     * Displays an information alert dialog with a specified title, header, and content message.
     * Waits for the user to acknowledge the message before returning.
     *
     * @param title      The title of the alert dialog.
     * @param headerTxt  The header text of the dialog.
     * @param contentTxt The content message of the dialog.
     */
    public void showInformationAlert(String title, String headerTxt, String contentTxt) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle(title);
        infoAlert.setHeaderText(headerTxt);
        infoAlert.setContentText(contentTxt);
        infoAlert.showAndWait();
    }
}
