package com.sarinawhite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sarinawhite.api.Entity;
import com.sarinawhite.api.EntityType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class EntityBase implements Entity {

    private String id;
    private Set<EntityBase> subEntityBases;
    private Map<Object, Object> data;
    private EntityType entityType;

    protected EntityBase(String id, Set<EntityBase> subEntityBases, Map<Object, Object> data, EntityType entityType) {
        this.id = id;
        this.subEntityBases = subEntityBases;
        this.data = data;
        this.entityType = entityType;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Set<Entity> getSubEntities() {
        return getSubEntityBases() == null ? null : getSubEntityBases().stream().map(e -> (Entity) e).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<EntityBase> getSubEntityBases() {
        return subEntityBases;
    }

    @JsonIgnore
    public Set<EntityBase> getSubEntityBasesNotNull() {
        return subEntityBases == null ? Collections.emptySet() : subEntityBases;
    }

    @Override
    public Map<Object, Object> getData() {
        return data;
    }

    @JsonIgnore
    public Map<Object, Object> getDataNotNull() {
        return data == null ? Collections.emptyMap() : data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubEntityBases(Set<EntityBase> subEntityBases) {
        this.subEntityBases = subEntityBases;
    }

    public void setData(Map<Object, Object> data) {
        this.data = data;
    }

    @JsonIgnore
    public EntityType getEntityType() {
        return entityType;
    }

    public void updateWith(EntityBase e) {
        if (e == null) return;
        if (e.getData() != null) {
            this.setData(e.getData());
        }
        if (e.getSubEntityBases() != null) {
            this.setSubEntityBases(e.getSubEntityBases());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityBase that = (EntityBase) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
