package ru.firsov.entities;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import ru.firsov.models.MissileModel;

public class Missile extends DynamicEntity implements MissileModel {
    private double[] targetsX;
    private boolean isDestroyed;

    public Missile(double[] initialState, double mLa, double deltaM, double kT) {
        super(initialState, mLa, deltaM, kT);
    }

    @Override
    public double[] getRight(double[] x, double t) {
        double[] xDot =  super.getRight(x, t);
        double psi = Math.atan((this.targetsX[2] - x[2])/ (this.targetsX[0] - x[0]));
        double epsilon = Math.atan(Math.sqrt(Math.pow((this.targetsX[0] - x[0]), 2) + Math.pow((this.targetsX[2] - x[2]), 2)) / (this.targetsX[1] - x[1]));

        RealMatrix a = new Array2DRowRealMatrix(
                new double[][] {
                        {Math.cos(epsilon)*Math.cos(psi), -Math.sin(epsilon), Math.cos(epsilon)*Math.sin(psi)},
                        {Math.sin(epsilon)*Math.cos(psi), Math.cos(epsilon), Math.sin(epsilon)*Math.sin(psi)},
                        {Math.sin(psi), 0, Math.cos(psi)}
                },
                false
        );
        RealMatrix v = new Array2DRowRealMatrix(
                new double[][] {
                        {xDot[3]},
                        {xDot[4]},
                        {xDot[5]}
                },
                false
        );
        RealMatrix result = a.multiply(v);
        xDot[3] = result.getEntry(0, 0);
        xDot[4] = result.getEntry(1, 0);
        xDot[5] = result.getEntry(2, 0);
        return xDot;
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
