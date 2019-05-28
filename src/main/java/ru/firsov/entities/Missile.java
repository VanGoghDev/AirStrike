package ru.firsov.entities;

import ru.firsov.models.MissileModel;

public class Missile extends DynamicEntity implements MissileModel {
    private double[] targetsX;
    private boolean isDestroyed = false;
    double distanceToTarget;

    public Missile(double[] initialState, double mLa, double deltaM, double kT) {
        super(initialState, mLa, deltaM, kT);
    }

    @Override
    public double[] getRight(double[] x, double t) {
        double[] r = new double[] {this.targetsX[0] - x[0], this.targetsX[1] - x[1], this.targetsX[2] - x[2]};
        double rLen = Math.sqrt(r[0]*r[0] + r[1]*r[1] + r[2]*r[2]);
        getDistance(x);
        double[] iR = new double[] {r[0]/distanceToTarget, r[1]/distanceToTarget, r[2]/distanceToTarget};  // нормированный вектор
        double v = Math.sqrt(x[3] * x[3] + x[4]*x[4] + x[5]*x[5]);
        x[3] = iR[0]*v;
        x[4] = iR[1]*v;
        x[5] = iR[2]*v;
        double[] xDot = super.getRight(x, t);
        setDestroyed();
        return  xDot;
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
    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed() {
        if (distanceToTarget <= 10) {
            isDestroyed = true;
        }
    }

    private void getDistance(double[] x) {
        for (int i = 0; i < 3; i++) {
            distanceToTarget += Math.pow((this.targetsX[i] - x[i]), 2);
        }
        distanceToTarget = Math.sqrt(distanceToTarget);
    }
    @Override
    public void setTargetsX(double[] targetsX) {
        this.targetsX = targetsX;
    }

    @Override
    public double[] getTargetsX() {
        return targetsX;
    }
}
