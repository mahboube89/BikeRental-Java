package org.mr.abschlussprojekt.bikeRental.gui;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.RentDao;
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.database.services.RentService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.CurrentUser;
import org.mr.abschlussprojekt.bikeRental.model.BikeRental;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;


import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller class for the User Activity UI. This class manages the user's rental history,
 * displaying a list of rented bikes and allowing users to return bikes.
 */
public class UserActivityController implements Initializable {

    /*
   Attributes---------------------------------------------------------------*/

    private RentService rentService;

    // Current user's ID and username for personalized actions
    int currentUserId = CurrentUser.getInstance().getUserId();


    /*
    FXML UI Elements---------------------------------------------------------*/

    @FXML
    private TableView<BikeRental> rentBikesTable;
    @FXML
    private TableColumn<BikeRental, Integer> rentIdColumn;
    @FXML
    private TableColumn<BikeRental, String> bikeModelColumn;
    @FXML
    private TableColumn<BikeRental, String> pickupLocationColumn;
    @FXML
    private TableColumn<BikeRental, LocalDate> pickupDateColumn;
    @FXML
    private TableColumn<BikeRental, LocalDate> returnDateColumn;
    @FXML
    private TableColumn<BikeRental, Double> totalPriceColumn;
    @FXML
    private TableColumn<BikeRental, Void> actionColumn;

    /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It attempts to connect to the database and load the user's rental history.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.rentService = new RentService();
        DatabaseService databaseService = new DatabaseService();

        try {
            // Check if the application is successfully connected to the database.
            boolean isDbConnected = databaseService.isDatabaseConnected();
            if (isDbConnected) {

                showRentHistory(); // If connected, fetch and display the current user's rental history.
                handleActionColumn(); // Set up the action column in the table, which allows to return bikes.

            } else {
                System.out.println("Connection failed in activity page");
            }
        } catch (Exception e) {
            System.out.println("Error during initialization: " + e.getMessage());
        }

    }

    /*
    JavaFX-EventHandler---------------------------------------------------------*/

    /*
    Help Methods----------------------------------------------------------------*/

    /**
     * Configures the action column in the TableView to include a button for each bike rental entry.
     * The button's behavior changes based on the rental's return status.
     */
    private void handleActionColumn() {
        // Set a custom cell factory for the action column.
        actionColumn.setCellFactory(column -> new TableCell<>() {
            final Button actionButton = new Button(AppTexts.RETURN_BUTTON_TXT); // Initialize the action button with default text.

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null); // If the cell is empty, don't display anything.
                } else {
                    // Retrieve the BikeRental object corresponding to this row.
                    BikeRental bikeRental = getTableView().getItems().get(getIndex());

                    if (bikeRental.isReturned()) {
                        // If the bike has already been returned, indicate completion and disable the button.
                        actionButton.setText(AppTexts.COMPLETED_BUTTON_TXT);
                        actionButton.setDisable(true);

                    } else {
                        // If the bike has not been returned, set the button to allow returning the bike.
                        actionButton.setText(AppTexts.RETURN_BUTTON_TXT);
                        actionButton.setDisable(false);

                        // Set the button's action. When clicked, it will prompt the user for confirmation to return the bike.
                        actionButton.setOnAction(event -> {

                            // Show a confirmation dialog to ensure the user wants to proceed with returning the bike.
                            Optional<ButtonType> result = AlertManager.getInstance().showConfirmationAlert(AppTexts.RETURN_BIKE, null, AppTexts.SURE_WANT_RETURN_BIKE);

                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                // If the user confirms, attempt to update the bike's status and the rental's return status.
                                boolean isBikeStausUpdate = rentService.updateBikeStatusToAvailable(bikeRental.getBikeId());
                                boolean isReturnStatusUpdate = rentService.updateRentalReturnStatus(bikeRental.getRentalId());

                                if (isReturnStatusUpdate && isBikeStausUpdate) {
                                    // If updates are successful, show a success message and refresh the rental history.
                                    rentService.updateRentalReturnStatus(bikeRental.getRentalId());
                                    AlertManager.getInstance().showInformationAlert(AppTexts.RETURN_SUCCESSFUL, null, AppTexts.BIKE_ISRETURNED_AND_IS_NOW_AVAILABLE);

                                    showRentHistory();
                                    bikeRental.setReturned(true);
                                }

                            } else {
                                // If the user cancels or the update fails, show an error message.
                                AlertManager.getInstance().showErrorAlert(AppTexts.RETURN_FAILED, AppTexts.FAILED_TO_RETURN_BIKE);
                            }

                        });
                    }
                    setGraphic(actionButton); // Set the action button as the graphic for this cell.
                }
            }
        });
    }


    /**
     * Retrieves and displays the rental history of the current user.
     * This method fetches the list of all bikes rented by the current user from the database,
     * then sets up the table view to display these rentals. Each rental's details, such as rental ID,
     * bike model, pickup location, dates, and total price, are shown in their respective columns.
     */
    public void showRentHistory() {
        // List to hold the rental history
        ObservableList<BikeRental> listOfRentedBike;
        try {

            // Fetch the list of rented bikes for the current user using the RentDao class.
            // This includes all relevant rental information to be displayed.
            //listOfRentedBike = RentDao.getRentalsForUser(currentUserId, connection);
            listOfRentedBike = rentService.getRentalsForUser(currentUserId);

            // Set up the columns in the table to display the fetched rental data.
            rentIdColumn.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
            bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));
            pickupLocationColumn.setCellValueFactory(new PropertyValueFactory<>("startStation"));
            pickupDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));

            // Set the ObservableList of rented bikes as the items of the TableView.
            rentBikesTable.setItems(listOfRentedBike);

        } catch (Exception e) {
            System.out.println("Error in showRentHistory: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }


}
