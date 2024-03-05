package org.mr.abschlussprojekt.bikeRental.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

/**
 * Represents a user of the bike rental system.
 * Manage user's details such as name, phone number, email, password, and role.
 */
public class User {

    /*
    Attributes
    -------------------------------------------------------- */
    private int id;
    private final StringProperty userName;
    private final StringProperty userPhone;
    private final StringProperty userEmail;
    private final StringProperty userPassword;
    private final ObjectProperty<Role> role;

    /*
    Constructors
    -------------------------------------------------------- */

    /**
     * Constructs a new User instance with default values.
     */
    public User() {
        this(AppTexts.DEFAULT_NUMBER_VALUE, AppTexts.DEFAULT_STRING_VALUE, AppTexts.DEFAULT_STRING_VALUE, AppTexts.DEFAULT_STRING_VALUE, AppTexts.DEFAULT_STRING_VALUE, Role.USER);
    }

    /**
     * Constructs a new User instance with specified details.
     *
     * @param id           Unique id for the user.
     * @param userName     The user's name.
     * @param userPhone    The user's phone number.
     * @param userEmail    The user's email address.
     * @param userPassword The user's password.
     * @param role         The role of the user, either ADMIN or USER.
     */
    public User(int id, String userName, String userPhone, String userEmail, String userPassword, Role role) {
        this.id = id;
        this.userName = new SimpleStringProperty(userName);
        this.userPhone = new SimpleStringProperty(userPhone);
        this.userEmail = new SimpleStringProperty(userEmail);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.role = new SimpleObjectProperty<>(role);

    }

    /**
     * @param id       Unique id for the user.
     * @param userName The user's name.
     * @param role     The role of the user, either ADMIN or USER.
     * Used in LoginDao
     */
    public User(int id, String userName, Role role) {
        this(id, userName, AppTexts.DEFAULT_STRING_VALUE, AppTexts.DEFAULT_STRING_VALUE, AppTexts.DEFAULT_STRING_VALUE, role);
    }

    /*
   Getter & Setter
   -------------------------------------------------------- */
    // User ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // User name
    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        validateNonNull(userName, "User Name");
        this.userName.set(userName);
    }

    // User Phone number
    public String getUserPhone() {
        return userPhone.get();
    }

    public StringProperty userPhoneProperty() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        validateNonNull(userPhone, "Phone Number");
        this.userPhone.set(userPhone);
    }

    // User E-Mail
    public String getUserEmail() {
        return userEmail.get();
    }

    public StringProperty userEmailProperty() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        validateNonNull(userEmail, "E-Mail");
        this.userEmail.set(userEmail);
    }

    // User Password
    public String getUserPassword() {
        return userPassword.get();
    }

    public StringProperty userPasswordProperty() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.set(userPassword);
    }

    // Role (Admin or User)
    public Role getRole() {
        return role.get();
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }

    public void setRole(Role role) {
        validateNonNull(String.valueOf(role), "Role");
        this.role.set(role);
    }

    /*
    Another Methods
    -------------------------------------------------------- */

    /**
     * Validates that the provided string is not null or empty.
     *
     * @param value     The string to validate.
     * @param fieldName The name of the field for error messages.
     */
    private void validateNonNull(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + AppTexts.CANNOT_BE_EMPTY);
        }
    }

     /*
    Overrides Methods
    -------------------------------------------------------- */

    /**
     * Returns a string representation of the user
     *
     * @return A string that contains the user's details.
     */
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", userName=" + userName +
               ", userPhone=" + userPhone +
               ", userEmail=" + userEmail +
               ", userPassword=" + userPassword +
               ", role=" + role +
               '}';
    }
}
