package org.mdyk.netsim.view.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;


public class MessageContentController implements Initializable{

    @FXML
    private TextArea messageContent;

    private String message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        messageContent.setText(message);

    }
}
