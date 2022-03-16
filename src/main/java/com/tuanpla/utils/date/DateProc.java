/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.date;

import com.tuanpla.utils.logging.LogUtils;
import java.sql.Date;       // extends java.util.Date
import java.sql.Time;
import java.sql.Timestamp;  // extends java.util.Date
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author tuanpla
 */
public class DateProc {

    /*
        http://tutorials.jenkov.com/java-date-time/parsing-formatting-dates.html
        java.sql.Date = (yyyy-MM-dd) not have time
        java.sql.Timestamp is full
        y   = year   (yy or yyyy)
        M   = month  (MM)
        d   = day in month (dd)
        h   = hour (0-12)  (hh)
        H   = hour (0-23)  (HH)
        m   = minute in hour (mm)
        s   = seconds (ss)
        S   = milliseconds (SSS)
        z   = time zone  text        (e.g. Pacific Standard Time...)
        Z   = time zone, time offset (e.g. -0800)
     */
    public DateProc() {
    }

    public static int currentYear() {
        Today d = new Today();
        return d.getYear();
    }

    public static int backYear(int num) {
        Today d = new Today();
        return d.getYear() - num;
    }

    public static double getTimer() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        double realTime = (double) h + (double) m / 60;
        return realTime;
    }

    public static double string2Time(String time) {
        String aTime[] = time.split(":");
        return Double.parseDouble(aTime[0]) + Double.parseDouble(aTime[1]) / 60;
    }

    /**
     * Require format don't have time format
     *
     * @param fmOut
     * @return
     */
    public static String currentDate(String fmOut) {
        return date2String(currentDate(), fmOut);
    }

    public static String currentDateTime(String fmOut) {
        return timestamp2String(currentTimestamp(), fmOut);
    }

    public static String currentddMMyyyy_Start01() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String strTemp = "01";
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            return strTemp + "/0" + (calendar.get(Calendar.MONTH) + 1)
                    + "/" + calendar.get(Calendar.YEAR);
        } else {
            return strTemp + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                    + calendar.get(Calendar.YEAR);
        }
    }

    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Date string2Date(String strInputDate, String forMat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            Date d = new Date(dateFormat.parse(strInputDate).getTime());
            return d;
        } catch (ParseException ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static String date2String(Date d, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(d);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return "";
        }
    }

    public static Date int2Date(Integer input, String fminput) {
        try {
            String strInputDate = String.valueOf(input);
            SimpleDateFormat dateFormat = new SimpleDateFormat(fminput);
            Date d = new Date(dateFormat.parse(strInputDate).getTime());
            return d;
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Integer date2Int(Date date, String fmOut) {
        try {
            String strDate = date2String(date, fmOut);
            return Integer.parseInt(strDate);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Integer currentInt(String fmOut) {
        Date d = DateProc.currentDate();
        String crdStr = date2String(d, fmOut);
        try {
            return Integer.parseInt(crdStr);
        } catch (Exception e) {
            LogUtils.debug("Error: " + e.toString());
            return null;
        }
    }

    public static Integer stringDate2Int(String input, String fminput, String fmOut) {
        try {
            Date d = string2Date(input, fminput);
            String strDate = date2String(d, fmOut);
            return Integer.parseInt(strDate);
        } catch (Exception e) {
            LogUtils.debug("Error: " + e.toString());
            return null;
        }
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp date2Timestamp(Date date) {
        return date != null ? new Timestamp(date.getTime()) : null;
    }

    public static Timestamp Timestamp2_0h0m0s0n(Timestamp ts) {
        if (ts == null) {
            return ts;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(ts);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    public static Timestamp string2Timestamp(String strInputDate, String forMat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            Timestamp d = new Timestamp(dateFormat.parse(strInputDate).getTime());
            return d;
        } catch (ParseException ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static String timestamp2String(Timestamp ts, String fmOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmOut);
            return dateFormat.format(ts);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return "";
        }
    }

    public static Timestamp long2Timestamp(Long input, String forMat) {
        try {
            String strInputDate = String.valueOf(input);
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            Timestamp d = new Timestamp(dateFormat.parse(strInputDate).getTime());
            return d;
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Long timestamp2Long(Timestamp ts, String fmOut) {
        try {
            String strTimestamp = timestamp2String(ts, fmOut);
            return Long.parseLong(strTimestamp);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Long currentLong(String fmOut) {
        Timestamp ts = currentTimestamp();
        String crdStr = timestamp2String(ts, fmOut);
        try {
            return Long.parseLong(crdStr);
        } catch (Exception e) {
            LogUtils.debug("Error: " + e.toString());
            return null;
        }
    }

    public static String timeString(Time ti, String fmOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmOut);
            return dateFormat.format(ti);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return "";
        }
    }

    /**
     * The same timestamp2String(Timestamp ts, String fmOut) use time format
     *
     * @param ts
     * @param fmOut
     * @return
     */
    public static String extractTime(Timestamp ts, String fmOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmOut);
            return dateFormat.format(ts);
        } catch (Exception ex) {
            LogUtils.debug("Error: " + ex.toString());
            return "";
        }
    }

    public static String getDisplayTimeNews(Timestamp newsTime) {
        try {
            Calendar current = Calendar.getInstance();
            Calendar input = Calendar.getInstance();
            if (newsTime == null) {
                newsTime = DateProc.currentTimestamp();
            }
            input.setTime(newsTime);
            if (input.get(Calendar.YEAR) < current.get(Calendar.YEAR)) {
                return DateProc.timestamp2String(newsTime, "dd/MM/yyyy HH:mm");
            } else if (input.get(Calendar.MONTH) < current.get(Calendar.MONTH)) {
                return DateProc.timestamp2String(newsTime, "dd/MM/yyyy HH:mm");
            } else if (input.get(Calendar.DAY_OF_MONTH) < current.get(Calendar.DAY_OF_MONTH)) {
                return DateProc.timestamp2String(newsTime, "dd/MM/yyyy HH:mm");
            } else if (current.get(Calendar.HOUR_OF_DAY) - input.get(Calendar.HOUR_OF_DAY) >= 5) {
                return DateProc.timestamp2String(newsTime, "dd/MM/yyyy HH:mm");
            } else if (current.get(Calendar.HOUR_OF_DAY) - input.get(Calendar.HOUR_OF_DAY) > 0 && current.get(Calendar.HOUR_OF_DAY) - input.get(Calendar.HOUR_OF_DAY) < 5) {
                return (current.get(Calendar.HOUR_OF_DAY) - input.get(Calendar.HOUR_OF_DAY)) + " giờ trước";
            } else {
                return (current.get(Calendar.MINUTE) - input.get(Calendar.MINUTE)) + " phút trước";
            }
        } catch (Exception e) {
            LogUtils.debug("Error: " + e.toString());
            return "";
        }
    }

    /**
     *
     * @param ts Timestapm to convert
     * @param iStyle 0: 24h, otherwise 12h clock
     * @return
     */
    public static String Timestamp2HHmmssAMPM(Timestamp ts, int iStyle) {
        if (ts == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);

        String strTemp;
        if (iStyle == 0) {
            strTemp = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        } else {
            strTemp = Integer.toString(calendar.get(Calendar.HOUR));
        }

        if (strTemp.length() < 2) {
            strTemp = "0" + strTemp;
        }
        if (calendar.get(Calendar.MINUTE) < 10) {
            strTemp += ":0" + calendar.get(Calendar.MINUTE);
        } else {
            strTemp += ":" + calendar.get(Calendar.MINUTE);
        }
        if (calendar.get(Calendar.SECOND) < 10) {
            strTemp += ":0" + calendar.get(Calendar.SECOND);
        } else {
            strTemp += ":" + calendar.get(Calendar.SECOND);
        }

        if (iStyle != 0) {
            if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                strTemp += " AM";
            } else {
                strTemp += " PM";
            }
        }
        return strTemp;
    }

    public static Timestamp getPreviousDate(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, iDay - 1);

        Timestamp tsNew = new Timestamp(calendar.getTimeInMillis());
        return tsNew;
    }

    public static Date getNextDate(Date d) {
        if (d == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int iDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, iDay + 1);

        Date tsNew = new Date((calendar.getTime()).getTime());
        return tsNew;
    }

    public static int getDayOfWeek(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iDay = calendar.get(Calendar.DAY_OF_WEEK);
        return iDay;
    }

    public static int getDay(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iDay = calendar.get(Calendar.DAY_OF_MONTH);
        return iDay;
    }

    public static int getMonth(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iMonth = calendar.get(Calendar.MONTH);
        return iMonth + 1;
    }

    public static int getYear(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iYear = calendar.get(Calendar.YEAR);
        return iYear;
    }

    public static int getHour(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iHour = calendar.get(Calendar.HOUR);
        return iHour;
    }

    public static int getMinute(Timestamp ts) {
        if (ts == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        int iMinute = calendar.get(Calendar.MINUTE);
        return iMinute;
    }

    public static boolean afTerCurrent(Timestamp timeComp) {
        if (timeComp == null) {
            return false;
        }
        return timeComp.after(currentTimestamp());
    }

    public static boolean beforeCurrent(Timestamp timeComp) {
        if (timeComp == null) {
            return false;
        }
        return timeComp.before(currentTimestamp());
    }

    ////////////////////// ADDED ///////////////////////////////////////////
    /**
     * return the dd/mm/yyyy of current month eg: 05/2002 --> 31/05/2002
     *
     * @param strMonthYear : input string mm/yyyy
     * @return
     */
    public static String getLastestDateOfMonth(String strMonthYear) {

        Date d = string2Date(strMonthYear, "MM/yyyy");

        LogUtils.debug("d:" + d);
        String strDate = strMonthYear;
        int i, nYear, nMonth, nDay;
        String strSub = null;

        i = strDate.indexOf("/");
        if (i < 0) {
            return "";
        }
        strSub = strDate.substring(0, i);
        nMonth = (new Integer(strSub)); // Month begin from 0 value
        strDate = strDate.substring(i + 1);
        nYear = (new Integer(strDate));
        LogUtils.debug("nYear:" + nYear);
        boolean leapyear = false;
        if (nYear % 100 == 0) {
            if (nYear % 400 == 0) {
                leapyear = true;
            }
        } else if ((nYear % 4) == 0) {
            leapyear = true;
        }

        if (nMonth == 2) {
            if (leapyear) {
                return "29/" + nMonth + "/" + strDate;
            } else {
                return "28/" + nMonth + "/" + strDate;
            }
        } else {
            if ((nMonth == 1) || (nMonth == 3) || (nMonth == 5) || (nMonth == 7)
                    || (nMonth == 8) || (nMonth == 10) || (nMonth == 12)) {
                return "31/" + nMonth + "/" + strDate;
            } else if ((nMonth == 4) || (nMonth == 6) || (nMonth == 9)
                    || (nMonth == 11)) {
                return "30/" + nMonth + "/" + strDate;
            }
        }
        return "";
    }

    public static int distanceDate(Date start, Date end) {
        int result = 0;
        try {
            long longStart = start.getTime();
            long longEnd = end.getTime();
            long distance = longEnd - longStart;
            result = (int) distance / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
        }
        return result;
    }

    public static int distanceDate(Date start) {
        int result = 0;
        try {
            long longStart = start.getTime();
//            LogUtils.debug( start + ":" + longStart);
            long longEnd = currentDate().getTime();
//            LogUtils.debug( currentDate() + ":" + longEnd);
            long distance = longEnd - longStart;
//            LogUtils.debug( distance);
            result = (int) (distance / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
        }
        return result;
    }

    public static Timestamp getFriday(Timestamp ts) {
        if (ts == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        int iDoW = getDayOfWeek(ts);
        if (iDoW == Calendar.SUNDAY) {
            iDoW = 8;
        }
        int k = Calendar.FRIDAY - iDoW;
        calendar.setTime(ts);
        int iDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, iDay + k);
        Timestamp tsNew = new Timestamp((calendar.getTime()).getTime());
        return tsNew;
    }

    public static boolean isFriday(Timestamp ts) {
        if (ts == null) {
            return false;
        }
        if (getDayOfWeek(ts) == Calendar.FRIDAY) {
            return true;
        }
        return false;
    }

    public static Timestamp getNextDateN(Timestamp ts, int n) {
        if (ts == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        Timestamp tsNew = new Timestamp(calendar.getTimeInMillis());
        return tsNew;
    }

    public static long nowToLong() {
        String str = getDateTimeForName();
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getDateTimeForName() {
        Calendar calendar = Calendar.getInstance();
        Timestamp ts = new Timestamp(calendar.getTimeInMillis());
        String str = ts.toString();
        str = str.replaceAll("-", "");
        str = str.replaceAll(":", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\\.", "");
        return str;
    }

    public static String extractInfo(Date date) {
        String dayOfWeek;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int d = cal.get(7);
        switch (d) {
            case 2: // '\002'
                dayOfWeek = "Thứ Hai";
                break;

            case 3: // '\003'
                dayOfWeek = "Thứ Ba";
                break;

            case 4: // '\004'
                dayOfWeek = "Thứ Tư";
                break;

            case 5: // '\005'
                dayOfWeek = "Thứ Năm";
                break;

            case 6: // '\006'
                dayOfWeek = "Thứ Sáu";
                break;

            case 7: // '\007'
                dayOfWeek = "Thứ Bẩy";
                break;

            case 1: // '\001'
                dayOfWeek = "Chủ Nhật";
                break;
            default:
                dayOfWeek = "";
                break;
        }
        return dayOfWeek;
    }

    public static String getCurrentTimestampString() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        String a = (hour < 12) ? " AM" : " PM";
        return (new Timestamp(calendar.getTimeInMillis())).toString() + a;
    }

}
