package org.mr.abschlussprojekt.bikeRental.logic;

/**
 * Singleton class representing the current user of the application.
 * This class stores information about the user currently logged into the system.
 */
public class CurrentUser {

    private static CurrentUser instance; // Singleton instance
    private String userName; // Username of the current user
    private int userId; // User ID of the current user

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private CurrentUser() {
    }


    /**
     * Provides access to the singleton instance of CurrentUser, creating it if it does not yet exist.
     *
     * @return The singleton instance of CurrentUser.
     */
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }


    /**
     * Gets the username of the current user.
     *
     * @return The username of the current user.
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Sets the username of the current user.
     *
     * @param userName The new username of the current user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Gets the user ID of the current user.
     *
     * @return The user ID of the current user.
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the user ID of the current user.
     *
     * @param userId The new user ID of the current user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


}
