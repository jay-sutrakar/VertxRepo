package com.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class SenderVerticle extends AbstractVerticle {
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type","text/html")
                    .end("<h1>Hello From non-clustered messenger Example</h1>");

        });
        router.post("/send/:message").handler(this::sendMessage);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.server.port",8081),httpServerAsyncResult -> {
                    if(httpServerAsyncResult.succeeded()){
                        System.out.println("http server is running on port : " + 8081);
                        future.complete();
                    } else {
                        System.out.println("Could not Start Server " + httpServerAsyncResult.cause());
                        future.fail(future.cause());
                    }
                });

    }
    private void sendMessage(RoutingContext routingContext) {
        final EventBus eventBus = vertx.eventBus();
        final String message = routingContext.request().getParam("message");
        eventBus.send("amex.message",message,reply -> {
            if(reply.succeeded()){
                System.out.println("Message Recieved : " + reply.result().body());
            }else {
                System.out.println("No Reply");
            }
        });
    }
}
