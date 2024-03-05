package org.mr.abschlussprojekt.bikeRental.database.services;

import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.LoginDao;
import org.mr.abschlussprojekt.bikeRental.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {

    private final Connection connection;

    public LoginService(){
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public User getUserIdAndRole(String username, String password) {
        return LoginDao.getUserIdAndRole(username, password, connection );
    }
}
