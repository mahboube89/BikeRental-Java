package org.mr.abschlussprojekt.bikeRental.database.services;

import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.StationDao;
import org.mr.abschlussprojekt.bikeRental.model.Station;

import java.sql.Connection;

public class StationService {

    private final Connection connection;

    public StationService(){
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public ObservableList<Station> getAllStations() {
        return StationDao.getAllStations(connection);
    }

    public int getStationIdByLocation(String stationAddress) {
        return StationDao.getStationIdByLocation(stationAddress, connection);
    }

}
