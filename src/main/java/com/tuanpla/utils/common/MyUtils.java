/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import com.tuanpla.utils.logging.LogUtils;
import com.tuanpla.utils.string.MyString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanpla
 */
public class MyUtils {

    static Logger logger = LoggerFactory.getLogger(MyUtils.class);

    // Get Random String 
//    private static final String[] ARR_STR = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
//    private static final int[] ARR_NUM = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36};
    private static final int SIZE_OF_INT_INT_HALF_BYTE = 8;
    private static final int NUMBER_OF_BITS_IN_A_HALF_BYTE = 4;
    private static final int HALF_BYTE = 0x0F;
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static int getDecimalFrom26(String hex) {
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 26 * val + d;
        }
        return val;
    }

    public static String decimalTo26(long d) {
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (d <= 0) {
            return "0";
        }
        int base = 26;   // flexible to change in any base under 16
        String hex = "";
        while (d > 0) {
            int digit = (int) d % base;              // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / base;
        }
        return hex;
    }

    public static String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(SIZE_OF_INT_INT_HALF_BYTE);
        hexBuilder.setLength(SIZE_OF_INT_INT_HALF_BYTE);
        for (int i = SIZE_OF_INT_INT_HALF_BYTE - 1; i >= 0; --i) {
            int j = dec & HALF_BYTE;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= NUMBER_OF_BITS_IN_A_HALF_BYTE;
        }
        return hexBuilder.toString();
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

    public static Double longToDouble(Long number) {
        return Double.longBitsToDouble(number);
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

    public static boolean isNull(String input) {
        return input == null || input.equalsIgnoreCase("null") || input.equalsIgnoreCase("");
    }

    public static boolean isNull(Object input) {
        return input == null;
    }

    public static String getNumber(String input) {
        if (input == null) {
            return null;
        }
        input = MyString.convert2NoSign(input);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if ((ch >= '0' && ch <= '9')) {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static boolean getBoolean(String input, boolean defaultVal) {
        try {
            return input != null && (input.equals("1") || input.equals("true"));
        } catch (Exception e) {
            return defaultVal;
        }

    }

    public static boolean getBoolean(int aInt) {
        return aInt != 0;
    }

    public static String validString(String input) {
        if (input != null) {
            input = input.trim();
        } else {
            input = "";
        }
        return input;
    }

    public static int string2Int(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int string2Int(String input, int defaultVal) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static long string2Long(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long string2Long(String input, long defaultVal) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static double string2Double(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double string2Double(String input, double defaultVal) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

}
