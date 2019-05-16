package ru.firsov.entities;

import ru.firsov.models.EntityModel;

import java.util.ArrayList;
import java.util.List;

public class DynamicEntity implements EntityModel {

    private String name;
    private double[] initialState;
    private double mLa;
    private double deltaM;
    private double kT;

    private ArrayList<double[]> bigX = new ArrayList<>();
    private boolean isDestroyed = false;
    private static final int DIMENSION = 7;

    public DynamicEntity(double[] initialState, double mLa, double deltaM, double kT) {
        this.initialState = initialState;
        this.mLa = mLa;
        this.deltaM = deltaM;
        this.kT = kT;
    }

    @Override
    public double[] getRight(double[] x, double t) {
        double[] xDot = new double[DIMENSION];

        if (x[6] <= 0.5) {
            deltaM = 0.0;
        } else {
            deltaM = 0.1;
        }

        xDot[0] = x[3];     // x = vx
        xDot[1] = x[4];     // y = vy
        xDot[2] = x[5];     // z = vz
        xDot[3] = this.kT * (this.deltaM) / (this.mLa + x[6]);
        xDot[4] = this.kT * (this.deltaM) / (this.mLa + x[6]);
        xDot[5] = this.kT * (this.deltaM) / (this.mLa + x[6]);
        xDot[6] = - this.deltaM;
        return xDot;
    }

    @Override
    public int getDimension() {
        return DIMENSION;
    }

    @Override
    public double[] getInitialState() {
        return initialState;
    }

    @Override
    public double[] setSceneInitialState() {
        return new double[0];
    }

    @Override
    public double[] getNewSceneInitialState(double[] x) {
        return new double[0];
    }

    @Override
    public void addEntity(DynamicEntity entity) {

    }

    @Override
    public List<DynamicEntity> getEntities() {
        return null;
    }

    @Override
    public ArrayList<double[]> getBigX() {
        return bigX;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void addX(double[] x) {
        bigX.add(x);
    }
}
