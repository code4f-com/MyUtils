/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.date;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author tuanpla
 */
public class Today {

    private int dd;
    private int mm;
    private int yyyy;
    private int yy;
    private int hh;
    private int mi;
    private int sec;
    private String dayOfWeek;

    public Today() {
        Calendar cal = Calendar.getInstance();
        init(cal);
    }

    public Today(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        init(cal);
    }

    public Today(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        init(cal);
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDay() {
        return dd;
    }

    public int getMonth() {
        return mm;
    }

    public String getMonthMM() {
        if (mm < 10) {
            return "0" + mm;
        }
        return mm + "";
    }

    public int getYear() {
        return yyyy;
    }

    public String getYear_yy() {
        if (yy < 10) {
            return "0" + yy;
        }
        return yy + "";
    }

    public int getHour() {
        return hh;
    }

    public int getMinute() {
        return mi;
    }

    public int getSecond() {
        return sec;
    }

    private void init(Calendar cal) {
        int d = cal.get(Calendar.DAY_OF_WEEK);
        switch (d) {
            case 2: // '\002'
                dayOfWeek = "Thu Hai";
                break;

            case 3: // '\003'
                dayOfWeek = "Thu Ba";
                break;

            case 4: // '\004'
                dayOfWeek = "Thu Tu";
                break;

            case 5: // '\005'
                dayOfWeek = "Thu Nam";
                break;

            case 6: // '\006'
                dayOfWeek = "Thu Sau";
                break;

            case 7: // '\007'
                dayOfWeek = "Thu Bay";
                break;

            case 1: // '\001'
                dayOfWeek = "Chu Nhat";
                break;

            default:
                dayOfWeek = "";
                break;
        }
        dd = cal.get(Calendar.DAY_OF_MONTH);
        mm = cal.get(Calendar.MONTH) + 1;
        yyyy = cal.get(Calendar.YEAR);
        yy = yyyy % 100;
        hh = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
    }

    public static final Today getInstance() {
        return new Today();
    }

}
