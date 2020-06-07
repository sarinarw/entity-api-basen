package com.sarinawhite.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public interface Entity {
    // Returns a unique identifier
    @JsonProperty("id")
    String getID();
    // Returns the sub-entities of this entity
    @JsonProperty("subEntities")
    Set<Entity> getSubEntities();
    // Returns a set of key-value data belonging to this entity
    @JsonProperty("data")
    Map<Object, Object> getData();
}