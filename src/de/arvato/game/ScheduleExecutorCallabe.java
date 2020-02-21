package de.arvato.game;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleExecutorCallabe {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        Callable<Integer> task2 = () -> 10;
        task1();
        //run this task after 5 seconds, nonblock for task3, returns a future
        ScheduledFuture<Integer> schedule = ses.schedule(task2, 5, TimeUnit.SECONDS);
        task3();
        // block and get the result
        System.out.println(schedule.get());
        System.out.println("shutdown!");
        ses.shutdown();
    }

    public static void task1() {
        System.out.println("Running task1..." + Thread.currentThread().getName());
    }

    public static void task3() {
        System.out.println("Running task3..." + Thread.currentThread().getName());
    }
}
