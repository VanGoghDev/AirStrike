package ru.firsov.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.ArrayUtils;
import ru.firsov.entities.Aircraft;
import ru.firsov.entities.DynamicEntity;

import java.net.URL;
import java.util.ResourceBundle;

public class AirParamsController implements Initializable {

    @FXML TextField nameField;
    @FXML TextField paramTxtField;
    @FXML Slider fuelSlider;
    @FXML TextField mLaTxtField;
    @FXML TextField deltaMTxtField;
    @FXML TextField kTTxtField;

    private MainWindowController parent;
    private Aircraft aircraft;

    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuelSlider.setMin(50);
        fuelSlider.setMax(200);
    }

    public void setEntity() {
        String param = paramTxtField.getText();
        String[] params = param.split(",");
        if (params.length != 6) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not enough arguments");
            alert.setHeaderText("Result:");
            alert.setContentText("Not enough arguments in parameters field. Only 6 params allowed");
            alert.showAndWait();
            return;
        }
        Double[] initialValues = new Double[params.length + 1];  // +1 because of fuel
        for (int i = 0; i < params.length; i++) {
            initialValues[i] = Double.parseDouble(params[i]);
        }
        initialValues[6] = fuelSlider.getValue();

        double[] initialValue = ArrayUtils.toPrimitive(initialValues);
        aircraft = new Aircraft(initialValue, Double.parseDouble(mLaTxtField.getText()), Double.parseDouble(deltaMTxtField.getText()), Double.parseDouble(kTTxtField.getText()));
        aircraft.setName(nameField.getText());
    }

    public void saveEntity() {
        setEntity();
        parent.addAircraft(aircraft);
        System.out.println(aircraft);
        aircraft = null;
    }
}
