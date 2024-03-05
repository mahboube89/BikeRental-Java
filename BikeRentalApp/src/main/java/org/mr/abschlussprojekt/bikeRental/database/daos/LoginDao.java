package org.mr.abschlussprojekt.bikeRental.database.daos;

import org.mr.abschlussprojekt.bikeRental.model.Role;
import org.mr.abschlussprojekt.bikeRental.model.User;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * (DAO) for handling login operations.
 * This class provides access to the database for login functionality,
 */
public class LoginDao {

    /*
    Constants
    ----------------------------------------------------------------------------------------------- */
    // SQL query to retrieve the user ID and role for a user with the specified username and password.
    public static final String SELECT_USER_ID_ROLE_FROM_USERS_SQL = "SELECT user_id, role FROM users WHERE user_name = ? AND password = ?";

    /*----------------------------------------------------------------------------------------------- */

    /**
     * Retrieves a User object containing the user ID and role for a given username and password.
     *
     * @param username   The username of the user attempting to log in.
     * @param password   The password of the user attempting to log in.
     * @param connection The database connection to use for the query.
     * @return A User object containing the user's ID and role if are valid; null otherwise.
     */
    public static User getUserIdAndRole(String username, String password, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_ID_ROLE_FROM_USERS_SQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a user is found, return a User object.
            if (resultSet.next()) {
                int userId = resultSet.getInt(AppTexts.USER_ID_COLUMN_DB);
                String roleStr = resultSet.getString(AppTexts.ROLE_COLUMN_DB);

                Role role = Role.valueOf(roleStr.toUpperCase()); // Convert the role string to the Role enum.

                return new User(userId, username, role); // Return a new User object with the retrieved data.
            }

        } catch (SQLException e) {
            System.out.println("Error by getUserIdAndRole " + e.getMessage());
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null; // Return null if no user matches the provided credentials.
    }

}
