package ru.firsov.models;

import ru.firsov.entities.DynamicEntity;

import java.util.List;

public interface SceneModel extends Model{
    void addEntity(DynamicEntity entity);
    List<DynamicEntity> getEntities();
}
