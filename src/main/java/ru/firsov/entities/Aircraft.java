package ru.firsov.entities;

public class Aircraft extends DynamicEntity{

    public Aircraft(double[] initialState, double mLa, double deltaM, double kT) {
        super(initialState, mLa, deltaM, kT);
    }
}
