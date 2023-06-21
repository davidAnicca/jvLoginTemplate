package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.controllers.content.Content;
import com.example.client.services.Service;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


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

    public void logout(ActionEvent event) throws Exception {
        listenThread.interrupt();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/login-view.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root,root.getPrefWidth(), root.getPrefHeight());
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        LoginController controller = loader.getController();
        controller.initialize(new Service());
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
