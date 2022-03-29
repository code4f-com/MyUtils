/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import com.tuanpla.utils.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class MyUtils {

    static Logger logger = LoggerFactory.getLogger(MyUtils.class);

    public static void main(String[] args) {

    }

    public static String decimalTo26(long input) {
        String str26 = "";
        System.out.println("input =" + input);
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (input <= 0) {
            return "0";
        }
        int base = 26;   // flexible to change in any base under 16
        while (input > 0) {
            int digit = (int) (input % base);              // rightmost digit
            System.out.println("digit=" + input);
            str26 = digits.charAt(digit) + str26;  // string concatenation
            input = input / base;
        }
        return str26;
    }

    public static long getDecimalFrom26(String str26) {
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        str26 = str26.toUpperCase();
        long val = 0;
        for (int i = 0; i < str26.length(); i++) {
            char c = str26.charAt(i);
            int d = digits.indexOf(c);
            val = 26 * val + d;
        }
        return val;
    }

    /**
     * Neu thoi gian hien tai <= thoi gian timeout => false else => true
     *
     * @param lastTime
     * @param secondTimeOut
     * @return
     */
    public static boolean timeOut(long lastTime, int secondTimeOut) {
        long current = System.currentTimeMillis();
        boolean result = lastTime + (secondTimeOut * 1000) < current;
        LogUtils.debug("timeOut.result: " + result);
        return result;
    }

    public static void sleepSecond(int second) {
        long start = System.currentTimeMillis();
        while (true) {
            long runTime = System.currentTimeMillis();
            long distance = runTime - start;
            if (distance > second * 1000) {
                break;
            }
        }
    }

    public static Double longToDouble(Long number) {
        return Double.longBitsToDouble(number);
    }

    public static boolean isNull(String input) {
        return input == null || input.equalsIgnoreCase("null") || input.equalsIgnoreCase("");
    }

    public static boolean isNull(Object input) {
        return input == null || input.equals("null") || input.equals("");
    }

}
