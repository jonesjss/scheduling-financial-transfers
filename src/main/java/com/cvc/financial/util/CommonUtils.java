package com.cvc.financial.util;

public class CommonUtils {
    private CommonUtils() {}

    public static long getRandomValuesBetween(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }
}
