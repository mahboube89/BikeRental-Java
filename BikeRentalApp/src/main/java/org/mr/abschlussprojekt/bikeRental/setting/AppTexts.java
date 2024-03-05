package org.mr.abschlussprojekt.bikeRental.setting;

public class AppTexts {
    /*
    Constants -------------------------------------------------------- */
    public static final String DEFAULT_STRING_VALUE = "Unknown";
    public static final double DEFAULT_PRICE_VALUE = 0.0;
    public static final int DEFAULT_NUMBER_VALUE = -1 ;
    public static final String CANNOT_BE_EMPTY = "  cannot be empty.";


    public static final String DB_CONNECTION_URL = "jdbc:mariadb://localhost/BikeRentalDB";
    public static final String DB_USER_NAME = "root";


    public static final String USER_ID_COLUMN_DB = "user_id";
    public static final String USER_NAME_COLUMN_DB = "user_name";
    public static final String USER_PHONE_COLUMN_DB = "user_phone";
    public static final String USER_EMAIL_COLUMN_DB = "user_email";
    public static final String USER_PASSWORD_COLUMN_DB = "password";
    public static final String ROLE_COLUMN_DB = "role";


    public static final String LOGIN_FORM_FXML_PATH = "login-form-view.fxml";
    public static final String USER_MENU_FXML_PATH = "user-menu-view.fxml";
    public static final String ADMIN_DASHBOARD_FXML_PATH = "admin-dashboard-view.fxml";


    public static final String VALID_USERNAME_4_15_CHARACTERS = "Please enter a valid username (4-15 characters)";
    public static final String PHONE_NUMBER_WITH_AT_LEAST_11_DIGITS = "Please enter a valid phone number with at least 11 digits.";
    public static final String UPDATE_SUCCESSFUL = "Update Successful";
    public static final String SUCCESSFULLY_UPDATED_CONTENT = "User information has been successfully updated.";
    public static final String PROBLEM_UPDATING_USER_INFORMATION_TRY_AGAIN = "There was a problem updating the user information. Please try again.";
    public static final String UPDATE_FAILED = "Update Failed";


    public static final String DELETE_USER = "Delete User";
    public static final String WANT_TO_DELETE_THIS_USER = "Are you sure you want to delete this user?";
    public static final String DELETION_SUCCESSFUL = "Deletion Successful";
    public static final String BEEN_SUCCESSFULLY_DELETED = "User has been successfully deleted.";
    public static final String DELETION_FAILED = "Deletion Failed";
    public static final String PROBLEM_DELETING_THE_USER_TRY_AGAIN = "There was a problem deleting the user. Please try again.";


    public static final String VALIDATION_ERROR = "Validation Error";
    public static final String NEW_PASSWORD_VALIDATION_FAILED = "New password validation failed.";
    public static final String INCORRECT_OLD_PASSWORD = "Incorrect Old Password";
    public static final String THE_OLD_PASSWORD_YOU_ENTERED_IS_INCORRECT = "The old password you entered is incorrect.";
    public static final String PASSWORD_SUCCESSFULLY_CHANGED = "Your password has been successfully changed.";
    public static final String PASSWORD_CHANGE = "Password Change";
    public static final String PASSWORD_CHANGE_ERROR = "Password Change Error";
    public static final String FAILED_TO_CHANGE_PASSWORD = "Failed to change password. Please try again.";
    public static final String NEW_PASSWORDS_DO_NOT_MATCH = "New passwords do not match.";
    public static final String PASSWORD_IS_NOT_LONG_ENOUGH = "Password is not long enough. Minimum length is 5 characters.";


    public static final String ERROR_DURING_INITIALIZATION = "Error during initialization: ";
    public static final String RETURN_BIKE = "Return Bike";
    public static final String SURE_WANT_RETURN_BIKE = "Are you sure you want to return this bike?";
    public static final String BIKE_ISRETURNED_AND_IS_NOW_AVAILABLE = "The bike has been successfully returned and is now available.";
    public static final String RETURN_SUCCESSFUL = "Return Successful";
    public static final String RETURN_FAILED = "Return Failed";
    public static final String FAILED_TO_RETURN_BIKE = "Failed to return the bike. Please try again.";
    public static final String RETURN_BUTTON_TXT = "Return";
    public static final String COMPLETED_BUTTON_TXT = "Completed";


    public static final String BIKE_ID_COLUMN_DB = "bike_id";
    public static final String BIKE_MODEL_COLUMN_DB = "bike_model";
    public static final String BIKE_TYPE_COLUMN_DB = "bike_type";
    public static final String BIKE_PRICE_COLUMN_DB = "bike_price";
    public static final String BIKE_STATUS_COLUMN_DB = "bike_status";

    public static final String STATION_ADDRESS_COLUMN_DB = "station_address";
    public static final String STATION_ID_COLUMN_DB = "station_id";


    public static final String RENTAL_ID_COLUMN_DB = "rental_id";
    public static final String START_STATION_COLUMN_DB = "start_station";
    public static final String START_DATE_COLUMN_DB = "start_date";
    public static final String RETURN_DATE_COLUMN_DB = "return_date";
    public static final String RENT_PRICE_COLUMN_DB = "rent_price";
    public static final String IS_RETURNED_COLUMN_DB = "is_returned";


    public static final String ENTER_VALID_EMAIL = "Please enter a valid email address";
    public static final String REGISTER_FAILED = "Register Failed";
    public static final String THIS_EMAIL_IS_ALREADY_USED = "This email is already used. Please try another one.";
    public static final String ICON_NOT_FOUND = "Icon NOT found: ";
}
