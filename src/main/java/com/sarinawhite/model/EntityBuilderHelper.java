package com.sarinawhite.model;

import com.sarinawhite.api.EntityType;
import com.sarinawhite.api.PostEntityRequest;
import com.sarinawhite.storage.MockTempEntityDatabase;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityBuilderHelper {

    public static EntityBase buildEntityFrom(PostEntityRequest request, MockTempEntityDatabase db) {
        if (request == null) return null;
        EntityType entityType = null;
        if (request.getEntityId() != null) {
            EntityBase existingEntity = db.get(request.getEntityId());
            if (existingEntity != null) {
                entityType = existingEntity.getEntityType();
            } else {
                throw new IllegalArgumentException("entity does not exist, id=" + request.getEntityId());
            }
        } else {
            entityType = request.getEntityType();
        }
        if (entityType != EntityType.PERSON) {
            throw new IllegalArgumentException("unsupported entity type: " + request.getEntityType());
        }
        EntityBuilder<?, ? extends EntityBase> builder = new PersonEntityBuilder().setId(request.getEntityId());
        Set<EntityBase> entityBaseSet = Optional.ofNullable(request.getSubEntityIds())
                .map(ids -> ids.stream().map(db::get).collect(Collectors.toSet())).orElse(Collections.emptySet());
        if (entityBaseSet.contains(null)) {
            throw new IllegalArgumentException("one or more of the provided entity IDs does not exist=" + String.join(",", request.getSubEntityIds()));
        }
        builder.addAllSubEntities(entityBaseSet);
        builder.addAllData(request.getData());
        return builder.build();
    }
}
