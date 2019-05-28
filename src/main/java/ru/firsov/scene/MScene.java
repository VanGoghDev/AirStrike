package ru.firsov.scene;

import ru.firsov.entities.Aircraft;
import ru.firsov.entities.DynamicEntity;
import ru.firsov.entities.Missile;
import ru.firsov.models.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MScene implements Model {
    private List<DynamicEntity> entities = new ArrayList<DynamicEntity>();
    private double[][] initialState;
    private static int DIMENSION = 7;

    public MScene(DynamicEntity[] entity) {
        entities.addAll(Arrays.asList(entity));
        setInitialState();
    }

    @Override
    public double[] getRight(double[] x, double t) {
        double[][] xDot = new double[entities.size()][DIMENSION];
        double[] targetsX = new double[DIMENSION];
        int targetId = this.findTargetID();

        System.arraycopy(x, targetId*DIMENSION, targetsX, 0, DIMENSION);
        double[][] entitiesX = convertVectorToArray(x);

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i) instanceof Missile) {
                ((Missile) entities.get(i)).setTargetsX(targetsX);
                xDot[i] = ((Missile) entities.get(i)).getRight(entitiesX[i], t);
                continue;
            }
            xDot[i] = entities.get(i).getRight(entitiesX[i], t);
        }
        return convertArrayToVector(xDot);
    }

    @Override
    public int getDimension() {
        return entities.size() * DIMENSION;
    }

    @Override
    public double[] getInitialState() {
        return new double[0];
    }

    @Override
    public double[] setSceneInitialState() {
        return convertArrayToVector(initialState);
    }

    @Override
    public double[] getNewSceneInitialState(double[] x) {
        ArrayList<Double> xx = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isDestroyed())
                entities.remove(i);
        }
        for (int i = 0; i < entities.size(); i++) {
            double[] entityX = new double[entities.get(i).getDimension()];  // вектор состояния i-ой сущности
            if (entities.get(i).getBigX().size() == 0)  // если на первом шаге, то берем значения для вектора состояния из начальных условий
                System.arraycopy(entities.get(i).getInitialState(), 0, entityX, 0, entities.get(i).getDimension());
            else  // иначе копируем из общего вектора состояния, который пришел из интегратора
                System.arraycopy(x, i * entities.get(i).getDimension(), entityX, 0, entities.get(i).getDimension());  // из x начиная с позиции i * количество элементов в векторе состояния одной сущность
            entities.get(i).addX(entityX);
            for (int j = 0; j < 7; j++) {
                xx.add(entities.get(i).getBigX().get(entities.get(i).getBigX().size() - 1)[j]);
            }
        }
        double[] arrX = xx.stream().mapToDouble(d -> d).toArray();
        return arrX;
    }

    @Override
    public void addEntity(DynamicEntity entity) {
        entities.add(entity);
    }

    @Override
    public List<DynamicEntity> getEntities() {
        return entities;
    }

    private void setInitialState() {
        initialState = new double[entities.size()][DIMENSION];
        for (int i = 0; i < entities.size(); i++) {
            initialState[i] = entities.get(i).getInitialState();
        }
    }

    private int findTargetID() {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i) instanceof Aircraft) {
                return i;
            }
        }
        return 0;
    }

    private double[][] convertVectorToArray(double[] vector) {
        double[][] array = new double[entities.size()][DIMENSION];
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (count == vector.length) break;
                array[i][j] = vector[count];
                count++;
            }
        }
        return array;
    }

    private double[] convertArrayToVector(double[][] array) {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                list.add(array[i][j]);
            }
        }

        double[] vector = new double[list.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = list.get(i);
        }
        return vector;
    }
}
