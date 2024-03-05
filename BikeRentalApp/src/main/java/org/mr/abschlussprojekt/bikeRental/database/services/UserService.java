package org.mr.abschlussprojekt.bikeRental.database.services;

import javafx.collections.ObservableList;
import org.mr.abschlussprojekt.bikeRental.database.DatabaseManager;
import org.mr.abschlussprojekt.bikeRental.database.daos.UserDao;
import org.mr.abschlussprojekt.bikeRental.model.User;

import java.sql.Connection;

public class UserService {

    private final Connection connection;

    public UserService(){
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void addNewUser(String name, String phone, String email, String password) {
        UserDao.addNewUser(name, phone, email, password, connection);
    }

    public int registerUserAndGetId(String userName, String phone, String email, String password) {
        return UserDao.registerUserAndGetId(userName, phone, email, password, connection);
    }

    public User getUserById(int userId) {
        return UserDao.getUserById(userId, connection);
    }

    public boolean isEmailAvailable(String email) {
        return UserDao.isEmailAvailable(email, connection);
    }

    public String getCurrentPassword(int userId){
        return UserDao.getCurrentPassword(userId, connection);
    }

    public ObservableList<User> loadAllUsers() {
        return UserDao.loadAllUsers(connection);
    }

    public void updateUserInfo(int id, String userName, String phone, String email, String password) {
        UserDao.updateUserInfo(id, userName, phone, email, password, connection);
    }

    public void updateUserInfoInProfile(int id, String userName, String phone) {
        UserDao.updateUserInfoInProfile(id, userName, phone, connection);
    }

    public void changeUserPassword(int id, String newPassword) {
        UserDao.changeUserPassword(id, newPassword, connection);
    }

    public void deleteUser(int id) {
        UserDao.deleteUser(id, connection);
    }
}
