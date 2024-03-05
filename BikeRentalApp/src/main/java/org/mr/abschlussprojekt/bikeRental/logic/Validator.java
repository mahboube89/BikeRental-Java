package org.mr.abschlussprojekt.bikeRental.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides validation methods for various types of user input.
 * This class helps in ensuring that user inputs meet specific criteria before being processed or stored.
 */
public class Validator {


    /**
     * Validates if a phone number is valid based on predefined criteria.
     * Criteria: Must not be blank, must be at least 11 characters long, and must only contain digits.
     *
     * @param phone The phone number to validate.
     * @return "true" if the phone number is valid, "false" otherwise.
     */
    public static boolean isPhoneValid(String phone) {
        return phone != null && !phone.isBlank() && phone.length() >= 11 && phone.chars().allMatch(Character::isDigit);
    }


    /**
     * Validates if an email address is valid based on a regular expression pattern.
     * Criteria: Must not be blank and must match the defined email pattern.
     *
     * @param email The email address to validate.
     * @return "true" if the email address is valid, "false" otherwise.
     */
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return !email.isBlank() && matcher.matches();
    }


    /**
     * Validates if a password is considered strong based on predefined criteria.
     * Criteria: Must not be blank and must be at least 5 characters long.
     *
     * @param password The password to validate.
     * @return "true" if the password is considered strong, "false" otherwise.
     */
    public static boolean isPasswordStrong(String password) {
        return password != null && !password.isBlank() && password.length() >= 5;
    }


    /**
     * Validates if a name is valid based on predefined criteria.
     * Criteria: Must not be blank, must be at least 4 characters long, and no longer than 15 characters.
     *
     * @param name The name to validate.
     * @return "true" if the name is valid, "false" otherwise.
     */
    public static boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.trim().length() >= 4 && name.trim().length() <= 15;
    }
}
