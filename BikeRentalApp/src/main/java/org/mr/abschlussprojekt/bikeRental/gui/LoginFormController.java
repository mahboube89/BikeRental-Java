package org.mr.abschlussprojekt.bikeRental.gui;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mr.abschlussprojekt.bikeRental.database.services.DatabaseService;
import org.mr.abschlussprojekt.bikeRental.database.services.LoginService;
import org.mr.abschlussprojekt.bikeRental.database.services.UserService;
import org.mr.abschlussprojekt.bikeRental.logic.AlertManager;
import org.mr.abschlussprojekt.bikeRental.logic.ChangeSceneManager;
import org.mr.abschlussprojekt.bikeRental.logic.CurrentUser;
import org.mr.abschlussprojekt.bikeRental.logic.Validator;
import org.mr.abschlussprojekt.bikeRental.model.User;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller class for the main login form. Manages the transitions between the sign-in and sign-up views.
 */
public class LoginFormController implements Initializable {

    /*
    Attributes---------------------------------------------------------*/

    private LoginService loginService;
    private UserService userService;


    /*
    FXML Elements---------------------------------------------------------*/
    // Background and manage the transition between views
    @FXML
    private Pane parentContainer;
    @FXML
    private VBox signInBox;
    @FXML
    private VBox signUpBox;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Circle databaseLight;
    //------------------------------------

    // Radio Buttons
    @FXML
    private RadioButton adminRadioButton;
    @FXML
    private RadioButton userRadioButton;
    @FXML
    private ToggleGroup roleToggleGroup;
    //-------------------------------------

    // Sign In Form Controller
    @FXML
    private Button loginButton;
    @FXML
    private TextField signInPassword;
    @FXML
    private TextField signInUsername;
    //-------------------------------------

    // Sign Up Box Controller
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpPhone;
    @FXML
    private TextField signUpEmail;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpConfirmPassword;
    @FXML
    private Label message;
    @FXML
    private Button registerButton;



    /*
    FXML-Initialisierungsmethod---------------------------------------------------------*/
    // This method is automatically called after the FXML file has been loaded.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loginService = new LoginService();
        this.userService = new UserService();
        DatabaseService databaseService = new DatabaseService();

        loadContent(); // Set up the event handlers for the sign-in and sign-up buttons
        registerButton.setDisable(true); // after entering the register information the button will be active
        loginButton.setDisable(true); // after entering the login information the button will be active

        boolean isDbConnected = databaseService.isDatabaseConnected();
        databaseLight.setFill(isDbConnected ? Color.GREEN : Color.RED);
    }


    /*
    SignIn-JavaFX-EventHandler---------------------------------------------------------*/

    /**
     * Handles the login button click event.
     *
     * @param event The ActionEvent triggered by clicking the login button.
     */
    @FXML
    void loginButtonHandler(ActionEvent event) {

        // Retrieve username and password from text fields.
        String inUsernameInserted = signInUsername.getText();
        String inPasswordInserted = signInPassword.getText();
        // Get the selected role from the ToggleGroup
        RadioButton selectedRadioButton = (RadioButton) roleToggleGroup.getSelectedToggle();
        String selectedRole = selectedRadioButton.getText().toUpperCase();

        try {
            // Attempt to authenticate the user based on username and password.
            User user = loginService.getUserIdAndRole(inUsernameInserted, inPasswordInserted);

            // Check if a user was found and if the selected role matches the user's role.
            if (user != null && user.getRole().name().equals(selectedRole)) {
                // Update the current user's state to reflect the logged-in user.
                CurrentUser.getInstance().setUserName(user.getUserName());
                CurrentUser.getInstance().setUserId(user.getId());

                // Navigate to different parts of the application based on the user's role.
                switch (user.getRole()) {
                    case ADMIN:
                        // Display success popup and switch to admin dashboard.
                        PopUpWindowConfirm.displayPopup("Login as Admin Successfully");
                        ChangeSceneManager.getInstance().switchToAdminDashboard();
                        break;
                    case USER:
                        // Display success popup and switch to user home.
                        PopUpWindowConfirm.displayPopup("Login as User Successfully");
                        ChangeSceneManager.getInstance().switchToUserHome();
                        break;
                    default:
                        // Handle invalid roles.
                        PopUpWindowConfirm.displayPopup("Invalid Role");
                        break;
                }
                // Close the login stage/window.
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            } else {
                // Display an error popup if login was not successful or there was a role mismatch.
                PopUpWindowConfirm.displayPopup("Login Not Successfully or Role Mismatch");
                clearSignInFields(); // Clear the login form fields.
            }
        } catch (Exception e) {
            // Display an error alert if an exception occurs during the login process.
            AlertManager.getInstance().showErrorAlert("Login Error" , "An error occurred. Please try again later.");
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Validates the sign-in fields in real-time as the user types.
     * It checks if both username and password fields are not blank.
     * If either field is blank, it disables the login button to prevent submitting the form.
     */
    @FXML
    void signInFieldsValidation(KeyEvent event) {
        String inUsernameInserted = signInUsername.getText();
        String inPasswordInserted = signInPassword.getText();

        boolean isLoginFieldsFull = inUsernameInserted.isBlank() || inPasswordInserted.isBlank() ;
        loginButton.setDisable(isLoginFieldsFull);
    }

    /**
     * Clears the content of the sign-in fields (username and password) and disables the login button.
     * This method is typically called after a login attempt fails or when resetting the form.
     */
    private void clearSignInFields() {
        if (!signInUsername.getText().isEmpty()) { // Clear the username field if it's not empty
            signInUsername.clear();
        }

        if (!signInPassword.getText().isEmpty()) { // Clear the password field if it's not empty
            signInPassword.clear();
        }
        loginButton.setDisable(true); // Disable the register button
    }


    /*
    SignUp-JavaFX-EventHandler---------------------------------------------------------*/
    @FXML
    void registerButtonHandler(ActionEvent event) {
        String upUsernameInserted = signUpUsername.getText();
        String upPhoneInserted = signUpPhone.getText();
        String upEmailInserted = signUpEmail.getText();
        String upPasswordInserted = signUpPassword.getText();

        // Initialisieren Sie isUserRegistered mit false
        boolean isUserRegistered = false;

        if (userService.isEmailAvailable(upEmailInserted)) {

            int userId = userService.registerUserAndGetId(upUsernameInserted,upPhoneInserted,upEmailInserted,upPasswordInserted);

            if (userId > 0) {
                CurrentUser.getInstance().setUserId(userId);
                CurrentUser.getInstance().setUserName(upUsernameInserted);
                try {
                    PopUpWindowConfirm.displayPopup("Register Successfully");
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    stage.close();
                    ChangeSceneManager.getInstance().switchToUserHome();
                } catch (Exception e) {
                    PopUpWindowConfirm.displayPopup("Register Not Successfully");
                    e.printStackTrace();
                }
                clearSignUpFields();
            } else {
                AlertManager.getInstance().showErrorAlert("Register Failed", "Failed to register. Please try again.");
            }
        } else {
            AlertManager.getInstance().showErrorAlert("Register Failed", "This email is already used. Please try another one.");
        }
    }


    @FXML //Validates the entered username.
    void signUpUsernameValidation(KeyEvent event) {
        String upUsernameInserted = signUpUsername.getText();
        if (!Validator.isNameValid(upUsernameInserted)) {
            message.setText("Please enter a valid username (4-15 characters)");
        } else {
            message.setText("");
        }
        validateSignUpFields();
    }

    @FXML // Validates the entered phone number.
    void signUpPhoneValidation(KeyEvent event) {
        String upPhoneInserted = signUpPhone.getText();
        if (!Validator.isPhoneValid(upPhoneInserted)){
            message.setText("Please enter a valid phone number.");
        }else {
            message.setText("");
        }
        validateSignUpFields();
    }

    @FXML // Validates the entered email address.
    void signUpEmailValidation(KeyEvent event) {
        String upEmailInserted = signUpEmail.getText();
        if (!Validator.isEmailValid(upEmailInserted)) {
            message.setText("Please enter a valid email address");
        } else {
            message.setText("");
        }
        validateSignUpFields();
    }

    @FXML // Validates the entered password.
    void signUpPasswordValidation(KeyEvent event) {
        String upPasswordInserted = signUpPassword.getText().trim();

        if (!Validator.isPasswordStrong(upPasswordInserted)) {
            message.setText("Password must be at least 5 characters long");
        } else {
            message.setText("");
        }
        validateSignUpFields();
    }

    @FXML // Validates the confirmation password and ensures it matches the original password.
    void signUpConfirmPasswordValidation(KeyEvent event) {
        String upPasswordInserted = signUpPassword.getText().trim();
        String upConfirmPassInserted = signUpConfirmPassword != null ? signUpConfirmPassword.getText().trim() : "";

        if (!upPasswordInserted.equals(upConfirmPassInserted)) {
            message.setText("Passwords do not match");
        } else {
            message.setText("");
        }
        validateSignUpFields();
    }


     /*
    Help Methods---------------------------------------------------------*/

    /**
     * Validates all sign-up fields.
     */
    private void validateSignUpFields() {
        String upUsernameInserted = signUpUsername.getText();
        String upEmailInserted = signUpEmail.getText();
        String upPasswordInserted = signUpPassword.getText();
        String upConfirmPassInserted = signUpConfirmPassword != null ? signUpConfirmPassword.getText() : "";

        boolean isSignUpFieldsValid = Validator.isNameValid(upUsernameInserted) &&
                                      Validator.isEmailValid(upEmailInserted) &&
                                      Validator.isPasswordStrong(upPasswordInserted) &&
                                      Validator.isPasswordStrong(upConfirmPassInserted) &&
                                      upPasswordInserted.equals(upConfirmPassInserted);

        // Aktiviere oder deaktiviere den Button basierend auf den Validierungsbedingungen
        registerButton.setDisable(!isSignUpFieldsValid);

    }

    /**
     * Clears the sign-up form fields and disables the register button.
     */
    private void clearSignUpFields() {
        if (!signUpUsername.getText().isEmpty()) { // Clear the username field if it's not empty
            signUpUsername.clear();
        }
        if (!signUpPhone.getText().isEmpty()) { // Clear the phone field if it's not empty
            signUpUsername.clear();
        }

        if (!signUpEmail.getText().isEmpty()) { // Clear the email field if it's not empty
            signUpEmail.clear();
        }
        if (!signUpPassword.getText().isEmpty()) { // Clear the password field if it's not empty
            signUpPassword.clear();
        }
        if (!signUpConfirmPassword.getText().isEmpty()) { // Clear the confirm password field if it's not empty
            signUpConfirmPassword.clear();
        }
        if (!message.getText().trim().isEmpty()) { // Clear any displayed error message
            message.setText("");
        }
        registerButton.setDisable(true); // Disable the register button
    }


    /*
    Manages the transitions Methods ----------------------------------------------------------------------*/


    /**
     * Sets up the action events for the sign-in and sign-up buttons.
     */
    private void loadContent() {
        // Assign the showSignUpBox method to execute when the signUpButton is pressed
        signInButton.setOnAction(event -> showSignInBox());
        // Assign the showSignInBox method to execute when the signInButton is pressed
        signUpButton.setOnAction(event -> showSignUpBox());
    }


    /**
     * Creates a TranslateTransition for a specified node.
     *
     * @param node     The node to animate.
     * @param toX      The ending X position for the transition.
     * @param duration The duration of the transition.
     * @return A TranslateTransition object.
     */
    private TranslateTransition createTransition(Node node, double toX, Duration duration) {
        TranslateTransition transition = new TranslateTransition(duration, node);
        transition.setToX(toX);
        return transition;
    }


    /**
     * Shows the sign-up box by sliding it into view and sliding the sign-in box out of view.
     */
    private void showSignUpBox() {
        // Hides the sign-in box by sliding it out of view
        TranslateTransition hideSignIn = createTransition(signInBox, (-parentContainer.getWidth()), Duration.seconds(0.5));

        // Makes the sign-in box invisible and re-enables the sign-up button after the transition
        hideSignIn.setOnFinished(event -> {
            signInBox.setVisible(false);
            signUpButton.setDisable(false);
        });
        // Disables the sign-up button to prevent multiple clicks during the transition
        signUpButton.setDisable(true);
        signUpBox.setVisible(true);

        // Shows the sign-up box by sliding it into view
        TranslateTransition showSignUp = createTransition(signUpBox, -(parentContainer.getWidth() - signInBox.getWidth()), Duration.seconds(0.5));

        hideSignIn.play();
        showSignUp.play();
    }


    /**
     * Shows the sign-in box by sliding it into view and sliding the sign-up box out of view.
     */
    private void showSignInBox() {
        clearSignUpFields();

        TranslateTransition hideSignUp = createTransition(signUpBox, (parentContainer.getWidth() - signInBox.getWidth()), Duration.seconds(0.5));

        hideSignUp.setOnFinished(event -> { // Makes the sign-up box invisible and re-enables the sign-in button after the transition
            signUpBox.setVisible(false);
            signInButton.setDisable(false);
        });
        signInButton.setDisable(true); // Disables the sign-in button to prevent multiple clicks during the transition
        signInBox.setVisible(true);

        // Shows the sign-in box by sliding it into view
        TranslateTransition showSignIn = createTransition(signInBox, 0, Duration.seconds(0.5));

        hideSignUp.play();
        showSignIn.play();
    }

}