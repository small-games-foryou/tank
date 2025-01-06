package com.test.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class PoolUtil {
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(200);

    public static ScheduledFuture<?>  startScheduled(Runnable func, long mill) {

        return pool.scheduleAtFixedRate(func, 0, mill, TimeUnit.MILLISECONDS);
    }

    public static void submit(Runnable runnable) {
        pool.submit(runnable);
    }

    public static void delay(Runnable runnable, int time) {
        pool.schedule(runnable,time,TimeUnit.MILLISECONDS);
    }
}
