package com.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

public class MessengerLauncher extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
//        deployHelper(SenderVerticle.class.getName()).setHandler(voidAsyncResult -> {
//            if(voidAsyncResult.succeeded()) {
//                startFuture.complete();
//            }else {
//                startFuture.fail(voidAsyncResult.cause());
//            }
//        });
//        CompositeFuture.all(deployHelper(SenderVerticle.class.getName())
        Future<Void> nFuture = test();
        if(nFuture.succeeded()){
            startFuture.complete();
        }else {
            startFuture.failed();
        }
        for (int i = 0;i<5;i++){
            System.out.println(Thread.currentThread().getName());
        }
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
    private Future<Void> test() {
        Future<Void> future = Future.future();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        future.complete();
        return future;
    }
    private Future<Void> deployHelper(String name) {
        final Future<Void> future = Future.future();
        vertx.deployVerticle(name, res -> {
            if(res.failed()) {
                System.out.println("Failed to Deplay Vertcle : " + name);
                future.fail(res.cause());
            } else {
                System.out.println("Verticle Deployed : " + name);
                future.complete();
            }
        });
        return future;
    }
}
