/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.date;

import com.tuanpla.utils.common.Nullable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
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
    private int weekOfYear;
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

    public Today(long timeLong) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeLong);
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

    public int getWeekOfYear() {
        return weekOfYear;
    }

    private void init(Calendar cal) {
        int d = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = switch (d) {
            case 2 ->
                "Thu Hai";
            case 3 ->
                "Thu Ba";
            case 4 ->
                "Thu Tu";
            case 5 ->
                "Thu Nam";
            case 6 ->
                "Thu Sau";
            case 7 ->
                "Thu Bay";
            case 1 ->
                "Chu Nhat";
            default ->
                "";
        }; // '\002'
        // '\003'
        // '\004'
        // '\005'
        // '\006'
        // '\007'
        // '\001'
        dd = cal.get(Calendar.DAY_OF_MONTH);
        mm = cal.get(Calendar.MONTH) + 1;
        yyyy = cal.get(Calendar.YEAR);
        yy = yyyy % 100;
        hh = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
        LocalDate ld = LocalDate.of(yyyy, mm, dd);
        weekOfYear = ld.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    public static final Today getInstance() {
        return new Today();
    }

    public long startOf() {
        String startTime = getDay() + "/" + getMonthMM() + "/" + getYear() + " 00:00:00";
        Date d = DateProc.string2Date(startTime, "dd/MM/yyyy HH:mm:ss");
        return d.getTime();
    }

    public static long startTime() {
        Today td = getInstance();
        String startTime = td.getDay() + "/" + td.getMonthMM() + "/" + td.getYear() + " 00:00:00";
        Date d = DateProc.string2Date(startTime, "dd/MM/yyyy HH:mm:ss");
        return d.getTime();
    }

    public static long endTime() {
        Today td = getInstance();
        String endTime = td.getDay() + "/" + td.getMonthMM() + "/" + td.getYear() + " 23:59:59";
        Date d = DateProc.string2Date(endTime, "dd/MM/yyyy HH:mm:ss");
        return d.getTime();
    }

    public long endOf() {
        String endTime = getDay() + "/" + getMonthMM() + "/" + getYear() + " 23:59:59";
        Date d = DateProc.string2Date(endTime, "dd/MM/yyyy HH:mm:ss");
        return d.getTime();
    }

    public static String buildDateStr(@Nullable Long date) {
        String str = "";
        try {
            Today td = new Today(date);
            str += (td.getHour() > 9) ? td.getHour() : "0" + td.getHour();
            str += ":";
            str += (td.getMinute() > 9) ? td.getMinute() : "0" + td.getHour();
            str += " Ngày " + td.getDay() + " tháng " + td.getMonthMM() + " năm " + td.getYear();
        } catch (Exception e) {
        }
        return str;
    }
}
