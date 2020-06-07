package com.sarinawhite;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sarinawhite.model.PersonEntityBuilder;
import com.sarinawhite.storage.MockTempEntityDatabase;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.time.LocalDate;

public class DatabaseModule extends AbstractModule {

    public DatabaseModule() {
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public MockTempEntityDatabase getMockTempEntityDatabase() {
        MockTempEntityDatabase db = new MockTempEntityDatabase();
        // insert test data
        db.upsert(
                new PersonEntityBuilder()
                        .setFirstName("Bob")
                        .setBirthDate(LocalDate.of(2000, 1, 1))
                        .addData("RandomOtherData", 1000)
                        .addSubEntity(
                                new PersonEntityBuilder()
                                        .setFirstName("Bobby")
                                        .setBirthDate(LocalDate.of(2010, 1, 1))
                                        .build()
                        ).build()
        );
        return db;
    }

}
