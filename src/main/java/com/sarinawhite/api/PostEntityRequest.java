package com.sarinawhite.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sarinawhite.api.EntityType;

import java.util.Map;
import java.util.Set;

public class PostEntityRequest {
    private EntityType entityType;
    private String entityId;
    private Set<String> subEntityIds;
    private Map<Object, Object> data;

    public PostEntityRequest(EntityType entityType, String entityId, Set<String> subEntityIds, Map<Object, Object> data) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.subEntityIds = subEntityIds;
        this.data = data;
    }

    @JsonCreator
    private PostEntityRequest() {

    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public Set<String> getSubEntityIds() {
        return subEntityIds;
    }

    public Map<Object, Object> getData() {
        return data;
    }
}
