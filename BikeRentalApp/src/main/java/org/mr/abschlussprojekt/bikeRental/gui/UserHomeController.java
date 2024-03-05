package org.mr.abschlussprojekt.bikeRental.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.Main;
import org.mr.abschlussprojekt.bikeRental.database.services.BikeService;
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.CurrentUser;
import org.mr.abschlussprojekt.bikeRental.model.Bike;
import org.mr.abschlussprojekt.bikeRental.model.BikeStatus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for the User Home screen. Manages the display and interaction with the list of bikes,
 * including searching, displaying bike details
 */
public class UserHomeController implements Initializable {

    /*
   Attributes---------------------------------------------------------------*/

    private BikeService bikeService;

    // List of bikes to display in the table
    private ObservableList<Bike> listOfBikes;

    /*
    FXML UI Elements---------------------------------------------------------*/
    @FXML
    private Label userNameLabel;
    @FXML
    private Label searchLabelMsg;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Bike> bikesTable;
    @FXML
    private TableColumn<Bike, Integer> bikeIdColumn;
    @FXML
    private TableColumn<Bike, String> bikeModelColumn;
    @FXML
    private TableColumn<Bike, String> bikeTypeColumn;
    @FXML
    private TableColumn<Bike, Double> bikePriceColumn;
    @FXML
    private TableColumn<Bike, String> bikeStatusColumn;
    @FXML
    private TableColumn<Bike, String> bikeLocationColumn;
    @FXML
    private TableColumn<Bike, Void> actionColumn;




    /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller class. This method is called after the FXML file has been loaded.
     * It sets up the user's name label, checks database connection, and prepares the bike list and action columns.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.bikeService = new BikeService();
        DatabaseService databaseService = new DatabaseService();

        // Set the user's name in the UI
        String currentUser = CurrentUser.getInstance().getUserName();
        userNameLabel.setText(currentUser);

        // Check database connection and display bikes
        try {
            boolean isDbConnected = databaseService.isDatabaseConnected();
            if (isDbConnected) {

                showAllBikes();
                handleActionColumn();
                bikesTable.requestFocus();

            }else {
                System.out.println("Connection failed");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /*
    JavaFX-EventHandler---------------------------------------------------------*/

    /**
     * Filters the list of bikes based on the user's search input and updates the TableView.
     * If a bike's model, type, status, or location matches the search text, it's included in the filtered list.
     * Displays a message to the user if no matches are found or if an error occurs.
     */
    @FXML
    void handleSearch() {

        // Check if the list of bikes is initialized to avoid NullPointerException.
        if (listOfBikes == null) {
            System.err.println("Bike list is not initialized.");
            searchLabelMsg.setText("Unable to perform search."); // Inform the user.
            return; // Exit the method to prevent further execution.
        }

        // Get the search text from the input field and convert it to lowercase for case-insensitive matching.
        String searchText = searchField.getText().toLowerCase();

        // Create an observable list to hold the filtered bikes.
        ObservableList<Bike> filteredList = FXCollections.observableArrayList();

        boolean found = false; // A flag to track if any matches are found.

        // Iterate over the list of bikes to find matches.
        for (Bike bike : listOfBikes) {
            // Check if the bike's model, type, status, or location contains the search text.
            if (bike.getBikeModel().toLowerCase().contains(searchText) ||
                bike.getBikeType().toString().toLowerCase().contains(searchText) ||
                bike.getBikeStatus().toString().toLowerCase().contains(searchText) ||
                bike.getBikeLocation().toLowerCase().contains(searchText)) {
                searchLabelMsg.setText(""); // Clear any previous message.
                filteredList.add(bike); // Add the matching bike to the filtered list.
                found = true; // Indicate that a match has been found.

            }
        }
        // Update the UI based on whether matches were found.
        if (!found) {
            searchLabelMsg.setText("No matches found!"); // Inform the user that no matches were found.
        } else {
            searchLabelMsg.setText(""); // Clear the message if matches are found
        }

        bikesTable.setItems(filteredList); // Update the TableView to display the filtered list of bikes.
    }


    /*
    Help Methods---------------------------------------------------------*/

    /**
     * Displays all available bikes in the table view. Sets up the columns and their cell value factories.
     */
    public void showAllBikes() {

        try {
            // Attempt to load all bikes from the database
            listOfBikes = bikeService.loadAllBikes() ;

            // Configure table columns to display bike information
            bikeIdColumn.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
            bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));
            bikeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bikeType"));
            bikePriceColumn.setCellValueFactory(new PropertyValueFactory<>("bikePrice"));
            bikeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("bikeStatus"));
            bikeLocationColumn.setCellValueFactory(new PropertyValueFactory<>("bikeLocation"));

            // Set the items of the table to the list of bikes loaded from the database
            bikesTable.setItems(listOfBikes);

        } catch (Exception e) {
            System.err.println("Error loading bikes: " + e.getMessage());
            AlertManager.getInstance().showErrorAlert("Loading Error", "Failed to load bikes. Please try again later.");
        }

    }



    /**
     * Configures the action column in the bike table. This method dynamically adds a "Rent" button
     * to each row in the table if the bike is available for rent. It also handles the button's action
     * to initiate the renting process for the selected bike.
     */
    private void handleActionColumn() {

        // Set a custom cell factory for the action column
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button rentButton = new Button("Rent");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                // Check if the current row is empty
                if (empty) {
                    setGraphic(null); // If empty, do not display anything
                } else {

                    // If not empty, retrieve the bike associated with the current row
                    Bike bike = getTableView().getItems().get(getIndex());

                    // Check if the bike is available for renting
                    if (BikeStatus.AVAILABLE.equals(bike.getBikeStatus())) {
                        rentButton.setDisable(false); // If available, enable the rent button

                        // Set the action when the rent button is clicked
                        rentButton.setOnAction(event -> {
                            System.out.println("Renting bike ID: " + bike.getBikeId());
                            openRentWindow(bike); // Open the rent window for the selected bike
                        });
                    } else {
                        // If the bike is not available, disable the rent button
                        rentButton.setDisable(true);
                    }
                    // Display the button in the current row of the action column
                    setGraphic(rentButton);
                    rentButton.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    /**
     * Refreshes the list of bikes displayed in the table. This is called after a bike is rented to update the list.
     */
    private void refreshBikeList() {
        showAllBikes();
        handleActionColumn();
    }


    /**
     * Opens a new window to handle the bike rental process for a selected bike.
     * This method loads the rent-bike-view.fxml file, sets up the controller with the current bike and user information,
     * and displays the window. It also ensures the bike list is refreshed when the window is closed.
     *
     * @param bike The bike that the user wants to rent.
     */
    private void openRentWindow(Bike bike) {
        try {

            // Create a new stage for the rental window
            Stage rentStage = new Stage();

            // Load the FXML file for the rent bike view
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("rent-bike-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Get the controller for the rent bike view and set the necessary information
            RentBikeController controller = fxmlLoader.getController();
            int currentUserId = CurrentUser.getInstance().getUserId(); // Retrieve the current user's ID

            controller.setUserId(currentUserId);// Pass the user ID to the controller
            controller.setBikeData(bike); // Pass the bike data to the controller

            rentStage.setScene(scene);
            rentStage.setTitle("Rent");
            rentStage.setResizable(false);

            // Set the window modality to block input to other windows until this one is closed
            rentStage.initModality(Modality.APPLICATION_MODAL);

            // Add a listener to refresh the bike list when the rental window is closed
            rentStage.setOnHidden(event -> this.refreshBikeList());
            rentStage.showAndWait();

        } catch (IOException e) {
            System.out.println("Problem in switching to Rent Window" + e.getMessage());
            e.printStackTrace();
        }

    }


}
