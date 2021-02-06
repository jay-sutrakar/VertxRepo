package com.example.Demo;

import java.util.concurrent.*;

class GetInteger implements Callable<Integer> {
    private Integer num;
    private Integer sleepTime;
    GetInteger(Integer num,Integer sleepTime) {
        this.num = num;
        this.sleepTime = sleepTime;
    }
    @Override
    public Integer call() throws Exception {
        Thread.sleep(sleepTime);
//        Thread.currentThread().setDaemon(true);
        System.out.println(Thread.currentThread().getName());
        return num * num;
    }
}
class SquareCalculator {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Future<Integer> calculate(Integer integer,Integer sleepTime) {
        GetInteger getInteger = new GetInteger(integer,sleepTime);
        return executorService.submit(getInteger);
    }
    public void setex() {
        executorService.shutdown();
    }



}

public class Demo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                    return "Result of asynchronous computation";
                });
        completableFuture.thenAccept(str -> {
            System.out.println(str);
        });
        completableFuture.thenAcceptAsync(
                res ->{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Inside ThenApplyAsyn :" + Thread.currentThread().getName());
                }
        );
        System.out.println(Thread.activeCount());

        Thread.sleep(10000);
    }
}

