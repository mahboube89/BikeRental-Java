module org.rentandroll.mrabschlussprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mariadb.jdbc;

    opens org.mr.abschlussprojekt.bikeRental.model to javafx.base;
    opens org.mr.abschlussprojekt.bikeRental to javafx.fxml;
    exports org.mr.abschlussprojekt.bikeRental;
    exports org.mr.abschlussprojekt.bikeRental.gui;
    opens org.mr.abschlussprojekt.bikeRental.gui to javafx.fxml;
}