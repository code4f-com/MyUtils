/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author tuanp
 */
public class Convert {

    /**
     * Primitive Data Types
     * <p>
     * byte	1 byte	Stores whole numbers from -128 to 127
     * <p>
     * short	2 bytes	Stores whole numbers from -32,768 to 32,767
     * <p>
     * int	4 bytes Stores whole numbers from -2,147,483,648 to 2,147,483,647
     * <p>
     * long	8 bytes Stores whole numbers from -9,223,372,036,854,775,808 to
     * 9,223,372,036,854,775,807
     * <p>
     * float	4 bytes Stores fractional numbers. Sufficient for storing 6 to 7
     * decimal digits
     * <p>
     * double	8 bytes Stores fractional numbers. Sufficient for storing 15
     * decimal digits
     * <p>
     * Boolean	1 bit	Stores true or false values
     * <p>
     * char	2 bytes Stores a single character/letter or ASCII values
     *
     */
    //======//
    public static int unsignedByte(byte value) {
        if (value < 0) {
            return (value + 256);
        } else {
            return value;
        }
    }

    /**
     * convert a single char to corresponding nibble. (4bit = 1/2 byte)
     *
     * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
     * signs.
     *
     * @return corresponding integer
     */
    public static int charToNibble(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }

    public static String byte2Base64(byte[] data) {
        try {
            return Base64.encodeBase64String(data);
        } catch (Exception e) {
            return "";
        }
    }

    public static String base64Decode(String input) {
        try {
            byte[] data = Base64.decodeBase64(input);
            return new String(data);
        } catch (Exception e) {
            return "";
        }
    }

    public static String encodeb64(String str) {
        return encodeb64(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeb64(byte[] bytes) {
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String decodeb64(String encodedString) {
        return new String(java.util.Base64.getUrlDecoder().decode(encodedString));
    }

    /**
     * short	2 bytes	Stores whole numbers from -32,768 to 32,767
     *
     * @param value
     * @return
     */
    public static byte[] shortToByte(short value) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /*
     * Convert a byte array to a short.  Short.MIN_VALUE is returned
     * if the startIndex is greater or equal to the endIndex, or if the
     * resultant unsigned integer is too large to store in a short.
     */
    public static short byteToShort(byte[] inBytes) {
        return byteToShort(inBytes, 0, inBytes.length);
    }

    public static short byteToShort(byte[] inBytes, int startIndex, int endIndex) {
        short outputShort;
        String hexString = HexUtil.byteToHex(inBytes, startIndex, endIndex);
        try {
            outputShort = Short.parseShort(hexString, 16);
        } catch (NumberFormatException ex) {
            outputShort = Short.MIN_VALUE;
        }
        return outputShort;
    }

    /**
     * int	4 bytes Stores whole numbers from -2,147,483,648 to 2,147,483,647
     *
     * @param value
     * @return
     */
    public static byte[] intToByte(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /*
     * Convert a byte array to an int.  Integer.MIN_VALUE is returned
     * if the startIndex is greater or equal to the endIndex, or if the
     * resultant unsigned integer is too large to store in an int.
     */
    public static int byteToInt(byte[] inBytes) {
        return byteToInt(inBytes, 0, inBytes.length);
    }

    public static int byteToInt(byte[] inBytes, int startIndex, int endIndex) {
        int outputInt;
        String hexString = HexUtil.byteToHex(inBytes, startIndex, endIndex);
        try {
            outputInt = Integer.parseInt(hexString, 16);
        } catch (NumberFormatException ex) {
            outputInt = Integer.MIN_VALUE;
        }
        return outputInt;
    }

    /**
     * returns a byte array of length 4 Array is reverse of result by method
     * intToByteArray in class com.tuanpla.utils.common.Convert.java
     *
     * @param i
     * @return
     */
    public static byte[] intToDWord(int i) {
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
    public static byte[] longToByte(long value) {
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

    /*
     * Convert a byte array to a long.  Long.MIN_VALUE is returned
     * if the startIndex is greater or equal to the endIndex, or if the
     * resultant unsigned integer is too large to store in a long.
     */
    public static long byteToLong(byte[] inBytes) {
        return byteToLong(inBytes, 0, inBytes.length);
    }

    public static long byteToLong(byte[] inBytes, int offset, int length) {
        long outputLong;
        String hexString = HexUtil.byteToHex(inBytes, offset, length);
        try {
            outputLong = Long.parseLong(hexString, 16);
        } catch (Exception ex) {
            outputLong = Long.MIN_VALUE;
        }
        return outputLong;
    }

    /**
     * convert a byte sequence into a number
     *
     * @param array byte[]
     * @param offset int: start position in array
     * @param length int: number of bytes to convert
     * @return
     */
    public static long byteArrayToLong(byte[] array, int offset, int length) {
        long rv = 0;
        for (int x = 0; x < length; x++) {
            long bv = array[offset + x];
            if (x > 0 & bv < 0) {
                bv += 256;
            }
            rv *= 256;
            rv += bv;
        }

        return rv;
    }

    /**
     * char	2 bytes Stores a single character/letter or ASCII values
     *
     * @param c
     * @return
     */
    public static byte[] charToByte(char c) {
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
