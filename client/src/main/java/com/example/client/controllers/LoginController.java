package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.services.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    public TextField userNameText;
    public TextField passwdText;
    public Button logInBtn;

    private Service service;


    public void initialize(Service service) {
        this.service = service;
    }

    public void loginPressed(ActionEvent event) throws Exception {
        String userName = userNameText.getText();
        String passwd = passwdText.getText();
        if (service.checkUser(userName, passwd)) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/main-view.fxml"));
            AnchorPane root = loader.load(); ///aici e eroarea
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            MainController controller = loader.getController();
            stage.setTitle("Main view");
            stage.setResizable(false);
            controller.setService(service);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            userNameText.setText("");
            passwdText.setText("");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorect");
            alert.setHeaderText("Ups..");
            alert.setContentText("Nume de utilizator sau parola invalide :(");
            alert.show();
        }
    }

}
