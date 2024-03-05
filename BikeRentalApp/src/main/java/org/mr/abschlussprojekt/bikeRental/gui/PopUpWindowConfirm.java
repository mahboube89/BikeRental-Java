package org.mr.abschlussprojekt.bikeRental.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class PopUpWindowConfirm {


    /**
     * Displays a modal confirmation window with a custom message and a button to confirm the action.
     *
     * @param title       The title of the confirmation window.
     * @param message     The message to be displayed to the user.
     * @param buttonText  The text to be displayed on the confirmation button.
     * @param ownerStage  The owner stage of this pop-up, used to modal block the user input on other windows.
     * @param onConfirm   A consumer to handle the action on confirmation (true) or cancellation (false).
     */
    public static void displayConfirmation(String title , String message, String buttonText ,Stage ownerStage, Consumer<Boolean> onConfirm){

        Stage popupStage = new Stage();

        Label label = new Label(message);

        Button infoButton = new Button(buttonText);
        Button cancelButton = new Button("Cancel");

        infoButton.setOnAction( event -> {
            onConfirm.accept(true);
            popupStage.close();
        });

        cancelButton.setOnAction( event ->{
            onConfirm.accept(false);
            popupStage.close();
        } );

        VBox vbox = new VBox(10, label, infoButton, cancelButton);
        vbox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vbox ,500 , 150 );
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(ownerStage);
        popupStage.setTitle(title);
        popupStage.showAndWait();

    }


    public static void displayPopup(String message){

        Stage stage = new Stage();
        Label label = new Label(message);

        stage.setTitle("Info");

        Button button = new Button("Ok");

        button.setOnAction(event -> stage.close());
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, button);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox ,300 , 150 );

        stage.setScene(scene);
        stage.showAndWait();

    }
}
