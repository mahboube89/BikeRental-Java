package org.mr.abschlussprojekt.bikeRental.gui;


import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mr.abschlussprojekt.bikeRental.database.services.RentService;
import org.mr.abschlussprojekt.bikeRental.database.services.StationService;
import org.mr.abschlussprojekt.bikeRental.database.services.StatusService;
import org.mr.abschlussprojekt.bikeRental.database.services.UserService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.CurrentUser;
import org.mr.abschlussprojekt.bikeRental.model.Bike;
import org.mr.abschlussprojekt.bikeRental.model.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * Controller class for managing bike rentals in the application.
 */
public class RentBikeController implements Initializable {

    /*
    Attributes---------------------------------------------------------------*/


    private RentService rentService;
    private StationService stationService;
    private StatusService statusService;
    private UserService userService;

    // Start and end date for the rental
    LocalDate startDate;
    LocalDate endDate;

    // The current bike selected for renting
    private Bike currentBike;

    // The current user's ID
    private int userId;

    /*
    FXML UI Elements---------------------------------------------------------*/
    @FXML
    private TextField bikeModelField;
    @FXML
    private TextField bikeTypeField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField locationField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label rentErrorLabel;
    @FXML
    private Button confirmRentalButton;

    /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the UI elements and loads user data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.rentService = new RentService();
        this.stationService = new StationService();
        this.statusService = new StatusService();
        this.userService = new UserService();

        //ObservableList<Station> stationList = StationDao.getAllStations(connection);
        loadUserData();
        confirmRentalButton.setDisable(true);

        // Configure date pickers for selecting rental dates
        configureDatePicker(startDatePicker);
        configureDatePicker(endDatePicker);

        // Listeners for date pickers to enable dynamic calculation of the rental price
        startDatePicker.valueProperty().addListener(((observableValue, oldValue, newValue) -> calculatePrice()));
        endDatePicker.valueProperty().addListener(((observableValue, oldValue, newValue) -> calculatePrice()));
    }

    /**
     * Configures a DatePicker to don't allow selection of dates before the current date.
     */
    public void configureDatePicker(DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    /**
     * Sets the user ID for the current session and loads user data based on it.
     *
     * @param userId The user ID of the currently logged-in user.
     */
    void setUserId(int userId) {
        this.userId = userId;
        loadUserData();
    }

    /*
    JavaFX-EventHandler---------------------------------------------------------*/

    /**
     * Handles the action of clicking the cancel button. Closes the current stage (window).
     */
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    /**
     * Validates the selected dates, calculates the total price,
     * and updates the database with the rental information if confirmed by the user.
     */
    @FXML
    void handleRenting(ActionEvent event) {

        int userId = CurrentUser.getInstance().getUserId();

        User user = userService.getUserById(userId);

        // Retrieve and validate selected dates
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();

        // Calculate the total price based on the selected dates and bike's daily rate.
        double totalPrice = calculatePrice();

        // Confirmation message
        String message = String.format("Confirm rental from %s to %s for a total of %.2f $ ?",
                startDate.toString(),
                endDate.toString(),
                totalPrice);

        // Show confirmation popup
        Stage ownerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        PopUpWindowConfirm.displayConfirmation("Confirm Rental", message, "Confirm", ownerStage, confirmed -> {
            if (confirmed) { // If the user confirms the rental

                // Retrieve the station ID from the bike's current location for the rental record.
                //int startStationId = StationDao.getStationIdByLocation(currentBike.getBikeLocation(), connection);
                int startStationId = stationService.getStationIdByLocation(currentBike.getBikeLocation());

                String totalPriceText = totalPriceLabel.getText().replace("$", "").trim();

                // Attempt to insert the rental record into the database.
                boolean isRentBikeInDB = rentService.insertBikeRental(
                //boolean isRentBikeInDB = RentDao.insertBikeRental(
                        currentBike.getBikeId(),
                        CurrentUser.getInstance().getUserId(),
                        startStationId,
                        startDatePicker.getValue(),
                        endDatePicker.getValue(),
                        Double.parseDouble(totalPriceText),
                        true
                );

                if (isRentBikeInDB) {
                    // Update bike status to 'RENTED'
                    boolean statusUpdated = statusService.updateBikeStatus(currentBike.getBikeId(), "RENTED");

                    if (statusUpdated) {
                        // Inform the user that the rental is confirmed and close the rental window.
                        AlertManager.getInstance().showInformationAlert("All set!", "Your rental is confirmed.", "Happy cycling!");

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();

                    } else {
                        // If updating the bike's status fails, inform the user.
                        AlertManager.getInstance().showErrorAlert("Update Error", "Could not update bike status. ");
                    }

                } else {
                    // If inserting the rental record fails, inform the user
                    AlertManager.getInstance().showErrorAlert("Rental Error", "Could not confirm rental, Please try again.");
                }
            } else {
                // If the user cancels the rental confirmation, inform them.
                AlertManager.getInstance().showErrorAlert("Rental not confirmed", "Please make a selection.");
            }

        });

    }



    /*
    Help Methods---------------------------------------------------------*/

    /**
     * Sets the bike data in the rental form based on the selected bike.
     *
     * @param bike The bike selected for rental.
     */
    public void setBikeData(Bike bike) {

        this.currentBike = bike;

        bikeModelField.setText((bike.getBikeModel()));
        bikeTypeField.setText(bike.getBikeType().toString());
        priceField.setText(String.valueOf(bike.getBikePrice()));
        locationField.setText(bike.getBikeLocation());
    }



    /**
     * Loads the current user's data into the form fields.
     */
    public void loadUserData() {

        try {
            User user = userService.getUserById(userId);

            if (user != null) {
                userNameField.setText(user.getUserName());
                phoneField.setText(user.getUserPhone());
                emailField.setText(user.getUserEmail());
            }
        } catch (Exception e) {
            System.out.println("Error by load user data: " + e.getMessage());
            AlertManager.getInstance().showErrorAlert("Data Loading Error", "Error loading user data. Please try again later.");

            throw new RuntimeException(e);
        }
    }



    /**
     * Calculates the total price of the rental based on the selected dates and bike's daily price.
     *
     * @return The total price of the rental.
     */
    private double calculatePrice() {

        // Retrieve the start and end dates selected by the user.
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();

        // Disable the confirm rental button until valid dates are selected.
        confirmRentalButton.setDisable(true);

        rentErrorLabel.setText(""); // Clear any previous error messages.

        double totalPrice = 0;

        // Check if both start and end dates have been selected.
        if (startDate != null && endDate != null) {
            if (!endDate.isBefore(startDate)) { // Ensure the end date is not before the start date

                confirmRentalButton.setDisable(false); // Enable the confirm rental button as the date selection is valid.

                // Calculate the number of days between the start and end dates, inclusive.
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

                try {
                    double dailyPrice = Double.parseDouble(priceField.getText());

                    // Calculate the total price for the rental period.
                    totalPrice = dailyPrice * daysBetween;

                    totalPriceLabel.setText(String.format(" %.2f $", totalPrice));
                    return totalPrice;

                } catch (NumberFormatException e) {
                    System.out.println("Error in parsing the daily price" + e.getMessage());
                    totalPriceLabel.setText("");
                }
            } else {
                // Display an error if the end date is before the start date.
                rentErrorLabel.setText("Please ensure that the end date is not before the start date.");
            }
        } else {
            // Display an error if either start or end date has not been selected.
            rentErrorLabel.setText("Please select both start and end dates.");
        }
        return totalPrice;
    }

}
