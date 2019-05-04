package ru.firsov.models;

import java.util.ArrayList;

public interface EntityModel extends Model {
    ArrayList<double[]> getBigX();
    void setName(String name);
    String getName();
    boolean isDestroyed();
    void addX(double[] x);
}
