package ru.firsov.models;

import ru.firsov.entities.DynamicEntity;

import java.util.List;

public interface Model {
    double[] getRight(double[] x, double t);
    int getDimension();
    double[] getInitialState();
    double[] setSceneInitialState();
    double[] getNewSceneInitialState(double[] x);
    void addEntity(DynamicEntity entity);
    List<DynamicEntity> getEntities();
}
