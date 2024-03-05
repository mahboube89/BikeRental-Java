package org.mr.abschlussprojekt.bikeRental.database.services;

import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.BikeDao;
import org.mr.abschlussprojekt.bikeRental.model.Bike;
import org.mr.abschlussprojekt.bikeRental.model.BikeStatus;
import org.mr.abschlussprojekt.bikeRental.model.BikeType;

import java.sql.Connection;

public class BikeService {

    private final Connection connection;


    public BikeService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public ObservableList<Bike> loadAllBikes(){
        return BikeDao.loadAllBikes(connection);
    }

    public void addNewBike(String model, BikeType type, double price, int stationId, BikeStatus status) {
        BikeDao.addNewBike(model, type, price, stationId, status, connection);
    }

    public void updateBikeInfo(int id, String bikeModel, BikeType bikeType, Double bikePrice, BikeStatus bikeStatus, String stationAddress) {
        BikeDao.updateBikeInfo(id, bikeModel, bikeType, bikePrice, bikeStatus, stationAddress, connection);
    }

    public void deleteBikeFromDB(int id) {
        BikeDao.deleteBikeFromDB(id, connection);
    }
}
