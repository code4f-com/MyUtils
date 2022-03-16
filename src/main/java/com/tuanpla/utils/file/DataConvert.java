/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.file;

import com.tuanpla.utils.logging.LogUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author tuanpla
 */
public class DataConvert {

    public DataConvert() {
        super();
    }

    public static byte[] shortToByteArray(short value) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static byte[] charToByteArray(char c) {
        byte[] twoBytes = {(byte) (c & 0xff), (byte) (c >> 8 & 0xff)};
        return twoBytes;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    private byte[] intToByteArray2(final int integer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(integer);
        dos.flush();
        return bos.toByteArray();
    }

    public static byte[] longToByteArray(long value) {
        long temp;
        byte[] rv = new byte[8];
        for (int x = 7; x >= 0; x--) {// big endian
            //for (int x = 0; x < 8; x++){// little endian
            temp = value & 0xFF;
            if (temp > 127) {
                temp -= 256;
            }
            rv[x] = (byte) temp;
            value >>= 8;
        }
        return rv;
    }

    // returns a byte array of length 4
    private static byte[] intToDWord(int i) {
        byte[] dword = new byte[4];
        dword[0] = (byte) (i & 0x00FF);
        dword[1] = (byte) ((i >> 8) & 0x000000FF);
        dword[2] = (byte) ((i >> 16) & 0x000000FF);
        dword[3] = (byte) ((i >> 24) & 0x000000FF);
        return dword;
    }

    // returns a byte array of length 4
    private static byte[] intToDWord2(int i) {
        byte[] dword = new byte[4];
        dword[0] = (byte) ((i >> 24) & 0x000000FF);
        dword[1] = (byte) ((i >> 16) & 0x000000FF);
        dword[2] = (byte) ((i >> 8) & 0x000000FF);
        dword[3] = (byte) (i & 0x00FF);
        return dword;
    }

    /*
     * Convert a byte array to a short.  Short.MIN_VALUE is returned
     * if the startIndex is greater or equal to the endIndex, or if the
     * resultant unsigned integer is too large to store in a short.
     */
    public static short byteToShort(byte[] inBytes) {
        return byteToShort(inBytes, 0, inBytes.length);
    }

    public static short byteToShort(byte[] inBytes, int startIndex,
            int endIndex) {
        String hexString;
        short outputShort;
        hexString = byteToHex(inBytes, startIndex, endIndex);
        try {
            outputShort = Short.parseShort(hexString, 16);
        } catch (Exception ex) {
            outputShort = Short.MIN_VALUE;
        }
        return outputShort;
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
        String hexString;
        int outputInt;
        hexString = byteToHex(inBytes, startIndex, endIndex);
        try {
            outputInt = Integer.parseInt(hexString, 16);
        } catch (Exception ex) {
            outputInt = Integer.MIN_VALUE;
        }
        return outputInt;
    }

    /*
     * Convert a byte array to a long.  Long.MIN_VALUE is returned
     * if the startIndex is greater or equal to the endIndex, or if the
     * resultant unsigned integer is too large to store in a long.
     */
    public static long byteToLong(byte[] inBytes) {
        return byteToLong(inBytes, 0, inBytes.length);
    }

    public static long byteToLong(byte[] inBytes, int startIndex, int endIndex) {
        String hexString;
        long outputLong;
        hexString = byteToHex(inBytes, startIndex, endIndex);
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

    /*
     * Convert a byte[] array to Hexadecimal string.
     */
    public static String byteToHex(byte[] inBytes) {
        return byteToHex(inBytes, 0, inBytes.length);
    }

    public static String byteToHex(byte[] inBytes, int startIndex, int endIndex) {
        byte newByte = 0x00;
        int i, hexIndex;
        String hexChars = "0123456789ABCDEF";
        StringBuilder outBuffer = new StringBuilder(endIndex - startIndex);
        if (inBytes == null || endIndex <= startIndex) {
            return (String) null;
        }
        for (i = startIndex; i < endIndex; i++) {
            /*
             *       Each Hexadecimal character represents 4 bits and each element of
             *       the byte array represents 8 bits.  First strip off the left 4
             *       bits, shift to the least significant (right) portion of a new
             *       byte, then mask the upper portion to allow proper conversion to an
             *       integer between 0 and 15.  This value can be used as the index into
             *       the hexadecimal character string.
             */
            newByte = (byte) (inBytes[i] & 0xF0);
            newByte = (byte) (newByte >>> 4);
            newByte = (byte) (newByte & 0x0F);
            hexIndex = (int) newByte;
            outBuffer.append(hexChars.substring(hexIndex, hexIndex + 1));
            /*
             *       Now strip off the right 4 bits, shift and convert to an integer
             *       between 0 and 15.
             */
            newByte = (byte) (inBytes[i] & 0x0F);
            hexIndex = (int) newByte;
            outBuffer.append(hexChars.substring(hexIndex, hexIndex + 1));
        }
        return outBuffer.toString();
    }

    public static byte[] InputStream2Bytes(InputStream is) {
        if (is == null) {
            return null;
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        byte[] dataReturn = null;
        try {
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();

            while (true) {
                int iBytes = bis.read(buffer);
                if (iBytes == -1) {
                    break;
                }
                baos.write(buffer, 0, iBytes);
            }
            dataReturn = baos.toByteArray();
        } catch (IOException ex) {
            LogUtils.debug( "Error DataConvert.InputStream2Bytes:" + ex.getMessage());
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (Exception e) {
            }
        }
        return dataReturn;
    }

    public static byte[] InputStream2Bytes(ImageInputStream is) {
        if (is == null) {
            return null;
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        byte[] dataReturn = null;
        try {
            bis = new BufferedInputStream((InputStream) is);
            baos = new ByteArrayOutputStream();

            while (true) {
                int iBytes = bis.read(buffer);
                if (iBytes == -1) {
                    break;
                }
                baos.write(buffer, 0, iBytes);
            }
            dataReturn = baos.toByteArray();
        } catch (IOException ex) {
            LogUtils.debug( "Error DataConvert.InputStream2Bytes:" + ex.getMessage());
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (Exception e) {
            }
        }
        return dataReturn;
    }

    public static String InputStream2String(InputStream is) {
        if (is == null) {
            return null;
        }
        String strReturn = null;
        byte[] buffer = null;
        buffer = new byte[1024];

        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        int iBytes = -1;
        try {
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();

            iBytes = bis.read(buffer);
            while (iBytes > 0) {
                baos.write(buffer, 0, iBytes);
                iBytes = bis.read(buffer);
            }
            strReturn = baos.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }

            } catch (Exception e) {
            }

        }
        return strReturn;
    }

    public static int InputStream2File(InputStream is, String sPath) {
        if (is == null) {
            return -1;
        }

        byte[] b = null;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(sPath);
            b = InputStream2Bytes(is);
            fout.write(b);
            fout.flush();
            fout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return b.length;
    }

    public static int Bytes2File(byte[] bInput, String sPath) {
        if (bInput == null) {
            return -1;
        }

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(sPath);
            fout.write(bInput);
            fout.flush();
            fout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bInput.length;
    }

    public static String[] Collection2StringArr(Collection col) {
        if (col == null) {
            return null;
        }
        /*
         Object[] arrObj = col.toArray();
         String[] arrString= new String[col.size()];
         for(int i=0;i<arrObj.length;i++){
         arrString[i]=(String)arrObj[i];
         }
         */
        String[] arrString = new String[col.size()];
        col.toArray(arrString);
        return arrString;
    }

    /**
     * convert a single char to corresponding nibble.
     *
     * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
     * signs.
     *
     * @return corresponding integer
     */
    private static int charToNibble(char c) {
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

    /**
     * Convert a hex string to a byte array. Permits upper or lower case hex.
     *
     * @param s String must have even number of characters. and be formed only
     * of digits 0-9 A-F or a-f. No spaces, minus or plus signs.
     * @return corresponding byte array.
     */
    public static byte[] HexString2Byte(String s) {
        int stringLength = s.length();
        if ((stringLength & 0x1) != 0) {
            throw new IllegalArgumentException(
                    "HexString2Byte requires an even number of hex characters");
        }
        byte[] b = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }
    //////////////////////////////////////////////////////////////////////
    // HEX Conversion
    // You can display in hex using code like this:
    //       String hex = Integer.toString(i , 16 /* radix */ );
    // That won't apply any lead zeroes.
    // Here is how to get a lead 0 for a fixed two character hex representation
    // of a byte:    convert a byte b to 2-char hex string with possible leading zero.
    //       String s2 = Integer.toString( ( b & 0xff ) + 0x100, 16 /* radix */ ) .substring( 1 );
    // You can convert a hex String to internal binary like this:
    //       int i = Integer.parseInt(g .trim(), 16 /* radix */ );
    //////////////////////////////////////////////////////////////////////
    // Fast convert a byte array to a hex string
    // with possible leading zero.
    final static char[] hexChar = {
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F'};

    public static String Byte2HexString(byte[] b) {
        if (b == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static int unsignedByte(byte value) {
        if (value < 0) {
            return (value + 256);
        } else {
            return value;
        }
    }

    public static String Byte2HexString2(byte[] buf) {
        if (buf == null) {
            return null;
        }
        StringBuilder strBuffer = new StringBuilder();
        String str = null;
        for (int i = 0; i < buf.length; i++) {
            str = Integer.toHexString(unsignedByte(buf[i])).toUpperCase();
            if (str.length() == 1) {
                str = "0" + str;
            }
            strBuffer.append(str);
        }
        return strBuffer.toString();
    }

    public static byte[] PictureMsgEncode(String sText, byte[] bOtb) {
        ByteArrayOutputStream encoded = null;
        try {
            encoded = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(encoded);
            dout.writeByte(0x30); //version 0
            dout.writeByte(0x00); //"00"
            if (sText != null) {
                dout.writeShort(sText.length());
                dout.writeBytes(sText);
            } else {
                dout.writeShort(0x0000);
            }
            dout.writeByte(0x02);
            dout.writeShort(0x0100);
            encoded.write(bOtb);
            return encoded.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] getBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream is = new FileInputStream(file);
        LogUtils.debug( "\nDEBUG: FileInputStream is " + file);

        // Get the size of the file
        long length = file.length();
        LogUtils.debug( "DEBUG: Length of " + file + " is " + length + "\n");

        /*
         * You cannot create an array using a long type. It needs to be an int
         * type. Before converting to an int type, check to ensure that file is
         * not loarger than Integer.MAX_VALUE;
         */
        if (length > Integer.MAX_VALUE) {
            LogUtils.debug( "File is too large to process");
            return null;
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length)
                && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {

            offset += numRead;

        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;

    }

    public static String byte2Base64(byte[] data) {
        try {
            return Base64.encodeBase64String(data);
        } catch (Exception e) {
            return "";
        }
    }
}
