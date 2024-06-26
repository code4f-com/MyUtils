package com.tuanpla.utils.common;

import java.util.Date;
import java.util.List;

public abstract class Compare {

    public static boolean lessThan(Character character, Character character1) {
        return character < character1;
    }

    public static boolean lessThan(Number number, Number number1) {
        if (number == null || number1 == null) {
            throw new NullPointerException("number or number1 is null");
        }
        if (number instanceof Byte) {
            if (number.byteValue() < number1.byteValue()) {
                return true;
            }
        } else if (number instanceof Short) {
            if (number.shortValue() < number1.shortValue()) {
                return true;
            }
        } else if (number instanceof Integer) {
            if (number.intValue() < number1.intValue()) {
                return true;
            }
        } else if (number instanceof Long) {
            if (number.longValue() < number1.longValue()) {
                return true;
            }
        } else if (number instanceof Float) {
            if (number.floatValue() < number1.floatValue()) {
                return true;
            }
        } else if (number instanceof Double) {
            if (number.doubleValue() < number1.doubleValue()) {
                return true;
            }
        }
        return false;
    }

    public static boolean lessThan(Object obj, Object obj1) {
        if (obj1 == null) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        if (obj instanceof Number) {
            return lessThan((Number) obj, (Number) obj1);
        }
        if (obj instanceof Date) {
            return lessThan((Date) obj, (Date) obj1);
        }
        if (obj instanceof String) {
            return lessThan((String) obj, (String) obj1);
        } else {
            return lessThan(obj.toString(), obj1.toString());
        }
    }

    public static boolean lessThan(String s, String s1) {
        if (s1.length() == 0) {
            return false;
        }
        if (s.length() == 0) {
            return true;
        }
        if (s.charAt(0) < s1.charAt(0)) {
            return true;
        }
        if (s.charAt(0) > s1.charAt(0)) {
            return false;
        }
        if (s1.length() == 1) {
            return false;
        }
        if (s.length() == 1) {
            return true;
        } else {
            return lessThan(s.substring(1), s1.substring(1));
        }
    }

    public static boolean lessThan(Date date, Date date1) {
        return date.before(date1);
    }

    public static void swap(List input, int posI, int posJ) {
        if (input == null || input.isEmpty()) {
            return;
        }
        if (posI > input.size() - 1 || posJ > input.size() - 1) {
            return;
        }
        Object obj = input.get(posI);
        input.set(posI, input.get(posJ));
        input.set(posJ, obj);
    }

}
