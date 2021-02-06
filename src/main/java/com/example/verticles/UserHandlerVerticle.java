package com.example.verticles;

import com.example.models.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserHandlerVerticle extends AbstractVerticle {
    private List<User> userList = new ArrayList<>();
    String id = UUID.randomUUID().toString();
    public void start() {
        User user1 = new User("jay","jay@me.com","23");
        User user2 = new User("Shivam","shivam@friend","23");


        vertx.eventBus().consumer("get-all-user",handler -> {

            handler.reply(Thread.currentThread().getName() + ":::::" + id);
        });
    }
}
