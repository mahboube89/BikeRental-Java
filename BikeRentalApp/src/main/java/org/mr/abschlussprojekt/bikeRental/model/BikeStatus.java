package org.mr.abschlussprojekt.bikeRental.model;

/**
 * Enumerates the possible states of a bike in the rental system.
 */
public enum BikeStatus {
    AVAILABLE,      // The bike is available for rental.
    RENTED,         // The bike is currently rented out to a user.
    MAINTENANCE,    // The bike is undergoing maintenance and is not available for rental.
    CHARGING        // The bike is an electric bike currently being charged and is not available for rental.
}
