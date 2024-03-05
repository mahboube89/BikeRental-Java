package org.mr.abschlussprojekt.bikeRental.model;

import javafx.beans.property.*;
import org.mr.abschlussprojekt.bikeRental.setting.AppTexts;

import java.time.LocalDate;

/**
 * Represents a bike rental record, including details about the rental,
 * the bike, the user, and the rental period.
 */
public class BikeRental {

    /*
    Attributes
    -------------------------------------------------------- */
    private int rentalId;
    private int bikeId;
    private String bikeModel;
    private StringProperty user;
    private String startStation;
    private LocalDate startDate;
    private LocalDate returnDate;
    private double rentPrice;
    private boolean isReturned;

     /*
    Constructors
    --------------------------------------------------------------------------- */

    /**
     * Default constructor initializing with default values.
     */
    public BikeRental() {
        this(AppTexts.DEFAULT_NUMBER_VALUE,
                AppTexts.DEFAULT_NUMBER_VALUE,
                AppTexts.DEFAULT_STRING_VALUE,
                AppTexts.DEFAULT_STRING_VALUE,
                LocalDate.now(),
                LocalDate.now(),
                AppTexts.DEFAULT_PRICE_VALUE,
                false);
    }


    /**
     * Constructs a new BikeRental with specified details.
     *
     * @param rentalId     The ID of the rental.
     * @param bikeId       The ID of the bike being rented.
     * @param bikeModel    The model of the bike.
     * @param startStation The station where the bike was picked up.
     * @param startDate    The start date of the rental period.
     * @param returnDate   The expected return date.
     * @param rentPrice    The price of the rental.
     * @param isReturned   Whether the bike has been returned.
     */
    public BikeRental(int rentalId, int bikeId, String bikeModel, String startStation, LocalDate startDate, LocalDate returnDate, double rentPrice, boolean isReturned) {
        setRentalId(rentalId);
        setBikeId(bikeId);
        setBikeModel(bikeModel);
        setStartStation(startStation);
        setStartDate(startDate);
        setReturnDate(returnDate);
        setRentPrice(rentPrice);
        setReturned(isReturned);

    }

    /*
   Getter & Setter
   -------------------------------------------------------- */
    // Rental Id
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    // ID of the bike being rented
    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    // Bike model and validates it is not null or empty.
    public String getBikeModel() {
        return bikeModel;
    }

    public void setBikeModel(String bikeModel) {
        validateNonNull(bikeModel, "Bike Model");
        this.bikeModel = bikeModel;
    }

    // User
    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }


    // Pickup station and validates it is not null or empty.
    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        validateNonNull(startStation, "Pickup Station");
        this.startStation = startStation;
    }

    // Start pickup date and validates the dates.
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // Return date and validates the dates.
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // Rent price and validates it is not negative.
    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        if (rentPrice < 0) {
            throw new IllegalArgumentException("The price cannot be negative.");
        }
        this.rentPrice = rentPrice;
    }

    // Return Status
    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
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
     * Returns a string representation of the bike Rental
     *
     * @return A string representation of the bike Rental.
     */
    @Override
    public String toString() {
        return "BikeRental{" +
               "rentalId=" + rentalId +
               ", bikeId=" + bikeId +
               ", bikeModel='" + bikeModel + '\'' +
               ", user=" + user +
               ", startStation='" + startStation + '\'' +
               ", startDate=" + startDate +
               ", returnDate=" + returnDate +
               ", rentPrice=" + rentPrice +
               ", isReturned=" + isReturned +
               '}';
    }
}
