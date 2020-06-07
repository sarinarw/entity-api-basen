package com.sarinawhite;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sarinawhite.api.EntityVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.concurrent.TimeUnit;

public class Launcher {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DatabaseModule());
        EntityVerticle entityVerticle = new EntityVerticle();
        injector.injectMembers(entityVerticle);
        // Blocked thread check time increased to suppress thread blocked warnings on setup due to initial start overhead
        // Note: https://github.com/eclipse-vertx/vert.x/issues/1379
        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(10);
        options.setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS);
        Vertx vertx = Vertx.vertx(options);
        injector.getInstance(Launcher.class).start(vertx, entityVerticle);
    }

    public void start(Vertx vertx, EntityVerticle ev) {
        vertx.deployVerticle(ev);
    }
}
