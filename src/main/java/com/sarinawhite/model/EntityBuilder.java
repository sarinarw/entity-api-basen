package com.sarinawhite.model;

import com.sarinawhite.api.Entity;

import java.util.*;

public abstract class EntityBuilder<B extends EntityBuilder<B, E>, E extends Entity> {

    private String id;
    private Set<EntityBase> subEntities;
    private Map<Object, Object> data;

    abstract B self();

    public EntityBuilder() {
    }

    public String getId() {
        return id;
    }

    public B setId(String id) {
        this.id = id;
        return self();
    }

    public Set<EntityBase> getSubEntities() {
        return id == null && subEntities == null ? Collections.emptySet() : subEntities;
    }

    public B addSubEntity(EntityBase entity) {
        if (entity == null) return self();
        if (getId() != null && getId().equals(entity.getID())) {
            throw new IllegalArgumentException("an entity cannot be a subentity of itself");
        }
        if (subEntities == null) {
            subEntities = new HashSet<>();
        }
        subEntities.add(entity);
        return self();
    }

    public B addAllSubEntities(Set<EntityBase> entities) {
        if (entities == null) return self();
        if (subEntities == null) {
            subEntities = new HashSet<>();
        }
        entities.forEach(this::addSubEntity);
        return self();
    }

    public Map<Object, Object> getData() {
        return id == null && data == null ? Collections.emptyMap() : data;
    }

    public B addData(Object dataKey, Object dataValue) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(dataKey, dataValue);
        return self();
    }

    public B addAllData(Map<Object, Object> newData) {
        if (newData != null) {
            if (data == null) {
                data = new HashMap<>();
            }
            this.data.putAll(newData);
        }
        return self();
    }


    public abstract E build();

}
