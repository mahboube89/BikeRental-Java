package org.mr.abschlussprojekt.bikeRental.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.Main;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.io.IOException;

/**
 * Singleton class for managing scene changes across the application.
 */
public class ChangeSceneManager {

    private static ChangeSceneManager instance; // Singleton instance

    /**
     * Private constructor to prevent external instantiation.
     */
    private ChangeSceneManager() {
    }

    /**
     * Provides access to the singleton instance of ChangeSceneManager, creating it if it does not yet exist.
     *
     * @return The singleton instance of ChangeSceneManager.
     */
    public static synchronized ChangeSceneManager getInstance() {
        if (instance == null) instance = new ChangeSceneManager();
        return instance;
    }


    /**
     * Switches the current stage to the first menu, typically the login form.
     *
     * @param stage The stage to update with the new scene.
     * @throws IOException If there is an error loading the FXML file.
     */
    public void switchToLoginForm(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppTexts.LOGIN_FORM_FXML_PATH));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the user home scene in a new stage.
     */
    public void switchToUserHome() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppTexts.USER_MENU_FXML_PATH));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    /**
     * Opens the admin dashboard scene in a new stage.
     */
    public void switchToAdminDashboard() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppTexts.ADMIN_DASHBOARD_FXML_PATH));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
