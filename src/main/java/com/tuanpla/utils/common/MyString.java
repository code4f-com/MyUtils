/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.common;

import com.tuanpla.utils.exception.CustomerException;
import com.tuanpla.utils.file.StreamUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author tuanp
 */
public abstract class MyString {

    private static final Random RANDOM = new SecureRandom();
    private static final String PRINTABLE = ": ~`!@#$%^&*()-_+=/\\,.[]{}|?<>\"'";
    private static final String SAFE = ":~!@#$%^&*()-_+=/\\,.[]{}|?<>";

    // (gt, lt, quot, amp, apos) and then newline, carriage return
    private static final String[][] XML_CHARS = {
        {"&", "&amp;"},
        {"<", "&lt;"},
        {">", "&gt;"},
        {"\"", "&quot;"},
        {"'", "&apos;"},
        {"\n", "&#10;"},
        {"\r", "&#13;"},};
    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * Searches the string for occurrences of the pattern $ENV{key} and attempts
     * to replace this pattern with a value from the System environment obtained
     * using the 'key'.For example, including "$ENV{USERNAME}" in a string and
     * calling this method would then attempt to replace the entire pattern with
     * the value of the environment variable "USERNAME".The System environment
     * is obtained in Java with a call to System.getenv(). An environment
     * variable is typically defined in the Linux shell or Windows property
     * tabs. NOTE: A Java System property is not the same as an environment
     * variable.
     *
     * @param string0 The string to perform substitution on such as "Hello
     * $ENV{USERNAME}". This string may be null, empty, or contain one or more
     * substitutions.
     * @return A string with all occurrences of keys substituted with their
     * values obtained from the System environment. Can be null if the original
     * string was null.
     * @throws com.tuanpla.utils.exception.CustomerException
     * @see #substituteWithProperties(java.lang.String, java.lang.String,
     * java.lang.String, java.util.Properties)
     */
    public static String substituteWithEnvironment(String string0) throws CustomerException {
        // turn environment into properties
        Properties envProps = new Properties();
        // add all system environment vars to the properties
        envProps.putAll(System.getenv());
        // delegate to other method using the default syntax $ENV{<key>}
        Set keySets = envProps.keySet();
        Iterator keys = keySets.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            System.out.println("key=" + key);
            System.out.println("value =" + envProps.getProperty(key));
        }
        return substituteWithProperties(string0, "$ENV{", "}", envProps);
    }

    /**
     * Searches string for occurrences of a pattern, extracts out a key name
     * between the startStr and endStr tokens, then attempts to replace the
     * property value into the string.This method is useful for merging property
     * values into configuration strings/settings.For examle, the system
     * environment Map could be converted into a Properties object then have its
     * values merged into a String so that users can access environment
     * variables.
     *
     * @param string0 The string to perform substitution on such as "Hello
     * $ENV{TEST}". This string may be null, empty, or contain one or more
     * substitutions.
     * @param startStr The string that marks the start of a replacement key such
     * as "$ENV{" if the final search pattern you wanted was "$ENV{<key>}".
     * @param endStr The string that marks the end of a replacement key such as
     * "}" if the final search pattern you wanted was "$ENV{<key>}".
     * @param properties The property keys and associated values to use for
     * replacement.
     * @return A string with all occurrences of keys substituted with their
     * values obtained from the properties object. Can be null if the original
     * string was null.
     * @throws com.tuanpla.utils.exception.CustomerException
     * @see #substituteWithEnvironment(java.lang.String)
     */
    public static String substituteWithProperties(String string0, String startStr, String endStr, Properties properties) throws CustomerException {
        // a null source string will always return the same -- a null result
        if (string0 == null) {
            return null;
        }
        // create a builder for the resulting string
        StringBuilder result = new StringBuilder(string0.length());

        // attempt to find the first occurrence of the starting string
        int end = -1;
        int pos = string0.indexOf(startStr);

        // keep looping while we keep finding more occurrences
        while (pos >= 0) {
            // is there string data before the position that we should append to the result?
            if (pos > end + 1) {
                result.append(string0.substring(end + endStr.length(), pos));
            }

            // search for endStr starting from the end of the startStr
            end = string0.indexOf(endStr, pos + startStr.length());

            // was the end found?
            if (end < 0) {
                throw new CustomerException("End of substitution pattern '" + endStr + "' not found [@position=" + pos + "]");
            }

            // extract the part in the middle of the start and end strings
            String key = string0.substring(pos + startStr.length(), end);
            // NOTE: don't trim the key, whitespace technically matters...

            // was there anything left?
            if (key == null || key.equals("")) {
                throw new CustomerException("Property key was empty in string with an occurrence of '" + startStr + endStr + "' [@position=" + pos + "]");
            }

            // attempt to get this property
            String value = properties.getProperty(key);
            // was the property found
            if (value == null) {
                throw new CustomerException("A property value for '" + startStr + key + endStr + "' was not found (property missing?)");
            }

            // append this value to our result
            result.append(value);

            // find next occurrence after last end
            pos = string0.indexOf(startStr, end + endStr.length());
        }

        // is there any string data we missed in the loop above?
        if (end + endStr.length() < string0.length()) {
            // append the remaining part of the string
            result.append(string0.substring(end + endStr.length()));
        }

        return result.toString();
    }

    /**
     * Returns true if the String is considered a "safe" string where only
     * specific characters are allowed to be used.Useful for checking passwords
     * or other information you don't want a user to be able to type just
     * anything in.This method does not allow any whitespace characters,
     * newlines, carriage returns. Primarily allows [a-z] [A-Z] [0-9] and a few
     * other useful ASCII characters such as ":~!@#$%^*()-_+=/\\,.[]{}|?<>" (but
     * not the quote chars)
     *
     * @param input
     * @return
     */
    public static boolean isSafeString(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!isSafeChar(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the char is considered a "safe" char.Please see
     * documentation for isSafeString().
     *
     * @param ch
     * @return
     * @see #isSafeString(java.lang.String)
     */
    public static boolean isSafeChar(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return true;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return true;
        }
        if (ch >= '0' && ch <= '9') {
            return true;
        }
        // loop thru our PRINTABLE string
        for (int i = 0; i < SAFE.length(); i++) {
            if (ch == SAFE.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the targetString is contained within the array of strings. This
     * method will return true if a "null" is contained in the array and the
     * targetString is also null.
     *
     * @param strings The array of strings to search.
     * @param targetString The string to search for
     * @return True if the string is contained within, otherwise false. Also
     * returns false if the strings array is null.
     */
    public static boolean contains(String[] strings, String targetString) {
        return (indexOf(strings, targetString) != -1);
    }

    /**
     * Finds the first occurrence of the targetString in the array of strings.
     * Returns -1 if an occurrence wasn't found. This method will return true if
     * a "null" is contained in the array and the targetString is also null.
     *
     * @param strings The array of strings to search.
     * @param targetString The string to search for
     * @return The index of the first occurrence, or -1 if not found. If strings
     * array is null, will return -1;
     */
    public static int indexOf(String[] strings, String targetString) {
        if (strings == null) {
            return -1;
        }
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                if (targetString == null) {
                    return i;
                }
            } else if (targetString != null) {
                if (strings[i].equals(targetString)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * If present, this method will strip off the leading and trailing "
     * character in the string parameter.For example, "10958" will becomes just
     * 10958.
     *
     * @param input
     * @return
     */
    public static String stripQuotes(String input) {
        // if an empty string, return it
        if (input.length() == 0) {
            return input;
        }
        // if the first and last characters are quotes, just do 1 substring
        if (input.length() > 1 && input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"') {
            return input.substring(1, input.length() - 1);
        } else if (input.charAt(0) == '"') {
            input = input.substring(1);
        } else if (input.charAt(input.length() - 1) == '"') {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }

    /**
     * Checks if a string contains only digits.
     *
     * @param input
     * @return True if the string0 only contains digits, or false otherwise.
     */
    public static boolean containsOnlyDigits(String input) {
        // are they all digits?
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Splits a string around matches of the given delimiter character.Where
     * applicable, this method can be used as a substitute for
     * <code>String.split(String regex)</code>, which is not available on a
     * JSR169/Java ME platform.
     *
     *
     * @param str the string to be split
     * @param delim the delimiter
     * @return
     * @throws NullPointerException if str is null
     */
    public static String[] split(String str, char delim) {
        if (str == null) {
            throw new NullPointerException("str can't be null");
        }

        // Note the javadoc on StringTokenizer:
        //     StringTokenizer is a legacy class that is retained for
        //     compatibility reasons although its use is discouraged in
        //     new code.
        // In other words, if StringTokenizer is ever removed from the JDK,
        // we need to have a look at String.split() (or java.util.regex)
        // if it is supported on a JSR169/Java ME platform by then.
        StringTokenizer st = new StringTokenizer(str, String.valueOf(delim));
        int n = st.countTokens();
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = st.nextToken();
        }
        return s;
    }

    /**
     * Used to print out a string for error messages, chops is off at 60 chars
     * for historical reasons.
     *
     * @param input
     * @return
     */
    public final static String formatForPrint(String input) {
        if (input.length() > 60) {
            StringBuilder tmp = new StringBuilder(input.substring(0, 60));
            tmp.append("&");
            input = tmp.toString();
        }
        return input;
    }

    /**
     * A method that receive an array of Objects and return a String array
     * representation of that array.
     *
     * @param objArray
     * @return
     */
    public static String[] toStringArray(Object[] objArray) {
        int idx;
        int len = objArray.length;
        String[] strArray = new String[len];

        for (idx = 0; idx < len; idx++) {
            strArray[idx] = objArray[idx].toString();
        }

        return strArray;
    }

    /**
     * Get 7-bit ASCII character array from input String.The lower 7 bits of
     * each character in the input string is assumed to be the ASCII character
     * value.Hexadecimal - Character | 00 NUL| 01 SOH| 02 STX| 03 ETX| 04 EOT|
     * 05 ENQ| 06 ACK| 07 BEL| | 08 BS | 09 HT | 0A NL | 0B VT | 0C NP | 0D CR |
     * 0E SO | 0F SI | | 10 DLE| 11 DC1| 12 DC2| 13 DC3| 14 DC4| 15 NAK| 16 SYN|
     * 17 ETB| | 18 CAN| 19 EM | 1A SUB| 1B ESC| 1C FS | 1D GS | 1E RS | 1F US |
     * | 20 SP | 21 ! | 22 " | 23 # | 24 $ | 25 % | 26 & | 27 ' | | 28 ( | 29 )
     * | 2A * | 2B + | 2C , | 2D - | 2E . | 2F / | | 30 0 | 31 1 | 32 2 | 33 3 |
     * 34 4 | 35 5 | 36 6 | 37 7 | | 38 8 | 39 9 | 3A : | 3B ; | 3C  < | 3D  = | 3E
     * > | 3F ? | | 40 @ | 41 A | 42 B | 43 C | 44 D | 45 E | 46 F | 47 G | | 48
     * H | 49 I | 4A J | 4B K | 4C L | 4D M | 4E N | 4F O | | 50 P | 51 Q | 52 R
     * | 53 S | 54 T | 55 U | 56 V | 57 W | | 58 X | 59 Y | 5A Z | 5B [ | 5C \ |
     * 5D ] | 5E ^ | 5F _ | | 60 ` | 61 a | 62 b | 63 c | 64 d | 65 e | 66 f |
     * 67 g | | 68 h | 69 i | 6A j | 6B k | 6C l | 6D m | 6E n | 6F o | | 70 p |
     * 71 q | 72 r | 73 s | 74 t | 75 u | 76 v | 77 w | | 78 x | 79 y | 7A z |
     * 7B { | 7C | | 7D } | 7E ~ | 7F DEL|
     *
     * @param input
     * @return
     */
    public static byte[] getAsciiBytes(String input) {
        char[] c = input.toCharArray();
        byte[] b = new byte[c.length];
        for (int i = 0; i < c.length; i++) {
            b[i] = (byte) (c[i] & 0x007F);
        }

        return b;
    }

    public static String getAsciiString(byte[] input) {
        StringBuilder buf = new StringBuilder(input.length);
        for (byte b : input) {
            buf.append((char) b);
        }
        return buf.toString();
    }

    /**
     * Trim off trailing blanks but not leading blanks
     *
     * @param str
     * @return The input with trailing blanks stipped off
     */
    public static String trimTrailing(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(str.charAt(len - 1))) {
                break;
            }
        }
        return str.substring(0, len);
    }

    /**
     * Truncate a String to the given length with no warnings or error raised if
     * it is bigger.
     *
     * @param	value String to be truncated
     * @param	length	Maximum length of string
     * @return Returns value if value is null or value.length() is less or equal
     * to than length, otherwise a String representing value truncated to
     * length.
     */
    public static String truncate(String value, int length) {
        if (value != null && value.length() > length) {
            value = value.substring(0, length);
        }
        return value;
    }

    /**
     * Return a slice (substring) of the passed in value, optionally trimmed.
     * WARNING - endOffset is inclusive for historical reasons, unlike
     * String.substring() which has an exclusive ending offset.
     *
     * @param value Value to slice, must be non-null.
     * @param beginOffset Inclusive start character
     * @param endOffset Inclusive end character
     * @param trim To trim or not to trim
     * @return Sliceed value.
     */
    public static String slice(String value,
            int beginOffset, int endOffset,
            boolean trim) {
        String retval = value.substring(beginOffset, endOffset + 1);

        if (trim) {
            retval = retval.trim();
        }

        return retval;
    }

    public static char[] HEX_TABLE = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Return true if the character is printable in ASCII.Not using
     * Character.isLetterOrDigit(); applies to all unicode ranges.
     *
     * @param ch
     * @return
     */
    public static boolean isPrintableChar(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return true;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return true;
        }
        if (ch >= '0' && ch <= '9') {
            return true;
        }

        // loop thru our PRINTABLE string
        for (int i = 0; i < PRINTABLE.length(); i++) {
            if (ch == PRINTABLE.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Convert a byte array to a human-readable String for debugging purposes.
     *
     * @param prefix
     * @param data
     * @return
     */
    public static String hexDump(String prefix, byte[] data) {
        byte byte_value;

        StringBuilder str = new StringBuilder(data.length * 3);

        str.append(prefix);

        for (int i = 0; i < data.length; i += 16) {
            // dump the header: 00000000:
            String offset = Integer.toHexString(i);

            // "0" left pad offset field so it is always 8 char's long.
            str.append("  ");
            for (int offlen = offset.length(); offlen < 8; offlen++) {
                str.append("0");
            }
            str.append(offset);
            str.append(":");

            // dump hex version of 16 bytes per line.
            for (int j = 0; (j < 16) && ((i + j) < data.length); j++) {
                byte_value = data[i + j];

                // add spaces between every 2 bytes.
                if ((j % 2) == 0) {
                    str.append(" ");
                }

                // dump a single byte.
                byte high_nibble = (byte) ((byte_value & 0xf0) >>> 4);
                byte low_nibble = (byte) (byte_value & 0x0f);

                str.append(HEX_TABLE[high_nibble]);
                str.append(HEX_TABLE[low_nibble]);
            }

            // IF THIS IS THE LAST LINE OF HEX, THEN ADD THIS
            if (i + 16 > data.length) {
                // for debugging purposes, I want the last bytes always padded
                // over so that the ascii portion is correctly positioned
                int last_row_byte_count = data.length % 16;
                int num_bytes_short = 16 - last_row_byte_count;
                // number of spaces to add = (num bytes remaining * 2 spaces per byte) + (7 - (num bytes % 2))
                int num_spaces = (num_bytes_short * 2) + (7 - (last_row_byte_count / 2));
                for (int v = 0; v < num_spaces; v++) {
                    str.append(" ");
                }
            }

            // dump ascii version of 16 bytes
            str.append("  ");

            for (int j = 0; (j < 16) && ((i + j) < data.length); j++) {
                char char_value = (char) data[i + j];

                // RESOLVE (really want isAscii() or isPrintable())
                //if (Character.isLetterOrDigit(char_value))
                if (isPrintableChar(char_value)) {
                    str.append(String.valueOf(char_value));
                } else {
                    str.append(".");
                }
            }

            // new line
            str.append("\n");
        }

        // always trim off the last newline
        str.deleteCharAt(str.length() - 1);

        return (str.toString());
    }

    // The functions below are used for uppercasing SQL in a consistent manner.
    // Derby will uppercase Turkish to the English locale to avoid i
    // uppercasing to an uppercase dotted i. In future versions, all
    // casing will be done in English.   The result will be that we will get
    // only the 1:1 mappings  in
    // http://www.unicode.org/Public/3.0-Update1/UnicodeData-3.0.1.txt
    // and avoid the 1:n mappings in
    //http://www.unicode.org/Public/3.0-Update1/SpecialCasing-3.txt
    //
    // Any SQL casing should use these functions
    /**
     * Convert string to uppercase Always use the java.util.ENGLISH locale
     *
     * @param s string to uppercase
     * @return uppercased string
     */
    public static String SQLToUpperCase(String s) {
        return s.toUpperCase(Locale.ENGLISH);
    }

    /**
     * Compares two strings Strings will be uppercased in english and compared
     * equivalent to s1.equalsIgnoreCase(s2) throws NPE if s1 is null
     *
     * @param s1 first string to compare
     * @param s2 second string to compare
     *
     * @return true if the two upppercased ENGLISH values are equal return false
     * if s2 is null
     */
    public static boolean SQLEqualsIgnoreCase(String s1, String s2) {
        if (s2 == null) {
            return false;
        } else {
            return SQLToUpperCase(s1).equals(SQLToUpperCase(s2));
        }

    }

    /**
     * Normalize a SQL identifer, up-casing if <regular identifer>, and handling
     * of <delimited identifer> (SQL 2003, section 5.2).The normal form is used
     * internally in Derby.
     *
     * @param id syntacically correct SQL identifier
     * @return
     */
    public static String normalizeSQLIdentifier(String id) {
        if (id.length() == 0) {
            return id;
        }

        if (id.charAt(0) == '"'
                && id.length() >= 3
                && id.charAt(id.length() - 1) == '"') {
            // assume syntax is OK, thats is, any quotes inside are doubled:

            return compressQuotes(
                    id.substring(1, id.length() - 1), "\"\"");

        } else {
            return SQLToUpperCase(id);
        }
    }

    /**
     * Compress 2 adjacent (single or double) quotes into a single (s or d)
     * quote when found in the middle of a String.
     *
     * NOTE: """" or '''' will be compressed into "" or ''. This function
     * assumes that the leading and trailing quote from a string or delimited
     * identifier have already been removed.
     *
     * @param source string to be compressed
     * @param quotes string containing two single or double quotes.
     * @return String where quotes have been compressed
     */
    public static String compressQuotes(String source, String quotes) {
        String result = source;
        int index;

        /* Find the first occurrence of adjacent quotes. */
        index = result.indexOf(quotes);

        /* Replace each occurrence with a single quote and begin the
         * search for the next occurrence from where we left off.
         */
        while (index != -1) {
            result = result.substring(0, index + 1)
                    + result.substring(index + 2);
            index = result.indexOf(quotes, index + 1);
        }

        return result;
    }

    /**
     * Quote a string so that it can be used as an identifier or a string
     * literal in SQL statements. Identifiers are surrounded by double quotes
     * and string literals are surrounded by single quotes. If the string
     * contains quote characters, they are escaped.
     *
     * @param source the string to quote
     * @param quote the character to quote the string with (' or &quot;)
     * @return a string quoted with the specified quote character
     * @see #quoteStringLiteral(String)
     * @see IdUtil#normalToDelimited(String)
     */
    static String quoteString(String source, char quote) {
        // Normally, the quoted string is two characters longer than the source
        // string (because of start quote and end quote).
        StringBuilder quoted = new StringBuilder(source.length() + 2);
        quoted.append(quote);
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            // if the character is a quote, escape it with an extra quote
            if (c == quote) {
                quoted.append(quote);
            }
            quoted.append(c);
        }
        quoted.append(quote);
        return quoted.toString();
    }

    /**
     * Quote a string so that it can be used as a string literal in an SQL
     * statement.
     *
     * @param string the string to quote
     * @return the string surrounded by single quotes and with proper escaping
     * of any single quotes inside the string
     */
    public static String quoteStringLiteral(String string) {
        return quoteString(string, '\'');
    }

    /**
     * Turn an array of ints into a printable string.Returns what's returned in
     * Java 5 by java.util.Arrays.toString(int[]).
     *
     * @param raw
     * @return
     */
    public static String stringify(int[] raw) {
        if (raw == null) {
            return "null";
        }

        StringBuilder buffer = new StringBuilder();
        int count = raw.length;

        buffer.append("[ ");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(raw[i]);
        }
        buffer.append(" ]");

        return buffer.toString();
    }

    /**
     * Checks if the string is an empty value which is true if the string is
     * null or if the string represents an empty string of "". Please note that
     * a string with just a space " " would not be considered empty.
     *
     * @param input The string to check
     * @return True if null or "", otherwise false.
     */
    public static boolean isEmpty(String input) {
        return input == null || input.length() == 0 || input.equalsIgnoreCase("null");
    }

    public static boolean nonEmpty(String input) {
        return input != null && input.length() > 0;
    }

    public static boolean isEqual(String string0, String string1) {
        return isEqual(string0, string1, true);
    }

    public static String listToDelimitedString(@Nullable List<Long> arr, String delim) {
        if (arr == null || arr.isEmpty()) {
            return "";
        }
        if (arr.size() == 1) {
            return ObjectUtils.nullSafeToString(arr.get(0));
        }

        StringJoiner sj = new StringJoiner(delim);
        for (Object elem : arr) {
            sj.add(String.valueOf(elem));
        }
        return sj.toString();
    }

    /**
     * Viết hoa chữ cái đầu trong từng từ của String
     *
     * @param input
     * @return
     */
    public static String capitalizeFirstLetter(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toTitleCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String removeSlash(@Nullable String input) {
        return input == null ? "" : input.replaceAll("\\/", "");
    }

    /**
     * Tạo 1 String từ 1 số với chiều dài tối đa của String. Nếu nhỏ hơn chiều
     * dài tối đa thì sẽ thêm số 0 ở đầu
     *
     * @param number
     * @param maxLength
     * @return
     */
    public static String toStringFormat(int number, int maxLength) {
        String formattedNumber = String.format("%0" + maxLength + "d", number);
        return formattedNumber;
    }

    /**
     * Tạo 1 String từ 1 số với chiều dài tối đa của String. Nếu nhỏ hơn chiều
     * dài tối đa thì sẽ thêm số 0 ở đầu
     *
     * @param number
     * @param maxLength
     * @return
     */
    public static String toStringFormat(Long number, int maxLength) {
        String formattedNumber = String.format("%0" + maxLength + "d", number);
        return formattedNumber;
    }

    public static boolean stringInList(List<String> data, String value) {
        List<String> lowercaseList = data.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return lowercaseList.contains(value.toLowerCase());
    }

    /**
     * Returns the value from calling "toString()" on the object, but is a safe
     * version that gracefully handles NULL objects by returning a String of "".
     *
     * @param obj The object to call toString() on. Safely handles a null
     * object.
     * @return The value from obj.toString() or "" if the object is null.
     * @see #toStringWithNullAsNull(java.lang.Object)
     */
    public static String toStringWithNullAsEmpty(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * Returns the value from calling "toString()" on the object, but is a safe
     * version that gracefully handles NULL objects by returning a String of
     * "<NULL>".
     *
     * @param obj The object to call toString() on. Safely handles a null
     * object.
     * @return The value from obj.toString() or "<NULL>" if the object is null.
     * @see #toStringWithNullAsEmpty(java.lang.Object)
     */
    public static String toStringWithNullAsReplaced(Object obj) {
        if (obj == null) {
            return "<NULL>";
        } else {
            return obj.toString();
        }
    }

    /**
     * Returns the value from calling "toString()" on the object, but is a safe
     * version that gracefully handles NULL objects by returning null (rather
     * than causing a NullPointerException).
     *
     * @param obj The object to call toString() on. Safely handles a null
     * object.
     * @return The value from obj.toString() or null if the object is null.
     * @see #toStringWithNullAsEmpty(java.lang.Object)
     */
    public static String toStringWithNullAsNull(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }

    /**
     * Checks if both strings are equal to each other. Safely handles the case
     * where either string may be null. The strings are evaluated as equal if
     * they are both null or if they actually equal each other. One string that
     * is null while the other one isn't (even if its an empty string) will be
     * considered as NOT equal. Case sensitive comparisons are optional.
     *
     * @param string0 The string to compare
     * @param string1 The other string to compare with
     * @param caseSensitive If true a case sensitive comparison will be made,
     * otherwise equalsIgnoreCase will be used.
     * @return True if the strings are both null or equal to each other,
     * otherwise false.
     */
    public static boolean isEqual(String string0, String string1, boolean caseSensitive) {
        if (string0 == null && string1 == null) {
            return true;
        }
        if (string0 == null || string1 == null) {
            return false;
        }
        if (caseSensitive) {
            return string0.equals(string1);
        } else {
            return string0.equalsIgnoreCase(string1);
        }
    }

    public static String readToString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * Escapes the characters in a String using XML entities. For example:
     * "bread" & "butter'ed" => &quot;bread&quot; &amp;
     * &quot;butter&apos;ed&quot;
     *
     * Supports the five basic XML entities (gt, lt, quot, amp, apos) and also
     * supports a newline and carriage return character. A newline is escaped to
     * &#10; and a carriage return to &#13;
     *
     * @param value The string to escape
     * @return The escaped String that can be used in an XML document.
     */
    public static String escapeXml(String value) {
        // null to null
        if (value == null) {
            return null;
        }

        // assume the resulting string will be the same
        int len = value.length();
        StringBuilder buf = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            boolean entityFound = false;
            // is this a matching entity?
            for (String[] XML_CHARS1 : XML_CHARS) {
                // is this the matching character?
                if (c == XML_CHARS1[0].charAt(0)) {
                    // append the entity
                    buf.append(XML_CHARS1[1]);
                    entityFound = true;
                }
            }
            if (!entityFound) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Removes all other characters from a string except digits. A good way of
     * cleaing up something like a phone number.
     *
     * @param str0 The string to clean up
     * @return A new String that has all characters except digits removed
     */
    public static String removeAllCharsExceptDigits(String str0) {
        if (str0 == null) {
            return null;
        }
        if (str0.length() == 0) {
            return str0;
        }
        StringBuilder buf = new StringBuilder(str0.length());
        int length = str0.length();
        for (int i = 0; i < length; i++) {
            char c = str0.charAt(i);
            if (Character.isDigit(c)) {
                // append this character to our string
                buf.append(c);
            }
        }
        return buf.toString();
    }

    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------
    /**
     * Check whether the given object (possibly a {@code String}) is empty.This
     * is effectively a shortcut for {@code !hasLength(String)}
     * .<p>
     * This method accepts any Object as an argument, comparing it to
     * {@code null} and the empty String. As a consequence, this method will
     * never return {@code true} for a non-null non-String object.
     * <p>
     * The Object signature is useful for general attribute handling code that
     * commonly deals with Strings but generally has to iterate over Objects
     * since attributes may e.g. be primitive value objects as well.
     * <p>
     * <b>Note: If the object is typed to {@code String} upfront, prefer
     * {@link #hasLength(String)} or {@link #hasText(String)} instead.</b>
     *
     * @param str the candidate object (possibly a {@code String})
     * @return
     * @since 3.2.1
     */
    public static boolean isEmpty(@Nullable Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor of
     * length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code CharSequence} that
     * purely consists of whitespace.
     * <p>
     * <pre class="code">
     * StringUtils.hasLength(null) = false StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and
     * has length
     * @see #hasLength(String)
     * @see #hasText(CharSequence)
     */
    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length
     * 0.
     * <p>
     * Note: this method returns {@code true} for a {@code String} that purely
     * consists of whitespace.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has
     * length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * Check whether the given {@code CharSequence} contains actual
     * <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     * <p>
     * <pre class="code">
     * StringUtils.hasText(null) = false StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     * @see #hasText(String)
     * @see #hasLength(CharSequence)
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the {@code String}
     * is not {@code null}, its length is greater than 0, and it contains at
     * least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null}, its
     * length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     * @see #hasLength(String)
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code CharSequence} contains any whitespace
     * characters.
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not empty and
     * contains at least 1 whitespace character
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(@Nullable CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code String} contains any whitespace
     * characters.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not empty and contains at
     * least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(@Nullable String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Trim leading and trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * Trim <i>all</i> whitespace from the given {@code String}: leading,
     * trailing, and in between characters.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Trim leading whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Trim all occurrences of the supplied leading character from the given
     * {@code String}.
     *
     * @param str the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given
     * {@code String}.
     *
     * @param str the {@code String} to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Test if the given {@code String} matches the given single character.
     *
     * @param str the {@code String} to check
     * @param singleCharacter the character to compare to
     * @return
     * @since 5.2.9
     */
    public static boolean matchesCharacter(@Nullable String str, char singleCharacter) {
        return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
    }

    /**
     * Test if the given {@code String} starts with the specified prefix,
     * ignoring upper/lower case.
     *
     * @param str the {@code String} to check
     * @param prefix the prefix to look for
     * @return
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length()
                && str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * Test if the given {@code String} ends with the specified suffix, ignoring
     * upper/lower case.
     *
     * @param str the {@code String} to check
     * @param suffix the suffix to look for
     * @return
     * @see java.lang.String#endsWith
     */
    public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length()
                && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
    }

    public static boolean equalsIgnoreCase(@Nullable String input_1, @Nullable String input_2) {
        if (input_1 == null && input_2 == null) {
            return true;
        } else if (input_1 == null && input_2 != null) {
            return false;
        } else if (input_1 != null && input_2 == null) {
            return false;
        } else {
            return input_1 != null && input_1.equalsIgnoreCase(input_2);
        }
    }

    /**
     * Test whether the given string matches the given substring at the given
     * index.
     *
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     * @return
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count the occurrences of the substring {@code sub} in string {@code str}.
     *
     * @param str string to search in
     * @param sub string to search for
     * @return
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (!hasLength(str) || !hasLength(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Replace all occurrences of a substring within a string with another
     * string.
     *
     * @param inString {@code String} to examine
     * @param oldPattern {@code String} to replace
     * @param newPattern {@code String} to insert
     * @return a {@code String} with the replacements
     */
    public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original {@code String}
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting {@code String}
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given {@code String}.
     *
     * @param inString the original {@code String}
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will
     * delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString, @Nullable String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        int lastCharIndex = 0;
        char[] result = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                result[lastCharIndex++] = c;
            }
        }
        if (lastCharIndex == inString.length()) {
            return inString;
        }
        return new String(result, 0, lastCharIndex);
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------
    /**
     * Quote the given {@code String} with single quotes.
     *
     * @param str the input {@code String} (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"), or {@code null} if
     * the input was {@code null}
     */
    @Nullable
    public static String quote(@Nullable String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a {@code String} with single quotes if it is a
     * {@code String}; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"), or the input
     * object as-is if not a {@code String}
     */
    @Nullable
    public static Object quoteIfString(@Nullable Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character.For example,
     * "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     * @return
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character.For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator the separator
     * @return
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a {@code String}, changing the first letter to upper case as
     * per {@link Character#toUpperCase(char)}. No other letters are changed.
     *
     * @param str the {@code String} to capitalize
     * @return the capitalized {@code String}
     */
    public static String capitalize(String str) {
        return changeFirstCharCase(str, true);
    }

    /**
     * Uncapitalize a {@code String}, changing the first letter to lower case as
     * per {@link Character#toLowerCase(char)}. No other letters are changed.
     *
     * @param str the {@code String} to uncapitalize
     * @return the uncapitalized {@code String}
     */
    public static String unCapitalize(String str) {
        return changeFirstCharCase(str, false);
    }

    private static String changeFirstCharCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        } else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars);
    }

    /**
     * Extract the filename from the given Java resource path, e.g.
     * {@code "mypath/myfile.txt" &rarr; "myfile.txt"}.
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename, or {@code null} if none
     */
    @Nullable
    public static String getFilename(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Extract the filename extension from the given Java resource path, e.g.
     * "mypath/myfile.txt" &rarr; "txt".
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename extension, or {@code null} if none
     */
    @Nullable
    public static String getFilenameExtension(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    /**
     * Strip the filename extension from the given Java resource path, e.g.
     * "mypath/myfile.txt" &rarr; "mypath/myfile".
     *
     * @param path the file path
     * @return the path with stripped filename extension
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }

        return path.substring(0, extIndex);
    }

    /**
     * Apply the given relative path to the given Java resource path, assuming
     * standard Java folder separation (i.e. "/" separators).
     *
     * @param path the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file
     * path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and inner
     * simple dots.
     * <p>
     * The result is convenient for path comparison. For other uses, notice that
     * Windows separators ("\") are replaced by simple slashes.
     * <p>
     * <strong>NOTE</strong> that {@code cleanPath} should not be depended upon
     * in a security context. Other mechanisms should be used to prevent
     * path-traversal issues.
     *
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if (!hasLength(path)) {
            return path;
        }

        String normalizedPath = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
        String pathToUse = normalizedPath;

        // Shortcut if there is no work to do
        if (pathToUse.indexOf('.') == -1) {
            return pathToUse;
        }

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(FOLDER_SEPARATOR)) {
                prefix = "";
            } else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        // we never require more elements than pathArray and in the common case the same number
        Deque<String> pathElements = new ArrayDeque<>(pathArray.length);
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            } else if (TOP_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            } else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.addFirst(element);
                }
            }
        }

        // All path elements stayed the same - shortcut
        if (pathArray.length == pathElements.size()) {
            return normalizedPath;
        }
        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.addFirst(TOP_PATH);
        }
        // If nothing else left, at least explicitly point to current path.
        if (pathElements.size() == 1 && pathElements.getLast().isEmpty() && !prefix.endsWith(FOLDER_SEPARATOR)) {
            pathElements.addFirst(CURRENT_PATH);
        }

        final String joined = collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
        // avoid string concatenation with empty prefix
        return prefix.isEmpty() ? joined : prefix + joined;
    }

    /**
     * Compare two paths after normalization of them.
     *
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * Decode the given encoded URI component value. Based on the following
     * rules:
     * <ul>
     * <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"}
     * through {@code "Z"}, and {@code "0"} through {@code "9"} stay the
     * same.</li>
     * <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and
     * {@code "*"} stay the same.</li>
     * <li>A sequence "{@code %<i>xy</i>}" is interpreted as a hexadecimal
     * representation of the character.</li>
     * </ul>
     *
     * @param source the encoded String
     * @param charset the character set
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid
     * encoded sequences
     * @since 5.0
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String uriDecode(String source, Charset charset) {
        int length = source.length();
        if (length == 0) {
            return source;
        }
        if (isEmpty(charset)) {
            return source;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    baos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            } else {
                baos.write(ch);
            }
        }
        return (changed ? StreamUtils.copyToString(baos, charset) : source);
    }

    /**
     * Parse the given {@code String} value into a {@link Locale}, accepting the
     * {@link Locale#toString} format as well as BCP 47 language tags.
     *
     * @param localeValue the locale value: following either {@code Locale's}
     * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
     * separators (as an alternative to underscores), or BCP 47 (e.g. "en-UK")
     * as specified by {@link Locale#forLanguageTag} on Java 7+
     * @return a corresponding {@code Locale} instance, or {@code null} if none
     * @throws IllegalArgumentException in case of an invalid locale
     * specification
     * @since 5.0.4
     * @see #parseLocaleString
     * @see Locale#forLanguageTag
     */
    @Nullable
    public static Locale parseLocale(String localeValue) {
        String[] tokens = tokenizeLocaleSource(localeValue);
        if (tokens.length == 1) {
            validateLocalePart(localeValue);
            Locale resolved = Locale.forLanguageTag(localeValue);
            if (resolved.getLanguage().length() > 0) {
                return resolved;
            }
        }
        return parseLocaleTokens(localeValue, tokens);
    }

    /**
     * Parse the given {@code String} representation into a {@link Locale}.
     * <p>
     * For many parsing scenarios, this is an inverse operation of
     * {@link Locale#toString Locale's toString}, in a lenient sense. This
     * method does not aim for strict {@code Locale} design compliance; it is
     * rather specifically tailored for typical Spring parsing needs.
     * <p>
     * <b>Note: This delegate does not accept the BCP 47 language tag format.
     * Please use {@link #parseLocale} for lenient parsing of both formats.</b>
     *
     * @param localeString the locale {@code String}: following {@code Locale's}
     * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
     * separators (as an alternative to underscores)
     * @return a corresponding {@code Locale} instance, or {@code null} if none
     * @throws IllegalArgumentException in case of an invalid locale
     * specification
     */
    @Nullable
    public static Locale parseLocaleString(String localeString) {
        return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
    }

    private static String[] tokenizeLocaleSource(String localeSource) {
        return tokenizeToStringArray(localeSource, "_ ", false, false);
    }

    @Nullable
    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
        String language = (tokens.length > 0 ? tokens[0] : "");
        String country = (tokens.length > 1 ? tokens[1] : "");
        validateLocalePart(language);
        validateLocalePart(country);

        String variant = "";
        if (tokens.length > 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }

        if (variant.isEmpty() && country.startsWith("#")) {
            variant = country;
            country = "";
        }

        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }

    /**
     * Determine the RFC 3066 compliant language tag, as used for the HTTP
     * "Accept-Language" header.
     *
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as {@code String}
     * @deprecated as of 5.0.4, in favor of {@link Locale#toLanguageTag()}
     */
    @Deprecated
    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    /**
     * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
     *
     * @param timeZoneString the time zone {@code String}, following
     * {@link TimeZone#getTimeZone(String)} but throwing
     * {@link IllegalArgumentException} in case of an invalid time zone
     * specification
     * @return a corresponding {@link TimeZone} instance
     * @throws IllegalArgumentException in case of an invalid time zone
     * specification
     */
    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            // We don't want that GMT fallback...
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
        }
        return timeZone;
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------
    /**
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>
     * The {@code Collection} must contain {@code String} elements only.
     *
     * @param collection the {@code Collection} to copy (potentially
     * {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * Copy the given {@link Enumeration} into a {@code String} array.
     * <p>
     * The {@code Enumeration} must contain {@code String} elements only.
     *
     * @param enumeration the {@code Enumeration} to copy (potentially
     * {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
        return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
    }

    /**
     * Append the given {@code String} to the given {@code String} array,
     * returning a new array consisting of the input array contents plus the
     * given {@code String}.
     *
     * @param array the array to append to (can be {@code null})
     * @param str the {@code String} to append
     * @return the new array (never {@code null})
     */
    public static String[] addStringToArray(@Nullable String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        }

        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate the given {@code String} arrays into one, with overlapping
     * array elements included twice.
     * <p>
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were
     * {@code null})
     */
    @Nullable
    public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }

        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Merge the given {@code String} arrays into one, with overlapping array
     * elements only included once.
     * <p>
     * The order of elements in the original arrays is preserved (with the
     * exception of overlapping elements, which are only included on their first
     * occurrence).
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were
     * {@code null})
     * @deprecated as of 4.3.15, in favor of manual merging via
     * {@link LinkedHashSet} (with every entry included at most once, even
     * entries within the first array)
     */
    @Deprecated
    @Nullable
    public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }

        List<String> result = new ArrayList<>(Arrays.asList(array1));
        for (String str : array2) {
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return toStringArray(result);
    }

    /**
     * Sort the given {@code String} array if necessary.
     *
     * @param array the original array (potentially empty)
     * @return the array in sorted form (never {@code null})
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Arrays.sort(array);
        return array;
    }

    /**
     * Trim the elements of the given {@code String} array, calling
     * {@code String.trim()} on each non-null element.
     *
     * @param array the original {@code String} array (potentially empty)
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * Remove duplicate strings from the given array.
     * <p>
     * As of 4.2, it preserves the original order, as it uses a
     * {@link LinkedHashSet}.
     *
     * @param array the {@code String} array (potentially empty)
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
        return toStringArray(set);
    }

    @Nullable
    public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        return toSplit.split(delimiter);
    }

    /**
     * Take an array of strings and split each element based on the given
     * delimiter. A {@code Properties} instance is then generated, with the left
     * of the delimiter providing the key, and the right of the delimiter
     * providing the value.
     * <p>
     * Will trim both the key and value before adding them to the
     * {@code Properties}.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals
     * symbol)
     * @return a {@code Properties} instance representing the array contents, or
     * {@code null} if the array to process was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Take an array of strings and split each element based on the given
     * delimiter. A {@code Properties} instance is then generated, with the left
     * of the delimiter providing the key, and the right of the delimiter
     * providing the value.
     * <p>
     * Will trim both the key and value before adding them to the
     * {@code Properties} instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals
     * symbol)
     * @param charsToDelete one or more characters to remove from each element
     * prior to attempting the split operation (typically the quotation mark
     * symbol), or {@code null} if no removal should occur
     * @return a {@code Properties} instance representing the array contents, or
     * {@code null} if the array to process was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, @Nullable String charsToDelete) {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }

        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = deleteAny(element, charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link StringTokenizer}.
     * <p>
     * Trims tokens and omits empty tokens.
     * <p>
     * The given {@code delimiters} string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@link #delimitedListToStringArray}.
     *
     * @param str the {@code String} to tokenize (potentially {@code null} or
     * empty)
     * @param delimiters the delimiter characters, assembled as a {@code String}
     * (each of the characters is individually considered as a delimiter)
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link StringTokenizer}.
     * <p>
     * The given {@code delimiters} string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@link #delimitedListToStringArray}.
     *
     * @param str the {@code String} to tokenize (potentially {@code null} or
     * empty)
     * @param delimiters the delimiter characters, assembled as a {@code String}
     * (each of the characters is individually considered as a delimiter)
     * @param trimTokens trim the tokens via {@link String#trim()}
     * @param ignoreEmptyTokens omit empty tokens from the result array (only
     * applies to tokens that are empty after trimming; StringTokenizer will not
     * consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            @Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a
     * {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character, but it
     * will still be considered as a single delimiter string, rather than as
     * bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     *
     * @param str the input {@code String} (potentially {@code null} or empty)
     * @param delimiter the delimiter between elements (this is a single
     * delimiter, rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a
     * {@code String} array.
     * <p>
     * A single {@code delimiter} may consist of more than one character, but it
     * will still be considered as a single delimiter string, rather than as
     * bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     *
     * @param str the input {@code String} (potentially {@code null} or empty)
     * @param delimiter the delimiter between elements (this is a single
     * delimiter, rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete; useful for deleting
     * unwanted line breaks: e.g. "\r\n\f" will delete all new lines and line
     * feeds in a {@code String}
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(
            @Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }
        if (delimiter == null) {
            return new String[]{str};
        }

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into an
     * array of strings.
     *
     * @param str the input {@code String} (potentially {@code null} or empty)
     * @return an array of strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(@Nullable String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into a set.
     * <p>
     * Note that this will suppress duplicates, and as of 4.2, the elements in
     * the returned set will preserve the original order in a
     * {@link LinkedHashSet}.
     *
     * @param str the input {@code String} (potentially {@code null} or empty)
     * @return a set of {@code String} entries in the list
     * @see #removeDuplicateStrings(String[])
     */
    public static Set<String> commaDelimitedListToSet(@Nullable String str) {
        String[] tokens = commaDelimitedListToStringArray(str);
        return new LinkedHashSet<>(Arrays.asList(tokens));
    }

    /**
     * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert (potentially {@code null}
     * or empty)
     * @param delim the delimiter to use (typically a ",")
     * @param prefix the {@code String} to start each element with
     * @param suffix the {@code String} to end each element with
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(
            @Nullable Collection<?> coll, String delim, String prefix, String suffix) {

        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }

        int totalLength = coll.size() * (prefix.length() + suffix.length()) + (coll.size() - 1) * delim.length();
        for (Object element : coll) {
            totalLength += String.valueOf(element).length();
        }

        StringBuilder sb = new StringBuilder(totalLength);
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert (potentially {@code null}
     * or empty)
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert (potentially {@code null}
     * or empty)
     * @return the delimited {@code String}
     */
    public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g.
     * CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param arr the array to display (potentially {@code null} or empty)
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        }

        StringJoiner sj = new StringJoiner(delim);
        for (Object elem : arr) {
            sj.add(String.valueOf(elem));
        }
        return sj.toString();
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String}
     * (i.e., CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param arr the array to display (potentially {@code null} or empty)
     * @return the delimited {@code String}
     */
    public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

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

    public static String intToNDigit(int value, int digitLength) {
        StringBuilder sBuf = new StringBuilder(String.valueOf(value));
        while (sBuf.length() < digitLength) {
            sBuf.insert(0, "0");
        }
        return sBuf.toString();
    }

    public static String getLongTimeString() {
        String str = "";
        long time = new Date().getTime();
        str += time;
        return str;
    }

    public static String trimString(String input, int lenght) {
        String SPECIAL_CHARACTOR = "[ .-/]";
        String str = "";
        if (input == null) {
            return str;
        } else {
            StringTokenizer tokenSpace = new StringTokenizer(input, SPECIAL_CHARACTOR);
            while (tokenSpace.hasMoreTokens()) {
                String tempStr = tokenSpace.nextToken();
                if ((str + tempStr).length() < lenght) {
                    str += tempStr + " ";
                } else {
                    str += "...";
                    break;
                }
            }

            return str.trim();
        }
    }

    public static String replaceString(String sStr, String oldStr, String newStr) {
        sStr = (sStr == null ? "" : sStr);
        String strVar = sStr;
        String tmpStr;
        String finalStr = "";
        int strLen = strVar.length();
        while (true) {
            var stpos = 0;
            var endpos = strVar.indexOf(oldStr, stpos);
            if (endpos == -1) {
                break;
            }
            tmpStr = strVar.substring(stpos, endpos);
            tmpStr = tmpStr.concat(newStr);
            strVar = strVar.substring(endpos + oldStr.length() > sStr.length() ? endpos : endpos + oldStr.length(), strLen);
            finalStr = finalStr.concat(tmpStr);
        }
        finalStr = finalStr.concat(strVar);
        return finalStr;
    }

    public static String null2Empty(String input) {
        if (input != null) {
            input = input.trim();
        } else {
            input = "";
        }
        return input;
    }

    public static String stringToHTMLString(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                switch (c) {
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    // Handle Newline
                    case '\n':
                        sb.append("&lt;br/&gt;");
                        break;
                    default:
                        int ci = 0xffff & c;
                        if (ci < 160) // nothing special only 7 Bit
                        {
                            sb.append(c);
                        } else {
                            // Not 7 Bit use the unicode system
                            sb.append("&#");
                            sb.append(Integer.toString(ci));
                            sb.append(';');
                        }
                        break;
                }
            }
        }
        return sb.toString();
    }

    public static String convert2NoSign(String org) {
        if (org == null) {
            org = "";
            return org;
        }
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                case (char) 160: {
                    result[i] = ' ';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        String tem = new String(result);
        char[] charArray = tem.toCharArray();
        String output = "";
        for (int i = 0; i < charArray.length; ++i) {
            char a = charArray[i];
            if ((int) a > 255) {
//                    output += "&#" + (int) a + ";";
            } else {
                output += a;
            }
        }
        return output;
    }

    public static String random5Char(int lenght) {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
        String pw = "";
        for (int i = 0; i < lenght; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    public static String getRandomString(int length) {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        String pw = "";
        for (int i = 0; i < length; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    public static String validStringJs(String input) {
        if (input == null) {
            return "";
        } else {
            input = input.replaceAll("/", "\\\\/");
            input = input.replaceAll("\"", "\\\\\"");
            input = input.replaceAll("\n", "");
            input = input.replaceAll("\r", "");
            return input;
        }
    }

    public static String StringOneLine(String input) {
        if (input == null) {
            return "";
        } else {
            input = input.trim();
            String NL = System.getProperty("line.separator");
            input = input.replaceAll("\n", "");
            input = input.replaceAll("\r", "");
            input = input.replaceAll(NL, "");
            return input;
        }
    }

    public static String str2OneLine(String input) {
        String cleaned = "";
        if (!isEmpty(input)) {
            cleaned = input.replaceAll("\\s*[\\r\\n]+\\s*", "").trim();
        }
        return cleaned;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try (is) {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static String removeEndCharactor(String str, String charactor) {
        if (!isEmpty(charactor) && !isEmpty(str)) {
            if (str.endsWith(charactor)) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    public static String randomUDID() {
        return UUID.randomUUID().toString();
    }

    public static String getStringAlt(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.trim();
        input = input.replaceAll("&amp;", "");
        input = input.replaceAll("'", "");
        input = input.replaceAll(":", "");
        input = input.replaceAll(",", "");
        input = input.replaceAll("\\.", "");
        input = input.replaceAll("‘", "");
        input = input.replaceAll("“", "");
        input = input.replaceAll("”", "");
        input = input.replaceAll("\\?", "");
        input = input.replaceAll("~", "");
        input = input.replaceAll("!", "");
        input = input.replaceAll("@", "");
        input = input.replaceAll("#", "");
        input = input.replaceAll("\\$", "");
        input = input.replaceAll("%", "");
        input = input.replaceAll("\\^", "");
        input = input.replaceAll("&", "");
        input = input.replaceAll("\\*", "");
        input = input.replaceAll("\\(", "");
        input = input.replaceAll("\\)", "");
        input = input.replaceAll("\"", "");
        input = input.replaceAll(" ", "-");
        while (input.contains("--")) {
            input = input.replaceAll("--", "-");
        }
        return input;
    }

    public static String getStringURL(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.trim();
        input = convert2NoSign(input);
        input = input.replaceAll("&amp;", "");
        input = input.replaceAll("amp;", "");
        input = input.replaceAll("&quot;", "");
        input = input.replaceAll("quot;", "");
        input = input.replaceAll("&apos;", "");
        input = input.replaceAll("apos;", "");
        input = input.replaceAll("&lt;", "");
        input = input.replaceAll("lt;", "");
        input = input.replaceAll("&gt;", "");
        input = input.replaceAll("gt;", "");
        input = input.replaceAll("&amp;", "");
        input = input.replaceAll("&AMP;", "");
        input = input.replaceAll("'", "");
        input = input.replaceAll(":", "");
        input = input.replaceAll(",", "");
        input = input.replaceAll("\\.", "");
        input = input.replaceAll("‘", "");
        input = input.replaceAll("“", "");
        input = input.replaceAll("”", "");
        input = input.replaceAll("\\?", "");
        input = input.replaceAll("~", "");
        input = input.replaceAll("!", "");
        input = input.replaceAll("@", "");
        input = input.replaceAll("#", "");
        input = input.replaceAll("\\$", "");
        input = input.replaceAll("%", "");
        input = input.replaceAll("\\^", "");
        input = input.replaceAll("&", "");
        input = input.replaceAll("…", "");
        input = input.replaceAll("\\*", "");
        input = input.replaceAll("\\(", "");
        input = input.replaceAll("\\)", "");
        input = input.replaceAll("\"", "");
        input = input.replaceAll("\'", "");
        input = input.replaceAll(" ", "-");
        input = input.replaceAll("/", "-");
        while (input.contains("--")) {
            input = input.replaceAll("--", "-");
        }
        input = input.toLowerCase();
        return input;
    }

    public static String arrTagAscii(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        input = input.trim();
        input = convert2NoSign(input);
        input = input.replaceAll("&amp;", "");
        input = input.replaceAll("amp;", "");
        input = input.replaceAll("&quot;", "");
        input = input.replaceAll("quot;", "");
        input = input.replaceAll("&apos;", "");
        input = input.replaceAll("apos;", "");
        input = input.replaceAll("&lt;", "");
        input = input.replaceAll("lt;", "");
        input = input.replaceAll("&gt;", "");
        input = input.replaceAll("gt;", "");
        input = input.replaceAll("&amp;", "");
        input = input.replaceAll("&AMP;", "");
        input = input.replaceAll("'", "");
        input = input.replaceAll(":", "");
//        input = input.replaceAll(",", "");
        input = input.replaceAll("\\.", "");
        input = input.replaceAll("‘", "");
        input = input.replaceAll("“", "");
        input = input.replaceAll("”", "");
        input = input.replaceAll("\\?", "");
        input = input.replaceAll("~", "");
        input = input.replaceAll("!", "");
        input = input.replaceAll("@", "");
        input = input.replaceAll("#", "");
        input = input.replaceAll("\\$", "");
        input = input.replaceAll("%", "");
        input = input.replaceAll("\\^", "");
        input = input.replaceAll("&", "");
        input = input.replaceAll("…", "");
        input = input.replaceAll("\\*", "");
        input = input.replaceAll("\\(", "");
        input = input.replaceAll("\\)", "");
        input = input.replaceAll("\"", "");
        input = input.replaceAll("\'", "");
        input = input.replaceAll(" ", "-");
        input = input.replaceAll("/", "-");
        while (input.contains("--")) {
            input = input.replaceAll("--", "-");
        }
        input = replaceString(input, ",-", ",");
//        String[] tags = input.split(",");
//        if (tags != null && tags.length > 0) {
//            int i = 1;
//            input = "";
//            for (String oneTag : tags) {
//                if (oneTag.startsWith("-")) {
//                    oneTag = oneTag.substring(1);
//                }
//                input += oneTag;
//                if (i != tags.length) {
//                    input += ",";
//                }
//            }
//        }
        input = input.toLowerCase();
        return input;
    }
}
