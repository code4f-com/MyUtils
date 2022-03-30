/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.date;

import com.tuanpla.utils.common.Convert;
import com.tuanpla.utils.exception.CustomerException;
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
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static int backYear(int num) {
        Calendar cal = Calendar.getInstance();
        var yyyy = cal.get(Calendar.YEAR);
        return yyyy - num;
    }

    public static double getTimer() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        double h = c.get(Calendar.HOUR_OF_DAY);
        double m = c.get(Calendar.MINUTE);
        double realTime = h + m / 60;
        return realTime;
    }

    /**
     *
     * @param time is for mat hh:mm or HH:mm
     * @return timer is double from 0 to 23.99
     * @throws CustomerException
     */
    public static double string2Timer(String time) throws CustomerException {
        String aTime[] = time.split(":");
        var h = Convert.string2Double(aTime[0], -1);
        var m = Convert.string2Double(aTime[1], -1);
        if (h < 0 || h > 23) {
            throw new CustomerException("hour invalid");
        }
        if (m < 0 || m > 59) {
            throw new CustomerException("minus invalid");
        }
        return h + m / 60;
    }

    /**
     * Require format dd/MM/yyyy hh:mm:ss <br>
     * If you want get only date used format: <b>dd/MM/yyyy</b>
     *
     * @param fmOut
     * @return
     */
    public static String currentDate(String fmOut) {
        return date2String(currentDate(), fmOut);
    }

    public static String currentddMMyyyy_Start01() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        var m = calendar.get(Calendar.MONTH);
        var y = calendar.get(Calendar.YEAR);
        if (m + 1 < 10) {
            return "01/0" + (m + 1) + "/" + y;
        } else {
            return "01/" + (m + 1) + "/" + y;
        }
    }

    public static Date string2Date(String strDate, String forMat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            return new Date(dateFormat.parse(strDate).getTime());
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

    /**
     *
     * @param input is number date Ex: 22/03/2022 to Integer: 22032022 =>
     * <b>fmIntDate</b> is ddMMyyyy
     * @param fmIntDate
     * @return
     */
    public static Date int2Date(Integer input, String fmIntDate) {
        String strDate = String.valueOf(input);
        return string2Date(strDate, fmIntDate);
    }

    /**
     *
     * @param date is number date Ex: 2022-03-22 to Integer: 20220322 =>
     * <b>fmOut</b> is yyyyMMdd
     * @param fmOut
     * @return
     */
    public static Integer date2Int(Date date, String fmOut) {
        try {
            String strDate = date2String(date, fmOut);
            return Integer.parseInt(strDate);
        } catch (NumberFormatException ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Integer currentInt(String fmOut) {
        try {
            String crdStr = date2String(currentDate(), fmOut);
            return Integer.parseInt(crdStr);
        } catch (NumberFormatException e) {
            LogUtils.debug("Error: " + e.toString());
            return null;
        }
    }

    public static Integer stringDate2Int(String input, String fmInput, String fmOut) {
        try {
            String strDate = date2String(string2Date(input, fmInput), fmOut);
            return Integer.parseInt(strDate);
        } catch (NumberFormatException e) {
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

    public static Timestamp string2Timestamp(String strDate, String inFormat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(inFormat);
            return new Timestamp(dateFormat.parse(strDate).getTime());
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
            return new Timestamp(dateFormat.parse(strInputDate).getTime());
        } catch (ParseException ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Long timestamp2Long(Timestamp ts, String fmOut) {
        try {
            String strTimestamp = timestamp2String(ts, fmOut);
            return Long.parseLong(strTimestamp);
        } catch (NumberFormatException ex) {
            LogUtils.debug("Error: " + ex.toString());
            return null;
        }
    }

    public static Long current2Long(String fmOut) {
        try {
            String crdStr = timestamp2String(currentTimestamp(), fmOut);
            return Long.parseLong(crdStr);
        } catch (NumberFormatException e) {
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

    public static String getTimeNews(Timestamp newsTime) {
        try {
            Calendar current = Calendar.getInstance();
            //
            if (newsTime == null) {
                newsTime = currentTimestamp();
            }
            //
            Calendar cl = Calendar.getInstance();
            cl.setTime(newsTime);
            if (cl.get(Calendar.YEAR) < current.get(Calendar.YEAR)
                    || cl.get(Calendar.MONTH) < current.get(Calendar.MONTH)
                    || cl.get(Calendar.DAY_OF_MONTH) < current.get(Calendar.DAY_OF_MONTH)
                    || current.get(Calendar.HOUR_OF_DAY) - cl.get(Calendar.HOUR_OF_DAY) >= 5) {
                return DateProc.timestamp2String(newsTime, "dd/MM/yyyy HH:mm");
            } else if (current.get(Calendar.HOUR_OF_DAY) - cl.get(Calendar.HOUR_OF_DAY) > 0
                    && current.get(Calendar.HOUR_OF_DAY) - cl.get(Calendar.HOUR_OF_DAY) < 5) {
                return (current.get(Calendar.HOUR_OF_DAY) - cl.get(Calendar.HOUR_OF_DAY)) + " giờ trước";
            } else {
                return (current.get(Calendar.MINUTE) - cl.get(Calendar.MINUTE)) + " phút trước";
            }
        } catch (Exception e) {
            LogUtils.debug("Error: " + e.toString());
            return "";
        }
    }

    public static void main(String[] args) {
        long t = 20220330163522L;
//        System.out.println(long2Timestamp(t, "yyyyMMddHHmmss"));
        Time t1 = new Time(System.currentTimeMillis());
        System.out.println("t1=" + t1);
        System.out.println(timeString(new Time(System.currentTimeMillis()), "HHmmss"));
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
