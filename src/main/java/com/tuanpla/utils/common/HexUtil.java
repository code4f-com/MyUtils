/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.common;

import java.util.Arrays;

/**
 *
 * @author TUANPLA
 */
public class HexUtil {

    public static char[] HEX_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Convert the hex string to bytes.
     *
     * @param hexString is the hex string.
     * @return the bytes value that converted from the hex string.
     */
    public static byte[] hexToBytes(String hexString) {
        return hexToBytes(hexString, 0, hexString.length());
    }

    /**
     * Convert the hex string to bytes.
     *
     * @param hexString is the hex string.
     * @param offset is the offset.
     * @param endIndex is the end index.
     * @return the bytes value that converted from the hex string.
     */
    public static byte[] hexToBytes(String hexString, int offset, int endIndex) {
        byte[] data;
        String realHexString = hexString.substring(offset, endIndex).toLowerCase();
        if ((realHexString.length() % 2) == 0) {
            data = new byte[realHexString.length() / 2];
        } else {
            data = new byte[(int) Math.ceil(realHexString.length() / 2d)];
        }

        int j = 0;
        char[] tmp;
        for (int i = 0; i < realHexString.length(); i += 2) {
            try {
                tmp = realHexString.substring(i, i + 2).toCharArray();
            } catch (StringIndexOutOfBoundsException siob) {
                // it only contains one character, so add "0" string
                tmp = (realHexString.substring(i) + "0").toCharArray();
            }
            data[j] = (byte) ((Arrays.binarySearch(HEX_TABLE, tmp[0]) & 0xf) << 4);
            data[j++] |= (byte) (Arrays.binarySearch(HEX_TABLE, tmp[1]) & 0xf);
        }

        for (int i = realHexString.length(); i > 0; i -= 2) {

        }
        return data;
    }

    /**
     * Creates a 2 character hex String from a byte with the byte in a "Big
     * Endian" hexidecimal format.For example, a byte 0x34 will be returned as a
     * String in format "34". A byte of value 0 will be returned as "00".
     *
     * @param value The byte value that will be converted to a hexidecimal
     * String.
     * @return
     */
    public static String byteToHex(byte value) {
        StringBuilder buffer = new StringBuilder(2);
        appendHexString(buffer, value);
        return buffer.toString();
    }

    /**
     * Creates a String from a byte array with each byte in a "Big Endian"
     * hexidecimal format.For example, a byte 0x34 will return a String "34".A
     * byte array of { 0x34, 035 } would return "3435".
     *
     * @param bytes The byte array that will be converted to a hexidecimal
     * String. If the byte array is null, this method will append nothing (a
     * noop)
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return byteToHex(bytes, 0, bytes.length);
    }

    /**
     * Creates a String from a byte array with each byte in a "Big Endian"
     * hexidecimal format.For example, a byte 0x34 will return a String "34".A
     * byte array of { 0x34, 035 } would return "3435".
     *
     * @param bytes The byte array that will be converted to a hexidecimal
     * String. If the byte array is null, this method will append nothing (a
     * noop)
     * @param offset The offset in the byte array to start from. If the offset
     * or length combination is invalid, this method will throw an
     * IllegalArgumentException.
     * @param length The length (from the offset) to conver the bytes. If the
     * offset or length combination is invalid, this method will throw an
     * IllegalArgumentException.
     * @return
     */
    public static String byteToHex(byte[] bytes, int offset, int length) {
        if (bytes == null) {
            return "";
        }
        assertOffsetLengthValid(offset, length, bytes.length);
        // each byte is 2 chars in string
        StringBuilder buffer = new StringBuilder(length * 2);
        appendHexString(buffer, bytes, offset, length);
        return buffer.toString();
    }

    /**
     * Creates a 4 character hex String from a short with the short in a "Big
     * Endian" hexidecimal format.For example, a short 0x1234 will be returned
     * as a String in format "1234". A short of value 0 will be returned as
     * "0000".
     *
     * @param value The short value that will be converted to a hexidecimal
     * String.
     * @return
     */
    public static String shortToHex(short value) {
        StringBuilder buffer = new StringBuilder(4);
        appendHexString(buffer, value);
        return buffer.toString();
    }

    /**
     * Creates an 8 character hex String from an int twith the int in a "Big
     * Endian" hexidecimal format.For example, an int 0xFFAA1234 will be
     * returned as a String in format "FFAA1234". A int of value 0 will be
     * returned as "00000000".
     *
     * @param value The int value that will be converted to a hexidecimal
     * String.
     * @return
     */
    public static String intToHex(int value) {
        StringBuilder buffer = new StringBuilder(8);
        appendHexString(buffer, value);
        return buffer.toString();
    }

    /**
     * Creates a 16 character hex String from a long with the long in a "Big
     * Endian" hexidecimal format.For example, a long 0xAABBCCDDEE123456 will be
     * returned as a String in format "AABBCCDDEE123456". A long of value 0 will
     * be returned as "0000000000000000".
     *
     * @param value The long value that will be converted to a hexidecimal
     * String.
     * @return
     */
    public static String longToHex(long value) {
        StringBuilder buffer = new StringBuilder(16);
        appendHexString(buffer, value);
        return buffer.toString();
    }

    public static String stringToHex(String data) {
        if (data == null) {
            return null;
        }
        byte[] bytes = data.getBytes();
        return byteToHex(bytes, 0, bytes.length);
    }

    /**
     * Convert the hex string to string.
     *
     * @param hexString is the hex string.
     * @return the string value that converted from hex string.
     */
    public static String hexToString(String hexString) {
        String uHexString = hexString.toLowerCase();
        StringBuilder sBuf = new StringBuilder();
        for (int i = 0; i < uHexString.length(); i = i + 2) {
            char c = (char) Integer.parseInt(uHexString.substring(i, i + 2), 16);
            sBuf.append(c);
        }
        return sBuf.toString();
    }

    /**
     * Appends a byte array to a StringBuilder with each byte in a "Big Endian"
     * hexidecimal format. For example, a byte 0x34 will be appended as a String
     * in format "34". A byte array of { 0x34, 035 } would append "3435".
     *
     * @param buffer The StringBuilder the byte array in hexidecimal format will
     * be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param bytes The byte array that will be converted to a hexidecimal
     * String. If the byte array is null, this method will append nothing (a
     * noop)
     */
    public static void appendHexString(StringBuilder buffer, byte[] bytes) {
        if (buffer == null || bytes == null) {
            return;     // do nothing (a noop)
        }
        appendHexString(buffer, bytes, 0, bytes.length);
    }

    /**
     * Appends a byte array to a StringBuilder with each byte in a "Big Endian"
     * hexidecimal format. For example, a byte 0x34 will be appended as a String
     * in format "34". A byte array of { 0x34, 035 } would append "3435".
     *
     * @param buffer The StringBuilder the byte array in hexidecimal format will
     * be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param bytes The byte array that will be converted to a hexidecimal
     * String. If the byte array is null, this method will append nothing (a
     * noop)
     * @param offset The offset in the byte array to start from. If the offset
     * or length combination is invalid, this method will throw an
     * IllegalArgumentException.
     * @param length The length (from the offset) to conver the bytes. If the
     * offset or length combination is invalid, this method will throw an
     * IllegalArgumentException.
     */
    public static void appendHexString(StringBuilder buffer, byte[] bytes, int offset, int length) {
        if (buffer == null || bytes == null) {
            return;     // do nothing (a noop)
        }
        assertOffsetLengthValid(offset, length, bytes.length);
        int end = offset + length;
        for (int i = offset; i < end; i++) {
            int nibble1 = (bytes[i] & 0xF0) >>> 4;
            int nibble0 = (bytes[i] & 0x0F);
            buffer.append(HEX_TABLE[nibble1]);
            buffer.append(HEX_TABLE[nibble0]);
        }
    }

    /**
     * Appends 2 characters to a StringBuilder with the byte in a "Big Endian"
     * hexidecimal format. For example, a byte 0x34 will be appended as a String
     * in format "34". A byte of value 0 will be appended as "00".
     *
     * @param buffer The StringBuilder the byte value in hexidecimal format will
     * be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param value The byte value that will be converted to a hexidecimal
     * String.
     */
    public static void appendHexString(StringBuilder buffer, byte value) {
        if (buffer == null) {
            return;
        }
        int nibble = (value & 0xF0) >>> 4;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x0F);
        buffer.append(HEX_TABLE[nibble]);
    }

    /**
     * Appends 4 characters to a StringBuilder with the short in a "Big Endian"
     * hexidecimal format. For example, a short 0x1234 will be appended as a
     * String in format "1234". A short of value 0 will be appended as "0000".
     *
     * @param buffer The StringBuilder the short value in hexidecimal format
     * will be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param value The short value that will be converted to a hexidecimal
     * String.
     */
    public static void appendHexString(StringBuilder buffer, short value) {
        if (buffer == null) {
            return;
        }
        int nibble = (value & 0xF000) >>> 12;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x0F00) >>> 8;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x00F0) >>> 4;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x000F);
        buffer.append(HEX_TABLE[nibble]);
    }

    /**
     * Appends 8 characters to a StringBuilder with the int in a "Big Endian"
     * hexidecimal format. For example, a int 0xFFAA1234 will be appended as a
     * String in format "FFAA1234". A int of value 0 will be appended as
     * "00000000".
     *
     * @param buffer The StringBuilder the int value in hexidecimal format will
     * be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param value The int value that will be converted to a hexidecimal
     * String.
     */
    public static void appendHexString(StringBuilder buffer, int value) {
        if (buffer == null) {
            return;
        }
        int nibble = (value & 0xF0000000) >>> 28;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x0F000000) >>> 24;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x00F00000) >>> 20;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x000F0000) >>> 16;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x0000F000) >>> 12;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x00000F00) >>> 8;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x000000F0) >>> 4;
        buffer.append(HEX_TABLE[nibble]);
        nibble = (value & 0x0000000F);
        buffer.append(HEX_TABLE[nibble]);
    }

    /**
     * Appends 16 characters to a StringBuilder with the long in a "Big Endian"
     * hexidecimal format. For example, a long 0xAABBCCDDEE123456 will be
     * appended as a String in format "AABBCCDDEE123456". A long of value 0 will
     * be appended as "0000000000000000".
     *
     * @param buffer The StringBuilder the long value in hexidecimal format will
     * be appended to. If the buffer is null, this method will throw a
     * NullPointerException.
     * @param value The long value that will be converted to a hexidecimal
     * String.
     */
    public static void appendHexString(StringBuilder buffer, long value) {
        appendHexString(buffer, (int) ((value & 0xFFFFFFFF00000000L) >>> 32));
        appendHexString(buffer, (int) (value & 0x00000000FFFFFFFFL));
    }

    static private void assertOffsetLengthValid(int offset, int length, int arrayLength) {
        if (offset < 0) {
            throw new IllegalArgumentException("The array offset was negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("The array length was negative");
        }
        if (offset + length > arrayLength) {
            throw new ArrayIndexOutOfBoundsException("The array offset+length would access past end of array");
        }
    }

    /**
     * Converts a hexidecimal character such as '0' or 'A' or 'a' to its integer
     * value such as 0 or 10. Used to decode hexidecimal Strings to integer
     * values.
     *
     * @param c The hexidecimal character
     * @return The integer value the character represents
     * @throws IllegalArgumentException Thrown if a character that does not
     * represent a hexidecimal character is used.
     */
    public static int hexCharToIntValue(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
            case 'a':
                return 10;
            case 'B':
            case 'b':
                return 11;
            case 'C':
            case 'c':
                return 12;
            case 'D':
            case 'd':
                return 13;
            case 'E':
            case 'e':
                return 14;
            case 'F':
            case 'f':
                return 15;
            default:
                throw new IllegalArgumentException("The character [" + c + "] does not represent a valid hex digit");
        }
    }

    public static int hexToDecimal(String hex) {
        int result = 0;
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            result = 16 * result + d;
        }
        return result;
    }

    /**
     * Creates a byte array from a CharSequence (String, StringBuilder, etc.)
     * containing only valid hexidecimal formatted characters. Each grouping of
     * 2 characters represent a byte in "Big Endian" format. The hex
     * CharSequence must be an even length of characters. For example, a String
     * of "1234" would return the byte array { 0x12, 0x34 }.
     *
     * @param hexString The String, StringBuilder, etc. that contains the
     * sequence of hexidecimal character values.
     * @return A new byte array representing the sequence of bytes created from
     * the sequence of hexidecimal characters. If the hexString is null, then
     * this method will return null.
     */
    public static byte[] toByteArray(CharSequence hexString) {
        if (hexString == null) {
            return null;
        }
        return toByteArray(hexString, 0, hexString.length());
    }

    /**
     * Creates a byte array from a CharSequence (String, StringBuilder, etc.)
     * containing only valid hexidecimal formatted characters. Each grouping of
     * 2 characters represent a byte in "Big Endian" format. The hex
     * CharSequence must be an even length of characters. For example, a String
     * of "1234" would return the byte array { 0x12, 0x34 }.
     *
     * @param hexString The String, StringBuilder, etc. that contains the
     * sequence of hexidecimal character values.
     * @param offset The offset within the sequence to start from. If the offset
     * is invalid, will cause an IllegalArgumentException.
     * @param length The length from the offset to convert. If the length is
     * invalid, will cause an IllegalArgumentException.
     * @return A new byte array representing the sequence of bytes created from
     * the sequence of hexidecimal characters. If the hexString is null, then
     * this method will return null.
     */
    public static byte[] toByteArray(CharSequence hexString, int offset, int length) {
        if (hexString == null) {
            return null;
        }
        assertOffsetLengthValid(offset, length, hexString.length());

        // a hex string must be in increments of 2
        if ((length % 2) != 0) {
            throw new IllegalArgumentException("The hex string did not contain an even number of characters [actual=" + length + "]");
        }

        // convert hex string to byte array
        byte[] bytes = new byte[length / 2];

        int j = 0;
        int end = offset + length;

        for (int i = offset; i < end; i += 2) {
            int highNibble = hexCharToIntValue(hexString.charAt(i));
            int lowNibble = hexCharToIntValue(hexString.charAt(i + 1));
            bytes[j++] = (byte) (((highNibble << 4) & 0xF0) | (lowNibble & 0x0F));
        }
        return bytes;
    }
}
