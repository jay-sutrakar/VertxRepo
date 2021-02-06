package com.example.verticles;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() {
        ConfigStoreOptions fileStore = new ConfigStoreOptions();
        fileStore.setType("file")
                .setOptional(true)
                .setConfig(new JsonObject().put("path","application-config.json"));
        ConfigStoreOptions systemStore = new ConfigStoreOptions().setType("sys");
        ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore).addStore(systemStore);


//        DeploymentOptions opts = new DeploymentOptions();
//        opts.setInstances(3);
//        opts.setWorker(true);

        ConfigRetriever retriever = ConfigRetriever.create(vertx,options);
        vertx.deployVerticle(UserHandlerVerticle.class.getName());
        Router router = Router.router(vertx);
//        router.route().handler(this::AllRequestHandler);
        router.get("/users").handler(this::getRequestHandler);
        int httpPort;
        try {
            httpPort = Integer.parseInt(System.getProperty("http.port","8081"));
        }catch (NumberFormatException nfe) {
            httpPort = 8081;
        }
        int finalHttpPort = httpPort;
        retriever.getConfig(json -> {
            JsonObject jsonObject = json.result();
            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(finalHttpPort, httpServerAsyncResult -> {
                        System.out.println("Server is listening on port : 8081 ");
                    });
        });

    }

    private void getRequestHandler(RoutingContext routingContext) {
        vertx.eventBus().request("get-all-user","",reply -> {
            routingContext.response().end(reply.result().body().toString());
        });
    }

    private void AllRequestHandler(RoutingContext rc) {
        HttpServerResponse response = rc.response();
        response.end("Hello World");

    }
}
