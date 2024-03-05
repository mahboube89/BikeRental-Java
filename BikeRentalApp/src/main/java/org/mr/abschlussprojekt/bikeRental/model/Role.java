package org.mr.abschlussprojekt.bikeRental.model;

/**
 * Represents the roles available within the bike rental system.
 * Each role defines a set of permissions and access levels to various features of the application.
 */
public enum Role {
    /**
     * Admin role with access to all administrative features,
     * including managing users, bikes.
     */
    ADMIN,

    /**
     * Regular user role with access to bike rental features,
     * such as searching for bikes, renting bikes, and viewing their rental history.
     */
    USER
}
