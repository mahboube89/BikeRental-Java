package org.mr.abschlussprojekt.bikeRental.gui;

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
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.database.services.UserService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.CurrentUser;
import org.mr.abschlussprojekt.bikeRental.logic.Validator;
import org.mr.abschlussprojekt.bikeRental.model.User;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUsersTabController implements Initializable {

    /*
   Attributes---------------------------------------------------------------*/

    private UserService userService;

    // List to store users fetched from the database.
    private ObservableList<User> listOfUsers;

    private boolean isEditMode = false;
    private User selectedUser;

    /*
   FXML UI Elements---------------------------------------------------------*/

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label tipText;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField userPhoneField;
    @FXML
    private TextField userEmailField;
    @FXML
    private TextField userPasswordField;
    @FXML
    private Label errorValidationLabel;

    @FXML
    private Circle dbStatusLight;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userEmailColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, Void> actionColumn;
    @FXML
    private TableColumn<User, String> userPasswordColumn;
    @FXML
    private TableColumn<User, String> userPhoneColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;

    /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It attempts to connect to the database and load the user's information.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userService = new UserService();
        DatabaseService databaseService = new DatabaseService();

        cancelButton.setDisable(true);
        addButton.setDisable(true);

        String currentUsername = CurrentUser.getInstance().getUserName();
        welcomeLabel.setText(currentUsername);

        boolean isDbConnected = databaseService.isDatabaseConnected();
        // Display database connection status
        dbStatusLight.setFill(isDbConnected ? Color.GREEN : Color.RED);

        handleActionColumn(); // Fetch and display users from the database.
        showUsers(); // Setup action column with edit and delete buttons.

    }

    /*
    JavaFX-EventHandler---------------------------------------------------------*/

    // Validation methods for user input fields.

    @FXML
    void userNameValidation(KeyEvent event) {
        String editedName = userNameField.getText();
        if (!Validator.isNameValid(editedName)) {
            errorValidationLabel.setText(AppTexts.VALID_USERNAME_4_15_CHARACTERS);
        } else {
            errorValidationLabel.setText("");
        }
        validateNewDaten();
    }

    @FXML
    void phoneValidation(KeyEvent event) {
        String editedPhone = userPhoneField.getText();
        if (!Validator.isPhoneValid(editedPhone)) {
            errorValidationLabel.setText(AppTexts.PHONE_NUMBER_WITH_AT_LEAST_11_DIGITS);
        } else {
            errorValidationLabel.setText("");
        }
        validateNewDaten();
    }

    @FXML
    void emailValidation(KeyEvent event) {
        String editedEmail = userEmailField.getText();
        if (!Validator.isEmailValid(editedEmail)) {
            errorValidationLabel.setText(AppTexts.ENTER_VALID_EMAIL);
        } else {
            errorValidationLabel.setText("");
        }
        validateNewDaten();
    }

    @FXML
    void PasswordValidation(KeyEvent event) {
        String editedPassword = userPasswordField.getText();
        if (!Validator.isPasswordStrong(editedPassword)) {
            errorValidationLabel.setText(AppTexts.PASSWORD_IS_NOT_LONG_ENOUGH);
        } else {
            errorValidationLabel.setText("");
        }
        validateNewDaten();
    }

    @FXML
    void addUserButtonHandler(ActionEvent event) {
        createNewUser(); // Create a new user with the provided details.
        showUsers(); // Refresh the user list.
    }

    @FXML
    void cancelBtnHandler(ActionEvent event) {
        clearTxtFieldsAndDisableBtns();
    }

    @FXML
    void loadUsersFromDbHandler(ActionEvent event) { // Show Users button
        showUsers();

    }


    /*
    Help Methods----------------------------------------------------------------*/

    // Validate input fields and dynamically enable/disable the add button.
    private void validateNewDaten() {
        String insertedName = userNameField.getText();
        String insertedPhone = userPhoneField.getText();
        String insertedEmail = userEmailField.getText();
        String insertedPassword = userPasswordField.getText();

        boolean areDatenValid = Validator.isNameValid(insertedName) &&
                                Validator.isPhoneValid(insertedPhone) &&
                                Validator.isEmailValid(insertedEmail) &&
                                Validator.isPasswordStrong(insertedPassword);

        addButton.setDisable(!areDatenValid); // Enable add button only if all fields are valid.

        boolean isAtLeastOneFieldFull =
                (!insertedName.trim().isBlank() ||
                 !insertedPhone.trim().isBlank() ||
                 !insertedEmail.trim().isBlank() ||
                 !insertedPassword.trim().isBlank()
                );
        cancelButton.setDisable(!isAtLeastOneFieldFull); // Enable cancel button if any field is filled.
    }

    // Clears input fields and disables buttons.
    private void clearTxtFieldsAndDisableBtns() {
        userNameField.clear();
        userPhoneField.clear();
        userEmailField.clear();
        userPasswordField.clear();

        cancelButton.setDisable(true);
        addButton.setDisable(true);
        isEditMode = false;

        addButton.setText("Add"); // Reset button text to "Add" for the next operation.
        addButton.setOnAction(this::addUserButtonHandler); // Ensure the correct event handler is set
        validateNewDaten(); // Re-validate fields.
    }

    // Fetches and displays users from the database.
    private void showUsers() {
        try {
            listOfUsers = userService.loadAllUsers(); // Fetch users.

            // Set cell value factories for table columns.
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
            userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("userPhone"));
            userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
            userPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("userPassword"));

            usersTable.setItems(listOfUsers); // Populate table with users.

            cancelButton.setDisable(true);
            addButton.setDisable(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Creates a new user based on input fields.
    private void createNewUser() {

        isEditMode = false;

        // Fetch input data.
        String userNameInserted = userNameField.getText();
        String phoneInserted = userPhoneField.getText();
        String emailInserted = userEmailField.getText();
        String passwordInserted = userPasswordField.getText();

        // Check if email is available before creating a new user.
        if (userService.isEmailAvailable(emailInserted)) {
            try {
                userService.addNewUser(userNameInserted, phoneInserted, emailInserted, passwordInserted);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            clearTxtFieldsAndDisableBtns(); // Reset UI.
        } else {
            AlertManager.getInstance().showErrorAlert(AppTexts.REGISTER_FAILED, AppTexts.THIS_EMAIL_IS_ALREADY_USED);
        }

    }

    /**
     * Creates a button with an icon.
     *
     * @param iconPath The path to the icon image file relative to the class's location.
     * @return A Button instance displaying the specified icon.
     */
    private Button createIconButton(String iconPath) {

        URL url = getClass().getResource(iconPath); // Attempt to retrieve the URL of the icon image based on the given path.

        if (url == null) { // If the URL is null, the icon path is invalid or the resource cannot be found.
            throw new IllegalArgumentException(AppTexts.ICON_NOT_FOUND + iconPath);
        }

        Image icon = new Image(url.toString()); // Create a new Image instance from the URL of the icon.
        ImageView iconView = new ImageView(icon); // Create an ImageView to display the icon.

        // Set the preferred size of the icon.
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);

        Button button = new Button(); // Create a new Button instance.
        button.setGraphic(iconView); // Set the ImageView as the graphic (icon) of the button.

        return button;
    }

    // Dynamically creates edit and delete buttons for each user in the action column.
    private void handleActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createIconButton("/org/mr/abschlussprojekt/bikeRental/styles/delete-icon.png");
            private final Button editButton = createIconButton("/org/mr/abschlussprojekt/bikeRental/styles/edit-icon.png");

            private final HBox container = new HBox(5, deleteButton, editButton);

            {
                container.setAlignment(Pos.CENTER);
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    editUser(user);

                });
            }

            @Override
            protected void updateItem(Void item, boolean b) {
                super.updateItem(item, b);
                setGraphic(b ? null : container);
            }
        });
    }


    /**
     * Prepares the form for editing an existing user's information.
     *
     * @param user The user to be edited.
     */
    private void editUser(User user) {

        // Set the currently selected user to the user to be edited
        selectedUser = user;
        isEditMode = true; // The form is now in edit mode

        // Fill the form fields with the user's current information for editing
        userNameField.setText(user.getUserName());
        userPhoneField.setText(user.getUserPhone());
        userEmailField.setText(user.getUserEmail());
        userPasswordField.setText(user.getUserPassword());

        // Provide a tip for the admin on how to proceed with editing
        tipText.setText("Tip: Change the data and confirm by clicking 'Update'.");
        tipText.setWrapText(true);
        tipText.setMaxWidth(200);

        // Change the text of the addButton to "Update" to reflect the current action
        addButton.setText("Update");
        addButton.setOnAction(event -> { // Set the action of the addButton to call updateUser() when clicked
            updateUser();
        });

        // Enable the cancelButton and set its action to clear the form and reset to add new user mode
        cancelButton.setDisable(false);
        cancelButton.setOnAction(event -> {
            clearTxtFieldsAndDisableBtns();
            addButton.setText("Add"); // Reset the button text to "Add" for adding new users
            tipText.setText(""); // Clear the tip text
            selectedUser = null; // Clear the selection
            addButton.setOnAction(this::addUserButtonHandler); // Reset the action to add new user
        });
        validateNewDaten(); // Validate the current data in the form fields
    }


    /**
     * Updates the user's information in the database with the new data entered in the form.
     */
    private void updateUser() {

        // Check if there is a selected user to update
        if (selectedUser != null) {

            // Update the user's information in the database
            try {
                userService.updateUserInfo(selectedUser.getId(), userNameField.getText(), userPhoneField.getText(),
                        userEmailField.getText(), userPasswordField.getText());

                // Notify the admin of the successful update
                AlertManager.getInstance().showInformationAlert(AppTexts.UPDATE_SUCCESSFUL, null, AppTexts.SUCCESSFULLY_UPDATED_CONTENT);

                clearTxtFieldsAndDisableBtns(); // Clear the form and reset the UI
                showUsers(); // Refresh the list of users to reflect the update

                // Reset the addButton to add mode
                addButton.setText("Add");
                tipText.setText("");
                selectedUser = null;
                addButton.setOnAction(this::addUserButtonHandler); // Reset the action of the addButton to add new user

            } catch (RuntimeException e) {
                // Notify the admin if the update failed
                AlertManager.getInstance().showErrorAlert(AppTexts.UPDATE_FAILED, AppTexts.PROBLEM_UPDATING_USER_INFORMATION_TRY_AGAIN);
            }

        }
    }

    /**
     * Attempts to delete a selected user from the database.
     *
     * @param user The user to be deleted.
     */
    private void deleteUser(User user) {

        // Display a confirmation dialog to ensure the admin wants to proceed with deletion.
        Optional<ButtonType> result = AlertManager.getInstance().showConfirmationAlert(AppTexts.DELETE_USER, null, AppTexts.WANT_TO_DELETE_THIS_USER);

        // Proceed with deletion if the admin confirms the action.
        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {

                // Attempt to delete the user from the database using the user's ID.
                userService.deleteUser(user.getId());
                listOfUsers.remove(user); // Remove the user from the list displayed in the UI.
                addButton.requestFocus();

                // Notify the admin of successful deletion.
                AlertManager.getInstance().showInformationAlert(AppTexts.DELETION_SUCCESSFUL, null, AppTexts.BEEN_SUCCESSFULLY_DELETED);

            } catch (RuntimeException e) {
                // If an error occurs during deletion, notify the admin.
                AlertManager.getInstance().showErrorAlert(AppTexts.DELETION_FAILED, AppTexts.PROBLEM_DELETING_THE_USER_TRY_AGAIN);
            }
        }
        // Ensure the "Add" button is focused even if the admin cancels the deletion.
        addButton.requestFocus();
    }


}

