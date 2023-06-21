package com.example.client.controllers.content;

import com.example.client.controllers.MainController;

public class UserName implements Content{
    public UserName(String text) {
        this.text = text;
    }

    private String text;

    @Override
    public void show(MainController controller) {
        controller.userLabel.setText("Utilizator: " + this.text);
    }
}
