package com.sarinawhite.storage;

import com.google.common.collect.Sets;
import com.sarinawhite.api.Entity;
import com.sarinawhite.model.EntityBase;
import io.vertx.core.impl.ConcurrentHashSet;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/*
  Class to temporarily store data for each run cycle of the service
  Much simpler to do this than setup caching or database as that was not a
  mentioned requirement and would result in additional setup/development time overhead
 */
public class MockTempEntityDatabase {

    private final Map<String, EntityBase> idToEntity;
    private final Set<String> usedIds;

    public MockTempEntityDatabase() {
        idToEntity = new ConcurrentHashMap<>();
        usedIds = new ConcurrentHashSet<>();
    }

    public EntityBase upsert(EntityBase e) {
        if (e == null) return null;
        verifyNoCircularRelationship(e, new HashSet<>());
        if (e.getID() == null) {
            return insert(e);
        }
        return update(e);
    }

    private void verifyNoCircularRelationship(EntityBase e, Set<String> touchedIds) {
        Set<String> subEntityIds = e.getSubEntityBasesNotNull().stream().map(EntityBase::getID).collect(Collectors.toSet());
        Set<String> thisAndSubEntityIdsNonnull = subEntityIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> touchedIdsNonnull = touchedIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (!Sets.intersection(thisAndSubEntityIdsNonnull, touchedIdsNonnull).isEmpty()) {
            throw new IllegalArgumentException("circular entity relationships are not allowed");
        }
        subEntityIds.add(e.getID());
        subEntityIds.addAll(touchedIds);
        subEntityIds= subEntityIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> updatedTouchedIds = new HashSet<>(subEntityIds);
        e.getSubEntityBasesNotNull().forEach(subBases -> verifyNoCircularRelationship(subBases, updatedTouchedIds));
    }

    private EntityBase update(EntityBase e) {
        if (e == null) return null;
        final String entityId = e.getID();
        if (!idToEntity.containsKey(e.getID())) {
            throw new IllegalArgumentException("Entity cannot be updated as it does not exist. id=" + entityId);
        }
        EntityBase prevEntity = idToEntity.getOrDefault(e.getID(), null);
        EntityBase newEntity;
        if (prevEntity != null) {
            prevEntity.updateWith(e);
            newEntity = prevEntity;
        } else {
            newEntity = e;
        }
        idToEntity.put(entityId, newEntity);
        e.getSubEntityBasesNotNull().forEach(this::upsert);
        return prevEntity;
    }

    private EntityBase insert(EntityBase e) {
        String id = generateUniqueID();
        e.setId(id);
        idToEntity.put(id, e);
        e.getSubEntityBasesNotNull().forEach(this::upsert);
        return idToEntity.get(id);
    }

    public EntityBase get(String id) {
        EntityBase e = idToEntity.getOrDefault(id, null);
        refreshSubEntities(e);
        return e;
    }

    private void refreshSubEntities(EntityBase e) {
        if (e == null) return;
        Set<EntityBase> sb = e.getSubEntityBasesNotNull().stream().map(se -> this.get(se.getID()))
                .filter(Objects::nonNull).collect(Collectors.toSet());
        e.setSubEntityBases(sb);
    }

    public Set<Entity> getAllEntities() {
        return new HashSet<>(idToEntity.values());
    }

    private String generateUniqueID() {
        Set<String> currentIDs = idToEntity.keySet();
        String maybeUniqueID;
        int count = 0;
        do {
            maybeUniqueID = RandomStringUtils.random(20, true, true);
            count++;
            if (count > 10000) {
                throw new RuntimeException("Failed to find unique ID after 10000 tries - please update the ID generation logic");
            }
        } while (currentIDs.contains(maybeUniqueID));
        usedIds.add(maybeUniqueID); // make sure this ID is not generated again
        return maybeUniqueID;
    }
}
