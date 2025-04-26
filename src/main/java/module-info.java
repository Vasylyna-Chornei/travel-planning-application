module com.vasylyna.travelplanningapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;

    opens com.vasylyna.travelplanningapplication to javafx.fxml;
    exports com.vasylyna.travelplanningapplication;
    exports com.vasylyna.travelplanningapplication.controllers;
    opens com.vasylyna.travelplanningapplication.controllers to javafx.fxml;
}