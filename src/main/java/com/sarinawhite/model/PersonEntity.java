package com.sarinawhite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sarinawhite.api.EntityType;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class PersonEntity extends EntityBase {

    PersonEntity(String id, Set<EntityBase> subEntities, Map<Object, Object> data) {
        super(id, subEntities, data, EntityType.PERSON);
    }

    @JsonIgnore
    public String getFirstName() {
        return (String) getDataNotNull().get(PersonTrait.FIRST_NAME.toString());
    }

    @JsonIgnore
    public LocalDate getBirthDate() {
        return (LocalDate) getDataNotNull().get(PersonTrait.BIRTHDATE.toString());
    }
}
