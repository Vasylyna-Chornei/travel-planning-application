module com.vasylyna.travelplanningapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.datatransfer;

    opens com.vasylyna.travelplanningapplication to javafx.fxml;
    exports com.vasylyna.travelplanningapplication;
    exports com.vasylyna.travelplanningapplication.controllers;
    opens com.vasylyna.travelplanningapplication.controllers to javafx.fxml;
}