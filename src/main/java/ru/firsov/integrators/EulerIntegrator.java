package ru.firsov.integrators;

import ru.firsov.entities.DynamicEntity;
import ru.firsov.models.Model;

public class EulerIntegrator extends Thread{
    private double h = 0.01;
    private Model model;

    public EulerIntegrator(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        try {
            double t0 = 0;
            double[] yIn = model.setSceneInitialState();
            double[] yOut = model.setSceneInitialState();
            while (true) {
                Thread.sleep(50);

                double[] f = model.getRight(yIn, t0);
                for (int j = 0; j < yIn.length; j++) {
                    f[j] = yIn[j] + f[j] * h / 2;
                }
                for (int i = 0; i < yIn.length; i++) {
                    yOut[i] = yIn[i] + h * model.getRight(f, t0)[i];
                }

                yIn = model.getNewSceneInitialState(yOut);
                /*for (double v : yOut) {
                    System.out.println(v);
                }*/
                yOut = new double[yIn.length];
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addEntity(DynamicEntity entity) {
        model.addEntity(entity);
    }

    public Model getModel() {
        return model;
    }
}
