package com.sarinawhite.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.sarinawhite.DatabaseModule;
import com.sarinawhite.model.EntityBase;
import com.sarinawhite.model.EntityBuilderHelper;
import com.sarinawhite.storage.MockTempEntityDatabase;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import javax.inject.Inject;

public class EntityVerticle extends AbstractVerticle {

    private static final String ENTITY_PATH = "/entity";
    private static final int PORT = 8080;

    @Inject
    private MockTempEntityDatabase db;

    @Override
    public void start(Promise<Void> promise) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get(ENTITY_PATH).handler(this::handleGet);
        router.post(ENTITY_PATH).handler(this::handlePost);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(PORT, result -> {
                    if (result.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(result.cause());
                    }
                });
    }

    private void handleGet(RoutingContext r) {
        try {
            String maybeId = r.queryParam("id").stream().findFirst().orElse(null);
            if (maybeId == null) {
                r.response().end(toJsonString(new GetAllEntitiesResponse(db.getAllEntities())));
            } else {
                r.response().end(toJsonString(db.get(maybeId)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.response().setStatusCode(500).end(failureMessage("internal server error"));
        }
    }

    private void handlePost(RoutingContext r) {
        try {
            PostEntityRequest parsedBody;
            try {
                String bodyStr = r.getBodyAsString();
                parsedBody = bodyStr == null ? null : new ObjectMapper().readValue(bodyStr, PostEntityRequest.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                r.response().setStatusCode(400).end(failureMessage("bad request: invalid JSON"));
                return;
            }
            EntityBase entityToInsertOrUpdate = EntityBuilderHelper.buildEntityFrom(parsedBody, db);
            if (parsedBody != null) {
                db.upsert(entityToInsertOrUpdate);
            } else {
                r.response().setStatusCode(400).end(failureMessage("bad request: no data"));
                return;
            }
            r.response().end(successMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            r.response().setStatusCode(400).end(failureMessage("bad request: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            r.response().setStatusCode(500).end(failureMessage("internal server error"));
        }
    }

    private String failureMessage(String msg) {
        return String.format(
                "{\"success\":\"false\",\"message\":\"%s\"}",
                Strings.nullToEmpty(msg).replace('"', '\'')
        );
    }

    private String successMessage() {
        return "{\"success\":\"true\"}";
    }

    private String toJsonString(Object obj) {
        if (obj == null) return "{}";
        return JsonObject.mapFrom(obj).toString();
    }
}