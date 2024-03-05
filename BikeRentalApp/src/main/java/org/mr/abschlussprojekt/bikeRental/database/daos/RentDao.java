package org.mr.abschlussprojekt.bikeRental.database.daos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.model.BikeRental;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.sql.*;
import java.time.LocalDate;

/**
 * (DAO) for handling bike rental operations.
 * This class provides methods to insert, retrieve, and update bike rental information in the database.
 */
public class RentDao {

    /*
   Constants
    ----------------------------------------------------------------------------------------------------------- */
    // SQL query to select rental records for a specific user, joined with bike and station details.
    // The results are ordered by the start date in descending order to show the most recent rentals first.
    public static final String SELECT_RENTAL_RECORDS_FOR_A_SPECIFIC_USER_SQL = "SELECT br.rental_id,b.bike_id, b.bike_model, s.station_address AS start_station ,br.start_date, br.return_date, br.rent_price, br.is_returned FROM bike_rentals br JOIN bikes b ON br.bike_id = b.bike_id JOIN stations s ON br.start_station_id = s.station_id WHERE br.user_id = ? ORDER BY br.start_date DESC";

    // Inserts a new bike rental record into the database.
    public static final String INSERT_INTO_BIKE_RENTALS_SQL = "INSERT INTO bike_rentals (bike_id, user_id, start_station_id , start_date, return_date, rent_price, is_returned) VALUES(?,?,?,?,?,?,FALSE)";

    // Updates the status of a bike to AVAILABLE in the database.
    public static final String UPDATE_BIKE_STATUS_TO_AVAILABLE_SQL = "UPDATE bikes SET bike_status = 'AVAILABLE' WHERE bike_id = ?";
    // Updates the return status of a bike rental to TRUE in the database.
    public static final String UPDATE_BIKE_RENTALS_IS_RETURNED_TRUE_SQL = "UPDATE bike_rentals SET is_returned = TRUE WHERE rental_id = ?";
    /*----------------------------------------------------------------------------------------------------------- */

    /**
     * Inserts a new bike rental record into the database.
     *
     * @param bikeId         The ID of the rented bike.
     * @param userId         The ID of the user renting the bike.
     * @param startStationId The ID of the start station where the bike is rented from.
     * @param startDate      The start date of the rental.
     * @param returnDate     The expected return date of the rental.
     * @param rentPrice      The price of the rental.
     * @param isRented       The rental status (always false when inserting a new record).
     * @param connection     The database connection to use.
     * @return true if the insertion was successful, false otherwise.
     */
    public static boolean insertBikeRental(int bikeId, int userId, int startStationId, LocalDate startDate, LocalDate returnDate, double rentPrice, boolean isRented, Connection connection) {


        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_INTO_BIKE_RENTALS_SQL)) {
            pstmt.setInt(1, bikeId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, startStationId);
            pstmt.setDate(4, java.sql.Date.valueOf(startDate));
            pstmt.setDate(5, java.sql.Date.valueOf(returnDate));
            pstmt.setDouble(6, rentPrice);
            pstmt.setBoolean(7, false); // is_returned is always FALSE when creating a new rental

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; // Return true if at least one row was affected

        } catch (Exception e) {
            System.out.println("Error while insertBikeRental: " + e.getMessage());
            return false; // Return false in case of any exception
        }

    }


    /**
     * Retrieves a list of bike rentals for a specific user.
     *
     * @param userId     The ID of the user whose rentals to retrieve.
     * @param connection The database connection to use.
     * @return An ObservableList of BikeRental objects.
     */
    public static ObservableList<BikeRental> getRentalsForUser(int userId, Connection connection) {

        // Create an empty ObservableList to hold BikeRental objects.
        ObservableList<BikeRental> rentalInfoList = FXCollections.observableArrayList();

        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_RENTAL_RECORDS_FOR_A_SPECIFIC_USER_SQL)) {

            pstmt.setInt(1, userId); // Set the user ID parameter in the query.

            // Execute the query and process the result set.
            try (ResultSet resultSet = pstmt.executeQuery()) {

                while (resultSet.next()) {

                    // Extract rental info from result set
                    int rentalId = resultSet.getInt(AppTexts.RENTAL_ID_COLUMN_DB);
                    int bikeId = resultSet.getInt(AppTexts.BIKE_ID_COLUMN_DB);
                    String bikeModel = resultSet.getString(AppTexts.BIKE_MODEL_COLUMN_DB);
                    String pickupStation = resultSet.getString(AppTexts.START_STATION_COLUMN_DB);
                    LocalDate pickupDate = resultSet.getDate(AppTexts.START_DATE_COLUMN_DB).toLocalDate();
                    LocalDate returnDate = resultSet.getDate(AppTexts.RETURN_DATE_COLUMN_DB).toLocalDate();
                    double rentPrice = resultSet.getDouble(AppTexts.RENT_PRICE_COLUMN_DB);
                    boolean isRented = resultSet.getBoolean(AppTexts.IS_RETURNED_COLUMN_DB);

                    // Add new BikeRental object to list
                    rentalInfoList.add(new BikeRental(rentalId, bikeId, bikeModel, pickupStation, pickupDate, returnDate, rentPrice, isRented));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetch rental Bikes: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return rentalInfoList; // Return the list of rentals
    }


    /**
     * Updates the status of a bike to AVAILABLE in the database.
     *
     * @param bikeId     The ID of the bike to update.
     * @param connection The database connection to use.
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updateBikeStatusToAvailable(int bikeId, Connection connection) {

        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_BIKE_STATUS_TO_AVAILABLE_SQL)) {

            pstmt.setInt(1, bikeId);

            // Execute the update query and store the number of affected rows.
            // If a row is updated, it means the bike's status was successfully changed to AVAILABLE.
            int affectRows = pstmt.executeUpdate();

            // Return true if one or more rows were affected, indicating a successful update.
            return affectRows > 0;

        } catch (SQLException e) {
            System.out.println("Error while updateBikeStatusToAvailable: " + e.getMessage());
            // Return false to indicate the update operation failed.
            return false;
        }
    }


    /**
     * Updates the return status of a bike rental to TRUE in the database, indicating that the bike has been returned.
     *
     * @param rentalId   The ID of the rental to update.
     * @param connection The database connection to use.
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updateRentalReturnStatus(int rentalId, Connection connection) {

        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_BIKE_RENTALS_IS_RETURNED_TRUE_SQL)) {

            pstmt.setInt(1, rentalId);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; // Return true if at least one row was affected

        } catch (SQLException e) {
            System.out.println("Error while updateRentalReturnStatus: " + e.getMessage());
            return false;
        }

    }
}
