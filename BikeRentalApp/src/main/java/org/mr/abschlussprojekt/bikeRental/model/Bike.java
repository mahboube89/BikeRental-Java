package org.mr.abschlussprojekt.bikeRental.model;

import javafx.beans.property.*;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

/**
 * Represents a bike with properties such as model, type, price, status, and location.
 * Manage bike information for a bike rental application.
 */
public class Bike {

    /*
    Attributes
    -------------------------------------------------------- */
    private int bikeId;
    private final StringProperty bikeModel;
    private final ObjectProperty<BikeType> bikeType;
    private double bikePrice;
    private final ObjectProperty<BikeStatus> bikeStatus;
    private final StringProperty bikeLocation;

     /*
    Constructors
    --------------------------------------------------------------------------- */
    /**
     * Default constructor that initializes a bike instance with default values.
     */
    public Bike() {
        this(AppTexts.DEFAULT_NUMBER_VALUE,
                AppTexts.DEFAULT_STRING_VALUE,
                BikeType.CITY,
                AppTexts.DEFAULT_PRICE_VALUE,
                BikeStatus.AVAILABLE,
                AppTexts.DEFAULT_STRING_VALUE);
    }

    /**
     * Constructs a new Bike instance with specified details.
     *
     * @param bikeId        The unique id of the bike.
     * @param bikeModel     The model of the bike.
     * @param bikeType      The type of the bike such as city,sport,cargo,mountain,E-bike.
     * @param bikePrice     The Price of the bike per day
     * @param bikeStatus    The status of the bike (e.g, Available , rented, maintenance , charging).
     * @param bikeLocation  The location where the bike is currently stationed.
     */
    public Bike(int bikeId, String bikeModel, BikeType bikeType, double bikePrice, BikeStatus bikeStatus, String bikeLocation) {
        this.bikeId = bikeId;
        this.bikeModel = new SimpleStringProperty(bikeModel);
        this.bikeType = new SimpleObjectProperty<>(bikeType);
        this.bikePrice = bikePrice;
        this.bikeStatus = new SimpleObjectProperty<>(bikeStatus);
        this.bikeLocation = new SimpleStringProperty(bikeLocation);
    }

    /*
    Getter & Setter
    -------------------------------------------------------- */

    // Id
    public int getBikeId() {
        return bikeId;
    }
    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    // Model
    public String getBikeModel() {
        return bikeModel.get();
    }
    public StringProperty bikeModelProperty() {
        return bikeModel;
    }
    public void setBikeModel(String bikeModel) {
        validateNonNull(bikeModel , "Bike Model");
        this.bikeModel.set(bikeModel);
    }

    // Type
    public BikeType getBikeType() {
        return bikeType.get();
    }
    public ObjectProperty<BikeType> bikeTypeProperty() {
        return bikeType;
    }
    public void setBikeType(BikeType bikeType) {
        validateNonNull(String.valueOf(bikeType), "Bike Type");
        this.bikeType.set(bikeType);
    }

    // Price
    public double getBikePrice() {
        return bikePrice;
    }
    public void setBikePrice(double bikePrice) {
        this.bikePrice = bikePrice;
    }

    // Status
    public BikeStatus getBikeStatus() {
        return bikeStatus.get();
    }
    public ObjectProperty<BikeStatus> bikeStatusProperty() {
        return bikeStatus;
    }
    public void setBikeStatus(BikeStatus bikeStatus) {
        validateNonNull(String.valueOf(bikeStatus), "Bike Status");
        this.bikeStatus.set(bikeStatus);
    }

    // Location
    public String getBikeLocation() {
        return bikeLocation.get();
    }
    public StringProperty bikeLocationProperty() {
        return bikeLocation;
    }
    public void setBikeLocation(String bikeLocation) {
        validateNonNull(bikeLocation , "Bike Location");
        this.bikeLocation.set(bikeLocation);
    }


    /*
    Another Methods
    -------------------------------------------------------- */

    /**
     * Validates that the provided string is not null or empty.
     *
     * @param value     The string to validate.
     * @param fieldName The name of the field for error messages.
     */
    private void validateNonNull(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + AppTexts.CANNOT_BE_EMPTY);
        }
    }

    /*
    Overrides Methods
    -------------------------------------------------------- */

    /**
     * Returns a string representation of the bike
     *
     * @return A string representation of the bike.
     */
    @Override
    public String toString() {
        return "Bike{" +
               "bikeId=" + bikeId +
               ", bikeModel=" + bikeModel +
               ", bikeType=" + bikeType +
               ", bikePrice=" + bikePrice +
               ", bikeStatus=" + bikeStatus +
               ", bikeLocation=" + bikeLocation +
               '}';
    }

}
