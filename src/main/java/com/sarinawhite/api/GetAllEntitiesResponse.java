package com.sarinawhite.api;

import java.util.Set;

public class GetAllEntitiesResponse {
    private final Set<Entity> entities;

    public GetAllEntitiesResponse(Set<Entity> entities) {
        this.entities = entities;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
