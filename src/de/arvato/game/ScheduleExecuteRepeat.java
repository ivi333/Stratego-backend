package de.arvato.game;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleExecuteRepeat {

	public static void main (String args []) throws Exception{
		ScheduledExecutorService scheduledExecutorService =
				Executors.newScheduledThreadPool(20);
		ScheduledFuture schedule = scheduledExecutorService.schedule(new Runnable () {
			@Override
			public void run() {
				System.out.println("Executed!:" + Thread.currentThread().getName());

			}
		}, 2, TimeUnit.SECONDS);
		schedule.get();
		ScheduledFuture schedule2 = scheduledExecutorService.schedule(new Runnable () {
			@Override
			public void run() {
				System.out.println("Executed 2!:" + Thread.currentThread().getName());

			}
		}, 2, TimeUnit.SECONDS);
		schedule2.get();
		scheduledExecutorService.shutdown();
		System.out.println("finished!");
	}
}