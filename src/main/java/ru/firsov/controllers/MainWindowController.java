package ru.firsov.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import ru.firsov.entities.Aircraft;
import ru.firsov.entities.DynamicEntity;
import ru.firsov.entities.Missile;
import ru.firsov.integrators.EulerIntegrator;
import ru.firsov.scene.MScene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML private JFXButton addAircraftBtn;
    @FXML private JFXButton addMissileBtn;
    @FXML private JFXButton startBtn;
    @FXML private JFXButton stopBtn;
    @FXML private ScatterChart scPlot;
    @FXML NumberAxis xAxis;
    @FXML NumberAxis yAxis;

    private List<DynamicEntity> entities = new ArrayList<DynamicEntity>();
    private FirstOrderIntegrator dp54 = new DormandPrince54Integrator(
            1.0e-8, 100.0, 1.0-10, 1.0e-10
    );
    private MScene scene;
    private EulerIntegrator euler;
    XYChart.Series series = new XYChart.Series();
    List<XYChart.Series> seriesList = new ArrayList<>();
    private boolean addSeries = false;
    private boolean started = false;  // Если начали интегрирование, то теперь добавляем объекты напрямую в интегратор
    private boolean addNewEntity = false;  // Если это добавление происходит после того как начали строить график, то теперь в другом потоке добавляем рисунок на график
    DynamicEntity entity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Drawer.fxml"));
            drawer.setSidePane(box);

            for (Node node : box.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        if (node.getAccessibleText().equals("btnAirParams")) {
                            loadAirParams();
                        } else if (node.getAccessibleText().equals("btnMissileParam")) {
                            loadMissileParams();
                        }
                    });
                }
            }

            HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask2.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                burgerTask2.setRate(burgerTask2.getRate() * -1);
                burgerTask2.play();
                if (drawer.isOpened()) {
                    drawer.close();
                } else {
                    drawer.open();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAirParams() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AirParams.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();

            AirParamsController airParamsController = fxmlLoader.getController();
            airParamsController.setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMissileParams() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MissileParams.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();

            MissileParamsController missileParamsController = fxmlLoader.getController();
            missileParamsController.setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAircraft(ActionEvent actionEvent) {
        if (!started) {
            entities.add(new Aircraft(new double[]{100, 100, 0, 100, 0, 0, 150}, 200, 0.1, 100));
        } else {
            euler.addEntity(new Aircraft(new double[]{100, 100, 0, 100, 0, 0, 150}, 200, 0.1, 100));
            addSeries = true;
            buildPlot();
        }
    }

    public void addAircraft(Aircraft aircraft) {
        entities.add(aircraft);
    }

    public void addMissile(ActionEvent actionEvent) {
        if (!started) {
            entities.add(new Missile(new double[]{0, 0, 0, 200, 0, 0, 50}, 50, 0.07, 400));
        } else {
            euler.addEntity(new Missile(new double[]{0, 0, 0, 200, 0, 0, 50}, 50, 0.07, 400));
            addSeries = true;
            buildPlot();
        }
    }

    public void addMissile(Missile missile) {
        entities.add(missile);
    }

    public void start(ActionEvent actionEvent) {
        started = true;
        addNewEntity = true;
        scene = new MScene(entities.toArray(new DynamicEntity[entities.size()]));
        double[] y = scene.setSceneInitialState();  // переделать 0 (добавить на форму возможность выбирать время интегрирования как вариант)
        euler = new EulerIntegrator(scene);
        euler.setDaemon(true);
        euler.start();
        buildPlot();
        for (double v : y) {
            System.out.println(v);
        }
    }

    public void buildPlot() {

        scPlot.getXAxis().setTickLabelsVisible(true);
        int xAxisIndex = 0;
        int yAxisIndex = 1;
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        int entitiesSize = euler.getModel().getEntities().size();
        int bigXSize = euler.getModel().getEntities().get(0).getBigX().size();

        for (int i = 0; i < euler.getModel().getEntities().size(); i++) {
            seriesList.add(new XYChart.Series());
            scPlot.getData().add(seriesList.get(i));
        }

        Thread updateThread = new Thread(() -> {
            while (euler.isAlive()) {
                try {
                    for (int i = 0; i < euler.getModel().getEntities().size(); i++) {
                        if (!addSeries) {
                            int finalI = i;
                            Platform.runLater(() -> seriesList.get(finalI).getData().add(new XYChart.Data(euler.getModel().getEntities().get(finalI).getBigX().get(euler.getModel().getEntities().get(finalI).getBigX().size() - 1)[xAxisIndex], euler.getModel().getEntities().get(finalI).getBigX().get(euler.getModel().getEntities().get(finalI).getBigX().size() - 1)[yAxisIndex])));
                        } else {
                            seriesList.add(new XYChart.Series());
                            scPlot.getData().add(seriesList.get(i));
                            addSeries = false;
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public DynamicEntity getEntity() {
        return entity;
    }

    public void setEntity(DynamicEntity entity) {
        this.entity = entity;
    }

    public void stop(ActionEvent actionEvent) {
        buildPlot();
    }
}
