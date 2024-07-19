package com.example.Additions;

import java.util.function.BooleanSupplier;

public class SleepUtils {
    public static void sleepUntilTrue(BooleanSupplier condition, int interval, int timeout) {
        long startTime = System.currentTimeMillis();
        while (!condition.getAsBoolean() && (System.currentTimeMillis() - startTime) < timeout) {
            sleep(interval, interval); // Provide both min and max as the same interval
        }
    }

    public static void sleep(int min, int max) {
        try {
            Thread.sleep(min + (int) (Math.random() * (max - min)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}