package org.mr.abschlussprojekt.bikeRental.database.daos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.model.*;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles database operations for bikes, including loading, updating, and deleting bike records.
 */
public class BikeDao {

    /*
    Constants
    ---------------------------------------------------------------------------------- */
    // SQL statement to insert a new bike into the database.
    public static final String INSERT_INTO_BIKES_SQL = "INSERT INTO bikes(bike_model,bike_type, bike_price, station_id, bike_status  ) VALUES(?,?,?,?, ?)";

    // SQL query to select bike details and join with the stations table to get the station address.
    public static final String READ_ALL_BIKES_SQL = "SELECT b.bike_id, b.bike_model, b.bike_type, b.bike_price, b.bike_status, s.station_address FROM bikes b JOIN stations s ON b.station_id = s.station_id";

    // SQL query to check the current status of the bike.
    public static final String CHECK_STATUS_SQL = "SELECT bike_status FROM bikes WHERE bike_id = ?";

    // SQL query to update bike information.
    public static final String UPDATE_BIKE_INFO_SQL = "UPDATE bikes SET bike_model = ?, bike_type = ?, bike_price = ?, bike_status = ?, station_id = ? WHERE bike_id = ?";

    // SQL statement to delete a bike based on its ID.
    public static final String DELETE_FROM_BIKES_WHERE_BIKE_ID = "DELETE FROM bikes WHERE bike_id = ?";

    /*---------------------------------------------------------------------------------*/

    /**
     * Adds a new bike to the database.
     *
     * @param model          The model of the new bike.
     * @param type           The type of the new bike.
     * @param price          The price of the new bike.
     * @param stationId      The station ID where the new bike is located.
     * @param statusInserted The status of the new bike.
     * @param connection     The database connection to use for adding the new bike.
     */
    public static void addNewBike(String model, BikeType type, double price, int stationId, BikeStatus statusInserted, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_BIKES_SQL)) {

            preparedStatement.setString(1, model);
            preparedStatement.setString(2, type.toString());
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, stationId);
            preparedStatement.setString(5, statusInserted.toString());

            // Executes the SQL statement to insert the new bike.
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while add new Bike " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all bikes from the database.
     *
     * @param connection The database connection to use for the query.
     * @return An ObservableList of all Bike objects.
     */
    public static ObservableList<Bike> loadAllBikes(Connection connection) {
        ObservableList<Bike> bikesList = FXCollections.observableArrayList(); // Initialize an empty ObservableList to store Bike objects.

        try (PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_BIKES_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery(); // Execute the query and get the result set.

            // Iterate through the result set.
            while (resultSet.next()) {
                int bikeId = resultSet.getInt(AppTexts.BIKE_ID_COLUMN_DB);
                String bikeModel = resultSet.getString(AppTexts.BIKE_MODEL_COLUMN_DB);
                String bikeTypeStr = resultSet.getString(AppTexts.BIKE_TYPE_COLUMN_DB);
                double bikePrice = resultSet.getDouble(AppTexts.BIKE_PRICE_COLUMN_DB);
                String bikeStatusStr = resultSet.getString(AppTexts.BIKE_STATUS_COLUMN_DB);
                String bikeLocation = resultSet.getString(AppTexts.STATION_ADDRESS_COLUMN_DB);

                // Convert string values to enums for bike type and status.
                BikeType bikeType = BikeType.valueOf(bikeTypeStr.toUpperCase());
                BikeStatus bikeStatus = BikeStatus.valueOf(bikeStatusStr.toUpperCase());

                // Create a new Bike object and add it to the bikesList.
                bikesList.add(new Bike(bikeId, bikeModel, bikeType, bikePrice, bikeStatus, bikeLocation));

            }
            return bikesList; // Return the list of bikes.

        } catch (SQLException e) {
            System.out.println("Error while loadAllBikes" + e.getMessage());
            throw new RuntimeException(e);
        }

    }



    /**
     * Updates the information of a specific bike in the database.
     *
     * @param id             The ID of the bike to update.
     * @param bikeModel      The new model of the bike.
     * @param bikeType       The new type of the bike.
     * @param bikePrice      The new price of the bike.
     * @param bikeStatus     The new status of the bike.
     * @param stationAddress The new station address of the bike.
     * @param connection     The database connection to use for the update.
     */
    public static void updateBikeInfo(int id, String bikeModel, BikeType bikeType, Double bikePrice, BikeStatus bikeStatus, String stationAddress, Connection connection) {

        try (PreparedStatement checkStatusStmt = connection.prepareStatement(CHECK_STATUS_SQL)) {

            checkStatusStmt.setInt(1, id); // Set the bike ID

            ResultSet resultSet = checkStatusStmt.executeQuery();

            // If the bike is currently rented, throw an exception to prevent updating.
            if (resultSet.next() && BikeStatus.valueOf(resultSet.getString(AppTexts.BIKE_STATUS_COLUMN_DB)) == BikeStatus.RENTED) {
                throw new SQLException("Cannot update a rented bike.");
            }

        } catch (SQLException e) {
            System.out.println("Error while update bike Info: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Get the station ID based on the given station address.
        int stationId = StationDao.getStationIdByLocation(stationAddress, connection);

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BIKE_INFO_SQL)) {
            // Set parameters for the update query.
            preparedStatement.setString(1, bikeModel);
            preparedStatement.setString(2, bikeType.toString());
            preparedStatement.setDouble(3, bikePrice);
            preparedStatement.setString(4, bikeStatus.toString());
            preparedStatement.setInt(5, stationId);
            preparedStatement.setInt(6, id);

            // Execute the update query.
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while update bikes info" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Deletes a bike from the database.
     *
     * @param id         The ID of the bike to delete.
     * @param connection The database connection to use for the deletion.
     */
    public static void deleteBikeFromDB(int id, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_BIKES_WHERE_BIKE_ID)) {

            preparedStatement.setInt(1, id); // Sets the bike ID in the prepared statement.

            // Executes the delete operation.
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while deleting the bike" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
