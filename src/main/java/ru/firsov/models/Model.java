package ru.firsov.models;

public interface Model {
    double[] getRight(double[] x, double t);
    int getDimension();
    double[] getInitialState();
    double[] setSceneInitialState();
    double[] getNewSceneInitialState(double[] x);
}
