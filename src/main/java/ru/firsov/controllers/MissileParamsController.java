package ru.firsov.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.ArrayUtils;
import ru.firsov.entities.Aircraft;
import ru.firsov.entities.Missile;

import java.net.URL;
import java.util.ResourceBundle;

public class MissileParamsController extends AirParamsController{

    private Missile missile;
    private MainWindowController parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuelSlider.setMin(50);
        fuelSlider.setMax(100);
    }

    @Override
    public void setParent(MainWindowController parent) {
        this.parent = parent;
    }

    @Override
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
        missile = new Missile(initialValue, Double.parseDouble(mLaTxtField.getText()), Double.parseDouble(deltaMTxtField.getText()), Double.parseDouble(kTTxtField.getText()));
        missile.setName(nameField.getText());
    }

    public void saveEntity() {
        setEntity();
        parent.addMissile(missile);
    }
}
