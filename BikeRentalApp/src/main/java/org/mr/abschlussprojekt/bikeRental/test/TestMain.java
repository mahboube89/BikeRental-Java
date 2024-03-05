package org.mr.abschlussprojekt.bikeRental.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.Main;
import org.mr.abschlussprojekt.bikeRental.logic.ChangeSceneManager;

import java.io.IOException;

public class TestMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/

//        ChangeSceneManager.getInstance().switchToLoginForm(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}
