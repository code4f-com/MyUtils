/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

/**
 *
 * @author tuanp
 */
public class Convert {

    public static String getNumber(String input) {
        if (input == null) {
            return null;
        }
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

    public static boolean getBoolean(int input) {
        return input != 0;
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
