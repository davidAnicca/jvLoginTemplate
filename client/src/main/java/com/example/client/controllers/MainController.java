package com.example.client.controllers;

import com.example.client.controllers.content.Content;
import com.example.client.services.Service;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class MainController {


    public ListView<String> someList;
    public Label userLabel;

    private Thread listenThread;

    private Service service;

    private ObservableList<String> someObsList;

    public void setService(Service service) throws Exception {
        this.service = service;
        init();
    }

    private void init() throws Exception {
            listenThread = new Thread(()->{
                while (true) {
                    Content content = service.listen();
                    Platform.runLater(() -> {
                        content.show(this);
                    });

                }
            });
            listenThread.start();
    }

    public void startGame(ActionEvent event) {

    }
}
