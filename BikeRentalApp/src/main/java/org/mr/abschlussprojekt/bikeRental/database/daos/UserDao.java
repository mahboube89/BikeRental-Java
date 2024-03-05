package org.mr.abschlussprojekt.bikeRental.database.daos;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.model.User;
import org.mr.abschlussprojekt.bikeRental.model.Role;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

/**
 * (DAO) related to users in the database.
 * It includes methods to add, update, delete, and query user information.
 */
public class UserDao {

     /*
    Constants
    ------------------------------------------------------------------------------------------------------------------ */

    // Adds a new user to the database.
    public static final String INSERT_NEW_USER_SQL = "INSERT INTO users (user_name, user_phone, user_email, password) VALUES (?, ?, ?, ?)";

    // Select a user from the users table by user ID
    public static final String SELECT_USER_BY_ID_SQL = "SELECT * FROM users WHERE user_id = ?";

    // Count the number of users with the specified email
    public static final String COUNT_EMAIL_FROM_USERS_SQL = "SELECT COUNT(user_id) FROM users WHERE user_email = ?";

    // Select all users with the role of 'user'
    public static final String SELECT_ALL_USERS_WITH_ROLE_OF_USER_SQL = "SELECT * FROM users WHERE role = 'user'";

    // Updates the information of an existing user in the database.
    public static final String UPDATE_USER_INFOS_SQL = "UPDATE users SET user_name = ?, user_phone= ?, user_email =?, password= ? WHERE user_id = ?";

    // Update the user's name and phone number based on their ID
    public static final String UPDATE_NAME_AND_PHONE_SQL = "UPDATE users SET user_name = ?, user_phone= ? WHERE user_id = ?";

    // Changes the password for a user in the database.
    public static final String UPDATE_PASSWORD_SQL = "UPDATE users SET password = ? WHERE user_id = ?";

    // Deletes a user
    public static final String DELETE_USER_SQL = "DELETE FROM users WHERE user_id = ? ";

    // Insert a new user into the users table.
    public static final String INSERT_NEW_USER_INTO_USERS_SQL = "INSERT INTO users (user_name, user_phone, user_email, password) VALUES (?, ?, ?, ?)";

    /*----------------------------------------------------------------------------------------------------------------*/


    /**
     * Adds a new user to the database.
     *
     * @param name       The user's name.
     * @param phone      The user's phone number.
     * @param email      The user's email address.
     * @param password   The user's password.
     * @param connection The database connection object.
     */
    public static void addNewUser(String name, String phone, String email, String password, Connection connection) {

        // Insert a new user record into the database
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_USER_SQL)) {

            // Set the values for the placeholders in the SQL statement
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding new user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Registers a new user in the database and returns the user's generated ID.
     *
     * @param userName The name of the user to be registered.
     * @param phone The phone number of the user.
     * @param email The email address of the user. This should be unique across all users.
     * @param password The password for the user.
     * @param connection The database connection object used to execute the SQL statement.
     * @return The auto-generated user ID of the newly registered user if the operation is successful; -1 otherwise.
     */
    public static int registerUserAndGetId(String userName, String phone, String email, String password, Connection connection) {

        ResultSet rs = null;

        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_NEW_USER_INTO_USERS_SQL, Statement.RETURN_GENERATED_KEYS)) {
            // Setting the prepared statement parameters.
            pstmt.setString(1, userName);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            // Executing the update and getting the number of affected rows.
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }



    /**
     * Retrieves a user by their ID from the database.
     *
     * @param userId     The ID of the user to retrieve.
     * @param connection The database connection object.
     * @return A User object if found, null otherwise.
     */
    public static User getUserById(int userId, Connection connection) {

        User user = null; // Initialize user object to null. It will be changed if a user is found.

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_SQL)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery(); // Execute the query

            if (resultSet.next()) {
                // Instantiate a new User object and populate it with the query results
                user = new User();
                user.setId(resultSet.getInt(AppTexts.USER_ID_COLUMN_DB));
                user.setUserName(resultSet.getString(AppTexts.USER_NAME_COLUMN_DB));
                user.setUserPhone(resultSet.getString(AppTexts.USER_PHONE_COLUMN_DB));
                user.setUserEmail(resultSet.getString(AppTexts.USER_EMAIL_COLUMN_DB));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving user by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return user; // Return the populated User object, or null if no user was found
    }



    /**
     * Checks if an email address is available (not already used by another user).
     *
     * @param email      The email address to check.
     * @param connection The database connection object.
     * @return true if the email is available, false otherwise.
     */
    public static boolean isEmailAvailable(String email, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_EMAIL_FROM_USERS_SQL)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery(); // Execute the query

            // Check if the query returned a result
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0; // Email is available if count is 0
            }
        } catch (SQLException e) {
            System.out.println("Error checking email availability: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }


    public static String getCurrentPassword(int userId, Connection connection){
        String query = "SELECT password FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                } else {
                    throw new SQLException("User not found");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error by getting current password: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Loads all users with the role of 'user' from the database.
     *
     * @param connection The database connection object.
     * @return An ObservableList of User objects.
     */
    public static ObservableList<User> loadAllUsers(Connection connection) {
        // Initialize an ObservableList to hold User objects
        ObservableList<User> usersList = FXCollections.observableArrayList();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_WITH_ROLE_OF_USER_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Extract user details
                int userId = resultSet.getInt(AppTexts.USER_ID_COLUMN_DB);
                String userName = resultSet.getString(AppTexts.USER_NAME_COLUMN_DB);
                String phone = resultSet.getString(AppTexts.USER_PHONE_COLUMN_DB);
                String email = resultSet.getString(AppTexts.USER_EMAIL_COLUMN_DB);
                String password = resultSet.getString(AppTexts.USER_PASSWORD_COLUMN_DB);

                String roleStr = resultSet.getString(AppTexts.ROLE_COLUMN_DB);
                Role role = Role.valueOf(roleStr.toUpperCase()); // Convert the role string to the Role enum

                // Add a new User object to the list with the extracted details
                usersList.add(new User(userId, userName, phone, email, password, role));

            }
            return usersList; // Return the list of User objects

        } catch (SQLException e) {
            System.out.println("Error loading all users: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }


    /**
     * Updates the information of an existing user in the database.
     *
     * @param id         The ID of the user to update.
     * @param userName   The new username.
     * @param phone      The new phone number.
     * @param email      The new email address.
     * @param password   The new password.
     * @param connection The database connection object.
     */
    public static void updateUserInfo(int id, String userName, String phone, String email, String password, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_INFOS_SQL)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            System.out.println("Error updating user info: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    /**
     * Updates the user's information in their profile, excluding email and password.
     *
     * @param id         The ID of the user to update.
     * @param userName   The new username.
     * @param phone      The new phone number.
     * @param connection The database connection object.
     */
    public static void updateUserInfoInProfile(int id, String userName, String phone, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NAME_AND_PHONE_SQL)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, phone);
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            System.out.println("Error updating user info in profile: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    /**
     * Changes the password for a user in the database.
     *
     * @param id          The ID of the user to update.
     * @param newPassword The new password.
     * @param connection  The database connection object.
     */
    public static void changeUserPassword(int id, String newPassword, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_SQL)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            System.out.println("Error changing user password: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    /**
     * Deletes a user from the database.
     *
     * @param id         The ID of the user to delete.
     * @param connection The database connection object.
     */
    public static void deleteUser(int id, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
