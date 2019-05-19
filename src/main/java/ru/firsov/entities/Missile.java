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

    /*@Override
    public double[] getRight(double[] x, double t) {
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

        RealMatrix a1 = new Array2DRowRealMatrix(
                new double[][] {
                        {Math.cos(epsilon)*Math.cos(psi), -Math.sin(epsilon)*Math.cos(psi), -Math.sin(psi)},
                        {Math.sin(epsilon), Math.cos(epsilon), 0},
                        {-Math.cos(epsilon)*Math.sin(psi), Math.sin(epsilon)*Math.sin(psi), Math.cos(psi)}
                },
                false
        );

        RealMatrix v = new Array2DRowRealMatrix(
                new double[][] {
                        {x[3]},
                        {x[4]},
                        {x[5]}
                },
                false
        );
        RealMatrix result = a.multiply(v);
        x[3] = result.getEntry(0, 0);
        x[4] = result.getEntry(1, 0);
        x[5] = result.getEntry(2, 0);
        double[] xDot =  super.getRight(x, t);
        return xDot;
    }*/

    /*@Override
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

        RealMatrix a1 = new Array2DRowRealMatrix(
                new double[][] {
                        {Math.cos(epsilon)*Math.cos(psi), -Math.sin(epsilon)*Math.cos(psi), -Math.sin(psi)},
                        {Math.sin(epsilon), Math.cos(epsilon), 0},
                        {-Math.cos(epsilon)*Math.sin(psi), Math.sin(epsilon)*Math.sin(psi), Math.cos(psi)}
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
    }*/

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

        /*double deltaM;
        double mLa = 50;
        double kT = 200;
        if (x[6] <= 0.5)
            deltaM = 0;
        else
            deltaM = 0.1;
        xDot[0] = x[3];
        xDot[1] = x[4];
        xDot[2] = x[5];
        xDot[3] = kT * (deltaM) / (mLa + x[6]);
        xDot[4] = kT * (deltaM) / (mLa + x[6]);
        xDot[5] = kT * (deltaM) / (mLa + x[6]);
        xDot[6] = -deltaM;*/
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
