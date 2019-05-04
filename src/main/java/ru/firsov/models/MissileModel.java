package ru.firsov.models;

public interface MissileModel extends EntityModel{
    void setTargetsX(double[] targetsX);
    double[] getTargetsX();
}
