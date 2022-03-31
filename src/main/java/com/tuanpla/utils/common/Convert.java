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

    /**
     * short	2 bytes	Stores whole numbers from -32,768 to 32,767
     *
     * @param value
     * @return
     */
    public static byte[] shortToByteArray(short value) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /**
     * int	4 bytes Stores whole numbers from -2,147,483,648 to 2,147,483,647
     *
     * @param value
     * @return
     */
    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /**
     * returns a byte array of length 4 Array is reverse of result by method
     * intToByteArray in class com.tuanpla.utils.common.Convert.java
     *
     * @param i
     * @return
     */
    private static byte[] intToDWord(int i) {
        byte[] dword = new byte[4];
        dword[0] = (byte) (i & 0x00FF);
        dword[1] = (byte) ((i >> 8) & 0x000000FF);
        dword[2] = (byte) ((i >> 16) & 0x000000FF);
        dword[3] = (byte) ((i >> 24) & 0x000000FF);
        return dword;
    }

    /**
     * long	8 bytes Stores whole numbers from -9,223,372,036,854,775,808 to
     * 9,223,372,036,854,775,807
     *
     * @param value
     * @return
     */
    public static byte[] longToByteArray(long value) {
        byte[] rv = new byte[8];
        for (int x = 7; x >= 0; x--) {// big endian
            //for (int x = 0; x < 8; x++){// little endian
            long temp = value & 0xFF;
            if (temp > 127) {
                temp -= 256;
            }
            rv[x] = (byte) temp;
            value >>= 8;
        }
        return rv;
    }

    /**
     * char	2 bytes Stores a single character/letter or ASCII values
     *
     * @param c
     * @return
     */
    public static byte[] charToByteArray(char c) {
        byte[] twoBytes = {(byte) (c & 0xff), (byte) (c >> 8 & 0xff)};
        return twoBytes;
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

    public static Double longToDouble(Long number) {
        return Double.longBitsToDouble(number);
    }

    public static String decimalTo26(long input) {
        String str26 = "";
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (input <= 0) {
            return "0";
        }
        int base = 26;   // flexible to change in any base under 16
        while (input > 0) {
            int digit = (int) (input % base);              // rightmost digit
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
}
