package org.mr.abschlussprojekt.bikeRental.database.services;

import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.RentDao;
import org.mr.abschlussprojekt.bikeRental.model.BikeRental;

import java.sql.Connection;
import java.time.LocalDate;

public class RentService {

    private final Connection connection;

    public RentService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean insertBikeRental(int bikeId, int userId, int startStationId, LocalDate startDate, LocalDate returnDate, double rentPrice, boolean isRented) {
        return RentDao.insertBikeRental(bikeId, userId, startStationId, startDate, returnDate, rentPrice, isRented, connection);
    }

    public ObservableList<BikeRental> getRentalsForUser(int userId){
        return RentDao.getRentalsForUser(userId, connection);
    }

    public boolean updateBikeStatusToAvailable(int bikeId) {
        return RentDao.updateBikeStatusToAvailable(bikeId, connection);
    }

    public boolean updateRentalReturnStatus(int rentalId) {
        return RentDao.updateRentalReturnStatus(rentalId, connection);
    }

}
