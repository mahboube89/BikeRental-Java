package org.mr.abschlussprojekt.bikeRental.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * (DAO) provides database access methods related to the status of bikes.
 * It contains methods to update the status of a bike in the database.
 */
public class StatusDao {

    // update the bike_status column for a specific bike identified by bike_id
    public static final String UPDATE_BIKE_STATUS_SQL = "UPDATE bikes SET bike_status = ? WHERE bike_id = ?";



    /**
     * Updates the status of a specific bike in the database.
     *
     * @param bikeId     The ID of the bike whose status is to be updated.
     * @param newStatus  The new status to be set for the bike.
     * @param connection The connection to the database.
     * @return true if the update operation affected at least one row (indicating success),
     * false otherwise (indicating failure or no change).
     */
    public static boolean updateBikeStatus(int bikeId, String newStatus, Connection connection) {

        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_BIKE_STATUS_SQL)) {

            // Set the new status and bike ID in the prepared statement
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bikeId);

            // Execute the update operation and check if it affected any rows
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if one or more rows were updated, indicating success

        } catch (SQLException e) {
            System.out.println("Error updating bike status: " + e.getMessage());
            return false; // Return false if an exception is caught
        }
    }
}
