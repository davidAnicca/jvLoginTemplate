package com.example.client.controllers;

import com.example.client.services.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class MainController {


    public ListView<String> someList;

    private Service service;

    private ObservableList<String> someObsList;

    public void setService(Service service) throws Exception {
        this.service = service;
        init();
    }

    private void init() throws Exception {

    }

    public void startGame(ActionEvent event) {

    }
}
