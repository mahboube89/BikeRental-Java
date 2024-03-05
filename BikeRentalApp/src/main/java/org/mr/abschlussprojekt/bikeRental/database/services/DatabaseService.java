package org.mr.abschlussprojekt.bikeRental.database.services;

import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;

public class DatabaseService {

    public boolean isDatabaseConnected() {
        return DatabaseManager.getInstance().isConnected();
    }
}
