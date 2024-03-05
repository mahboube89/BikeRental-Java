package org.mr.abschlussprojekt.bikeRental.database;

import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class responsible for managing database connections.
 * Ensures that only one instance of the database connection is active throughout the application lifecycle.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private boolean isConnected;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private DatabaseManager() {
    }

    /**
     * Gets the single instance of the DatabaseManager.
     *
     * @return The single instance of the DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    /**
     * Provides the database connection. If the connection is not open, attempts to open it.
     *
     * @return An active database connection.
     */
    public Connection getConnection() {
        if (!isConnected) {
            openConnection();
        }
        return connection;
    }

    /**
     * Open a connection to the database if it is not already open.
     */
    private void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                //Replace "AppTexts.DB_CONNECTION_URL" and "AppTexts.DB_USER_NAME" and "Password" with your actual database credentials.
                connection = DriverManager.getConnection(AppTexts.DB_CONNECTION_URL, AppTexts.DB_USER_NAME, "root2401");

                isConnected = true;
            }
        } catch (SQLException e) {
            isConnected = false;
            System.out.println("Connection Error" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * Checks if there is an active database connection.
     *
     * @return true if connected, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

}
