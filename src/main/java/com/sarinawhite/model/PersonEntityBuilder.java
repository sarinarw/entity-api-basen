package com.sarinawhite.model;

import java.time.LocalDate;

public class PersonEntityBuilder extends EntityBuilder<PersonEntityBuilder, PersonEntity> {

    public PersonEntityBuilder setFirstName(String name) {
        addData(PersonTrait.FIRST_NAME.toString(), name);
        return this;
    }
    public PersonEntityBuilder setBirthDate(LocalDate date) {
        addData(PersonTrait.BIRTHDATE.toString(), date);
        return this;
    }

    @Override
    PersonEntityBuilder self() {
        return this;
    }

    @Override
    public PersonEntity build() {
        return new PersonEntity(getId(), getSubEntities(), getData());
    }
}
