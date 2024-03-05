package org.mr.abschlussprojekt.bikeRental.gui;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.mr.abschlussprojekt.bikeRental.database.services.BikeService;
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.database.services.StationService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.model.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the Admin Bikes Tab. Manages the CRUD operations for bikes in the admin panel.
 */
public class AdminBikesTabController implements Initializable {

    /*
   Attributes---------------------------------------------------------------*/

    private BikeService bikeService; // A service layer handling business logic related to bikes.
    private StationService stationService; // A service layer for managing station-related operations.

    // List to store bikes fetched from the database.
    private ObservableList<Bike> listOfBikes;

    private Bike selectedBike; // Currently selected bike for edit
    private boolean isEditMode = false;


    /*
   FXML UI Elements---------------------------------------------------------*/
    @FXML
    private Circle dbStatusLight;

    @FXML
    private TableView<Bike> bikesTable;
    @FXML
    private TableColumn<Bike, Integer> bikeIdColumn;
    @FXML
    private TableColumn<Bike, String> bikeModelColumn;
    @FXML
    private TableColumn<Bike, Double> bikePriceColumn;
    @FXML
    private TableColumn<Bike, String> bikeStationColumn;
    @FXML
    private TableColumn<Bike, String> bikeStatusColumn;
    @FXML
    private TableColumn<Bike, String> bikeTypeColumn;
    @FXML
    private TableColumn<Bike, Void> actionColumn;
    @FXML
    private ComboBox<BikeType> bikeTypeComboBox;
    @FXML
    private ComboBox<Station> stationComboBox;
    @FXML
    private ComboBox<BikeStatus> bikeStatusComboBox;
    @FXML
    private TextField bikeModelField;
    @FXML
    private TextField bikePriceField;

    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button updateButton;

    @FXML
    private Label tipText;
    @FXML
    private Label errorLabel;


    /*
   FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller. This method is called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.bikeService = new BikeService();
        this.stationService = new StationService();

        // A service for checking the database connection status.
        DatabaseService databaseService = new DatabaseService();

        cancelButton.setDisable(true); // Initially disable the cancel button
        addButton.setDisable(true); // Initially disable the add button
        updateButton.setDisable(true); // Initially disable the update button
        loadStationsInComboBox(); // Load stations into the ComboBox

        bikeTypeComboBox.getItems().setAll(BikeType.values());  // Populate bike types
        bikeStatusComboBox.getItems().setAll(BikeStatus.values()); // Populate bike statuses

        // Check if database connection is established
        boolean isDbConnected = databaseService.isDatabaseConnected();
        if (isDbConnected) {
            dbStatusLight.setFill(Color.GREEN);
            setChangeListeners(); // Set listeners for form fields
            switchToAddMode(); // Prepare UI for adding new bikes
            showBikes(); // Display existing bikes
        } else {
            dbStatusLight.setFill(Color.RED);
        }
        handleActionColumn(); // Set up the action column in the TableView

    }


    /*
    JavaFX-EventHandler---------------------------------------------------------*/

    @FXML
    void addBikeButtonHandler(ActionEvent event) {
        createNewBike();
        showBikes(); // Refreshes the list of bikes
    }

    @FXML
    void cancelBtnHandler(ActionEvent event) {
        tipText.setText("");
        clearTxtFieldsAndDisableBtns(); // Clears all form fields and resets button states
        switchToAddMode(); // Resets the form to "add mode," preparing it for a new bike entry.
    }

    @FXML
    void loadBikesFromDbHandler(ActionEvent event) {
        showBikes(); // Refreshes the list of bikes from the database, displaying the latest information in the table.
    }

    @FXML
    void txtFieldsValidate(KeyEvent event) {
    }

    /**
     * Handles the event of clicking the 'Update' button in edit mode.
     * This method updates the information of the selected bike with the new values entered by the user.
     */
    @FXML
    void updateBikeHandler(ActionEvent event) {
        if (selectedBike != null) { // Check if a bike is selected for editing.
            try {
                // Extract new bike information from the form fields.
                String newModel = bikeModelField.getText();
                BikeType newType = bikeTypeComboBox.getValue();
                double newPrice = Double.parseDouble(bikePriceField.getText());
                BikeStatus newStatus = bikeStatusComboBox.getValue();

                // Call the service to update the bike information in the database.
                bikeService.updateBikeInfo(selectedBike.getBikeId(), newModel, newType, newPrice, newStatus, String.valueOf(stationComboBox.getSelectionModel().getSelectedItem()));

                // Show a success message to the user.
                AlertManager.getInstance().showInformationAlert("Update Successful", null, "Bike information has been successfully updated.");

                // Clear the form fields and reset the UI to add mode.
                clearTxtFieldsAndDisableBtns();
                isEditMode = false; // Reset edit mode flag.
                tipText.setText(""); // Clear any tips.
                showBikes(); // Refresh the list of bikes to show the updated information.

            } catch (NumberFormatException e) {
                // Handle the case where the price is not a valid double value.
                errorLabel.setText("Please enter a valid number for the price.");
            } catch (RuntimeException e) {
                AlertManager.getInstance().showErrorAlert("Update Failed", "There was a problem updating the bike information. Please try again.");
            }
        }
    }

    /*
    Help Methods----------------------------------------------------------------*/


    /**
     * Loads all stations into the station combo box.
     * This method fetches a list of all stations from the station service and populates the station combo box
     * with these stations, allowing the user to select a station when adding or editing a bike.
     */
    private void loadStationsInComboBox() {

        List<Station> stations = stationService.getAllStations(); // Fetch the list of stations.
        stationComboBox.getItems().setAll(stations); // Populate the combo box with the stations.
    }


    /**
     * Fetches and displays all bikes from the database into the table view.
     * This method queries the database for all bike entries, sets up the table columns,
     * and binds the fetched data to the table view for display.
     */
    private void showBikes() {

        try {
            // Fetch all bikes from the database
            listOfBikes = bikeService.loadAllBikes();

            // Setting up table columns to display bike properties using property value factories
            bikeIdColumn.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
            bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));
            bikeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bikeType"));
            bikePriceColumn.setCellValueFactory(new PropertyValueFactory<>("bikePrice"));
            bikeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("bikeStatus"));
            bikeStationColumn.setCellValueFactory(new PropertyValueFactory<>("bikeLocation"));

            // Set the fetched list of bikes to the table view to display
            bikesTable.setItems(listOfBikes);

        } catch (Exception e) {
            System.out.println("Error fetching bikes: " + e.getMessage());
            throw new RuntimeException(e);
        }
        // disable action buttons until a specific user interaction
        cancelButton.setDisable(true);
        addButton.setDisable(true);
    }


    /**
     * Creates a button with an icon.
     *
     * @param iconPath The path to the icon image.
     * @return A button with the specified icon.
     */
    private Button createIconButton(String iconPath) {

        URL url = getClass().getResource(iconPath);
        if (url == null) {
            throw new IllegalArgumentException("Icon nicht gefunden: " + iconPath);
        }

        Image icon = new Image(url.toString());
        ImageView iconView = new ImageView(icon);

        iconView.setFitHeight(20);
        iconView.setFitWidth(20);
        Button button = new Button();
        button.setGraphic(iconView);

        return button;
    }


    /**
     * Configures the action column in the bikes table.
     * Sets up custom cell factory to include delete and edit buttons for each row.
     */
    private void handleActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createIconButton("/org/mr/abschlussprojekt/bikeRental/styles/delete-icon.png");
            private final Button editButton = createIconButton("/org/mr/abschlussprojekt/bikeRental/styles/edit-icon.png");

            private final HBox container = new HBox(5, deleteButton, editButton);

            {
                container.setAlignment(Pos.CENTER);
                // Set up delete action
                deleteButton.setOnAction(event -> {
                    Bike bike = getTableView().getItems().get(getIndex());
                    System.out.println("bike in delete handler: " + bike);
                    deleteBike(bike);
                });
                // Set up edit action
                editButton.setOnAction(event -> {
                    Bike bike = getTableView().getItems().get(getIndex());
                    switchToEditMode(bike);
                });
            }

            @Override
            protected void updateItem(Void item, boolean b) {
                super.updateItem(item, b);
                if (b) {
                    setGraphic(null);
                } else {
                    Bike bike = getTableView().getItems().get(getIndex());
                    // Disable buttons based on the bike's status
                    boolean isRented = bike.getBikeStatus().equals(BikeStatus.RENTED);
                    deleteButton.setDisable(isRented);
                    editButton.setDisable(isRented);
                    setGraphic(container);
                }
            }
        });
    }


    /**
     * Switches the UI to edit mode for a selected bike.
     * It sets the form to reflect the information of the bike to be edited,
     * populates the fields with the bike's current data, and updates button states accordingly.
     * @param bike The bike object to be edited.
     */
    private void switchToEditMode(Bike bike) {
        isEditMode = true; // Indicate that the form is now in edit mode.
        selectedBike = bike; // Store the selected bike for editing.

        populateFieldsForEditing(bike); // Populate form fields with the bike's current data.
        updateButtonStates(); // Update the states of the buttons based on the current form mode.
    }

    /**
     * Switches the UI to add mode, preparing it for entering a new bike.
     * It clears the form and resets the state, disabling the update button and enabling the add button.
     */
    private void switchToAddMode() {
        isEditMode = false; // Indicate that the form is now in add mode, not edit mode.
        selectedBike = null; // Clear the selected bike as we're not editing an existing one.
        updateButtonStates(); // Update the states of the buttons based on the current form mode.
    }


    /**
     * Updates the states of the form buttons (Add, Update, Cancel) based on the current mode (add or edit)
     * and the validity of the form fields or changes made to the editable fields.
     */
    private void updateButtonStates() {
        if (!isEditMode) {
            boolean formValid = validateNewBikeFields(); // Check if the form fields are valid for a new bike.
            addButton.setDisable(!formValid); // Enable the Add button only if the form is valid.
            updateButton.setDisable(true); // The Update button is always disabled in add mode.
        } else {
            boolean changesMade = checkForChanges(); // Check if changes have been made to the bike's information.
            updateButton.setDisable(!changesMade); // Enable the Update button only if changes have been made.
            addButton.setDisable(true); // The Add button is always disabled in edit mode.
        }
        cancelButton.setDisable(false); // The Cancel button is always enabled.
    }


    /**
     * Populates the form fields with the selected bike's information for editing.
     * Sets the editing mode to true and updates the UI components with the bike's current details.
     *
     * @param bike The bike selected for editing.
     */
    private void populateFieldsForEditing(Bike bike) {

        isEditMode = true;
        selectedBike = bike;

        // Set the form fields with the bike's current information
        bikeModelField.setText(bike.getBikeModel());
        bikeTypeComboBox.setValue(bike.getBikeType());
        bikeStatusComboBox.setValue(bike.getBikeStatus());
        bikePriceField.setText(String.format("%.2f", bike.getBikePrice()));

        // Find and select the corresponding station in the combo box
        for (Station station : stationComboBox.getItems()) {
            if (station.getStationAddress().equals(bike.getBikeLocation())) {
                stationComboBox.setValue(station);
                break;
            }
        }

        // Provide a tip for the user on how to proceed with editing
        tipText.setText("Tip: Change the data and confirm by clicking 'Update'.");
        tipText.setTextFill(Color.GREEN);
        tipText.setWrapText(true);
        tipText.setMaxWidth(200);

        // Listen for changes in the form to enable the update button
        setChangeListeners();

    }


    /**
     * Checks for any changes made to the bike's information in the form compared to its original data.
     * This helps to determine whether the "Update" button should be enabled.
     *
     * @return true if any changes were made to the bike's information, false otherwise.
     */
    private boolean checkForChanges() {
        if (selectedBike == null) {
            return false; // No bike is selected for editing
        }

        // Retrieve current values from the form
        String currentModel = bikeModelField.getText();
        BikeType currentType = bikeTypeComboBox.getValue();
        BikeStatus currentStatus = bikeStatusComboBox.getValue();
        Station currentStation = stationComboBox.getValue();
        double currentPrice;
        try {
            currentPrice = Double.parseDouble(bikePriceField.getText());
        } catch (NumberFormatException e) {
            return true;
        }

        // Check if any of the current form values differ from the selected bike's original data
        boolean modelChanged = !currentModel.equals(selectedBike.getBikeModel());
        boolean typeChanged = currentType != null && !currentType.equals(selectedBike.getBikeType());
        boolean statusChanged = currentStatus != null && !currentStatus.equals(selectedBike.getBikeStatus());
        boolean stationChanged = currentStation != null && !currentStation.getStationAddress().equals(selectedBike.getBikeLocation());
        boolean priceChanged = currentPrice != selectedBike.getBikePrice();

        return modelChanged || typeChanged || statusChanged || stationChanged || priceChanged;
    }


    /**
     * Creates a new bike entry in the database with the information provided in the form fields.
     */
    private void createNewBike() {

        switchToAddMode(); // Ensures the UI is in the correct state for adding a new bike

        // Retrieves the values entered by the user in the form fields
        String bikeModelInserted = bikeModelField.getText();
        BikeType selectedType = bikeTypeComboBox.getSelectionModel().getSelectedItem();
        BikeStatus statusInserted = bikeStatusComboBox.getSelectionModel().getSelectedItem();
        Station selectedStation = stationComboBox.getSelectionModel().getSelectedItem();

        // Checks if a station has been selected, as it is mandatory for adding a bike
        if (selectedStation == null) {
            errorLabel.setText("Please select a station.");
            return;
        }
        String selectedStationAddress = selectedStation.toString(); // Gets the selected station's address for the new bike's location

        // Fetches the station ID based on the selected station's address
        int stationId = stationService.getStationIdByLocation(selectedStationAddress);

        try {

            double bikePriceInserted = Double.parseDouble(bikePriceField.getText());

            // add the new bike to the database
            bikeService.addNewBike(bikeModelInserted, selectedType, bikePriceInserted, stationId, statusInserted);

            clearTxtFieldsAndDisableBtns(); // Resets the form fields

        } catch (NumberFormatException e) {
            // Displays an error if the price is not a valid number
            errorLabel.setText("Please enter a valid number for the price.");
        } catch (Exception e) {
            System.out.println("Error while adding new bike: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets up change listeners for the bike form fields. These listeners update the state of the
     * form's buttons based on the current input to ensure that the user can only submit valid data.
     */
    private void setChangeListeners() {

        // Listener for changes in text fields
        ChangeListener<String> fieldChangeListener = (observable, oldValue, newValue) -> updateButtonStates();
        // Listener for changes in combo boxes
        ChangeListener<Object> comboBoxChangeListener = (observable, oldValue, newValue) -> updateButtonStates();

        // Adds listeners to each form field to trigger button state updates
        bikeModelField.textProperty().addListener(fieldChangeListener);
        bikePriceField.textProperty().addListener(fieldChangeListener);
        bikeTypeComboBox.valueProperty().addListener(comboBoxChangeListener);
        bikeStatusComboBox.valueProperty().addListener(comboBoxChangeListener);
        stationComboBox.valueProperty().addListener(comboBoxChangeListener);
    }


    /**
     * Validates the fields for creating or updating a bike. Checks if the model text is not empty,
     * a bike type is selected, a status is selected, a station is selected, and if the price is a valid
     * numerical value.
     *
     * @return true if all validations pass, false otherwise.
     */
    private boolean validateNewBikeFields() {
        // Retrieve the current values from the form fields
        String bikeModelText = bikeModelField.getText();
        BikeType selectedBikeType = bikeTypeComboBox.getValue();
        BikeStatus selectedBikeStatus = bikeStatusComboBox.getValue();
        Station selectedStation = stationComboBox.getValue();
        String bikePriceText = bikePriceField.getText();

        boolean isModelValid = !bikeModelText.trim().isEmpty();
        boolean isTypeSelected = selectedBikeType != null;
        boolean isStatusSelected = selectedBikeStatus != null;
        boolean isStationSelected = selectedStation != null;
        boolean isPriceValid = bikePriceText.matches("\\d+(\\.\\d+)?"); // Validate the bike price as a numerical value

        // Return true if all validations pass
        return isModelValid && isTypeSelected && isStatusSelected && isStationSelected && isPriceValid;
    }


    /**
     * Clears all input fields and resets the form to its initial state.
     * This includes clearing the bike model, price fields, and resetting all combo boxes.
     * disables the "Cancel", "Add", and "Update" buttons to prevent actions without valid input.
     */
    private void clearTxtFieldsAndDisableBtns() {
        // Clear the input fields for bike model and price
        bikeModelField.clear();
        bikePriceField.clear();

        // Clear the selections in all combo boxes
        bikeTypeComboBox.getSelectionModel().clearSelection();
        bikeStatusComboBox.getSelectionModel().clearSelection();
        stationComboBox.getSelectionModel().clearSelection();

        // Disable the "Cancel", "Add", and "Update" buttons as no action can be performed without valid input
        cancelButton.setDisable(true);
        addButton.setDisable(true);
        updateButton.setDisable(true);
    }


    /**
     * Deletes a bike from the database and updates the UI accordingly.
     *
     * @param bike The bike object to be deleted.
     */
    private void deleteBike(Bike bike) {

        // Confirm with the user before proceeding with deletion
        Optional<ButtonType> result = AlertManager.getInstance().showConfirmationAlert("Delete Bike", null, "Are you sure you want to delete this Bike?");

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {

                // Attempt to delete the bike from the database
                bikeService.deleteBikeFromDB(bike.getBikeId());

                listOfBikes.remove(bike); // Remove the bike from the displayed list
                addButton.requestFocus(); // Refocus on the add button

                // Inform the user of successful deletion
                AlertManager.getInstance().showInformationAlert("Deletion Successful", null, "Bike has been successfully deleted.");

            } catch (RuntimeException e) {
                // Inform the user of a failure to delete the bike
                AlertManager.getInstance().showErrorAlert("Deletion Failed", "There was a problem deleting the bike. Please try again.");
            }
        }
        addButton.requestFocus();
    }


}
