package org.mr.abschlussprojekt.bikeRental.database.daos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.model.Station;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * (DAO) methods for interacting with station-related data in the database.
 */
public class StationDao {

    /*
    Constants
   ----------------------------------------------------------------------------------------------------------- */
    // Retrieves all stations from the database.
    public static final String SELECT_ALLE_STATIONS_SQL = "SELECT * FROM stations";

    // Fetches the ID of a station by its address.
    public static final String SELECT_STATION_ID_BY_ITS_ADDRESS_SQL = "SELECT station_id FROM stations WHERE station_address = ?";

    /*----------------------------------------------------------------------------------------------------------- */


    /**
     * Retrieves all stations from the database.
     *
     * @param connection The connection to the database.
     * @return An ObservableList of Station objects representing all the stations in the database.
     */
    public static ObservableList<Station> getAllStations(Connection connection) {
        // Create an empty ObservableList to hold stations.
        ObservableList<Station> stations = FXCollections.observableArrayList();

        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALLE_STATIONS_SQL);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                // Create a new Station object for each row in the result set
                Station station = new Station();
                station.setStationId(resultSet.getInt(AppTexts.STATION_ID_COLUMN_DB));
                station.setStationAddress(resultSet.getString(AppTexts.STATION_ADDRESS_COLUMN_DB));

                stations.add(station); // Add the Station object to the list of stations
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching stations: " + e.getMessage());
        }
        return stations; // Return the list of stations
    }


    /**
     * Fetches the ID of a station by its address.
     *
     * @param stationAddress The address of the station to look for.
     * @param connection     The connection to the database.
     * @return The ID of the station found, or throws an exception if no station matches the address.
     * @throws RuntimeException If no station is found with the provided address.
     */
    public static int getStationIdByLocation(String stationAddress, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STATION_ID_BY_ITS_ADDRESS_SQL)) {

            preparedStatement.setString(1, stationAddress);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Return the station ID of the first (and should be only) match
                return resultSet.getInt(AppTexts.STATION_ID_COLUMN_DB);

            } else {
                // If no result is found, throw an exception
                throw new RuntimeException("No station found with the given address: " + stationAddress);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching station ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
