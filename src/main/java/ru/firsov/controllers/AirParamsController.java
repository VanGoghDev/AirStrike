package ru.firsov.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AirParamsController implements Initializable {

    @FXML
    TextField nameField;

    private MainWindowController parent;

    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setText() {
        parent.setText(nameField.getText());
    }

    public void getText() {
        parent.setText(nameField.getText());
    }
}
