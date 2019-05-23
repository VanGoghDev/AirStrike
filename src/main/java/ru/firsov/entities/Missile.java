package ru.firsov.entities;

import ru.firsov.models.MissileModel;

public class Missile extends DynamicEntity implements MissileModel {
    private double[] targetsX;
    private boolean isDestroyed;

    public Missile(double[] initialState, double mLa, double deltaM, double kT) {
        super(initialState, mLa, deltaM, kT);
    }

    @Override
    public double[] getRight(double[] x, double t) {
        double[] r = new double[] {this.targetsX[0] - x[0], this.targetsX[1] - x[1], this.targetsX[2] - x[2]};
        double rLen = Math.sqrt(r[0]*r[0] + r[1]*r[1] + r[2]*r[2]);
        double[] iR = new double[] {r[0]/rLen, r[1]/rLen, r[2]/rLen};
        double v = Math.sqrt(x[3] * x[3] + x[4]*x[4] + x[5]*x[5]);
        x[3] = iR[0]*v;
        x[4] = iR[1]*v;
        x[5] = iR[2]*v;
        double[] xDot = super.getRight(x, t);
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

    public void setDestroyed(double[] x) {
        if (!isDestroyed()) {
            double thisX = 0;
            for (int i = 0; i < 3; i++) {
                thisX += (this.targetsX[i] - x[i]) * (this.targetsX[i] - x[i]);
            }
            thisX = Math.sqrt(thisX);
            isDestroyed = thisX <= 100;
        }
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
