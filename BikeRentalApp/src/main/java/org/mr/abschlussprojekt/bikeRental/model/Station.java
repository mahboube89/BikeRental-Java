package org.mr.abschlussprojekt.bikeRental.model;

import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

/**
 * Represents a station where bikes can be rented or returned.
 */
public class Station {

    /*
    Attributes
    -------------------------------------------------------- */
    private int stationId;
    private String stationAddress;


    /*
   Constructors
   -------------------------------------------------------- */

    /**
     * Constructs a new Station with default values.
     */
    public Station() {
        this(AppTexts.DEFAULT_NUMBER_VALUE, AppTexts.DEFAULT_STRING_VALUE);
    }

    /**
     * Constructs a new Station with a specified ID and address.
     *
     * @param stationId      The unique ID of the station.
     * @param stationAddress The address of the station.
     */
    public Station(int stationId, String stationAddress) {
        setStationId(stationId);
        setStationAddress(stationAddress);
    }

    /*
   Getter & Setter
   -------------------------------------------------------- */
    // Station ID
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    // Station Address
    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        if (stationAddress == null || stationAddress.trim().isEmpty()) {
            throw new IllegalArgumentException(stationAddress + AppTexts.CANNOT_BE_EMPTY);
        }
        this.stationAddress = stationAddress;
    }



    /*
   Overrides Methods
   -------------------------------------------------------- */
    @Override
    public String toString() {
        return stationAddress;
    }
}
