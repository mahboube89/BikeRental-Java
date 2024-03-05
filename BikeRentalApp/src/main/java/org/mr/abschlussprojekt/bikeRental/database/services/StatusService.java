package org.mr.abschlussprojekt.bikeRental.database.services;

import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.StationDao;
import org.mr.abschlussprojekt.bikeRental.database.daos.StatusDao;
import org.mr.abschlussprojekt.bikeRental.model.Station;

import java.sql.Connection;

public class StatusService {

    private final Connection connection;

    public StatusService(){
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean updateBikeStatus(int bikeId, String newStatus) {
        return StatusDao.updateBikeStatus(bikeId, newStatus,connection);
    }

}
