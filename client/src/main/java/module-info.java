module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.logging.log4j;
    requires org.json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens com.example.client to javafx.fxml;
    exports com.example.client;
    exports com.example.client.controllers;
}