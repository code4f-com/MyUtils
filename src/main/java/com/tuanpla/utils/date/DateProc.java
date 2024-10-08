/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.date;

import com.tuanpla.utils.common.Convert;
import com.tuanpla.utils.exception.CustomerException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author tuanpla
 */
public abstract class DateProc {

    private static final Logger logger = LogManager.getLogger(DateProc.class);

    /*
        https://www.w3resource.com/mysql/date-and-time-functions/mysql-from_unixtime-function.php
        MySQL. Dưới đây là một số ví dụ về các định dạng phổ biến:

        %Y: Năm đầy đủ (ví dụ: 2021)
        %y: Năm cuối cùng hai chữ số (ví dụ: 21 cho năm 2021)
        %m: Tháng (01-12)
        %c: Tháng (1-12)
        %d: Ngày (01-31)
        %H: Giờ (00-23)
        %h: Giờ (01-12)
        %i: Phút (00-59)
        %s: Giây (00-59)
        %p: AM hoặc PM
        %W: Tên đầy đủ của ngày trong tuần (ví dụ: Wednesday)
        %a: Tên viết tắt của ngày trong tuần (ví dụ: Wed)
        %M: Tên đầy đủ của tháng (ví dụ: January)
        %b: Tên viết tắt của tháng (ví dụ: Jan)
     */

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
    
        y: Năm (đại diện cho năm cuối cùng gần nhăm hiện tại nhất Ex 21/10/21 -> dd/MM/y =>21/10/2021).
        yy: Năm (số dư 2 chữ số) (ví dụ: 21 cho năm 2021).
        yyy: Năm (hiển thị full year với 4 chữ số) (ví dụ: 21 => 0021, 221 => 0221 ).
        yyyy: Năm đầy đủ (ví dụ: 2021).
        M: Tháng (1-12).
        MM: Tháng (01-12).
        MMM: Tên tháng viết tắt (ví dụ: "Jan" cho tháng 1).
        MMMM: Tên đầy đủ của tháng (ví dụ: "January" cho tháng 1).
        d: Ngày trong tháng (1-31).
        dd: Ngày trong tháng (01-31).
        E: Ngày trong tuần (ví dụ: "Tue" cho thứ Ba).
        EE: Ngày trong tuần (ví dụ: "Tuesday" cho thứ Ba).
        a: Buổi (AM hoặc PM).
        H: Giờ trong ngày (0-23).
        HH: Giờ trong ngày (00-23).
        k: Giờ trong ngày (1-24).
        m: Phút (0-59).
        mm: Phút (00-59).
        s: Giây (0-59).
        ss: Giây (00-59).
        S: milliseconds (SSS)
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

    public static int getWeekOfYear() {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        // Lấy số tuần của năm
        int weekOfYear = today.get(WeekFields.ISO.weekOfWeekBasedYear());
        return weekOfYear;
    }

    public static int getCurentMonth() {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        // Lấy số tuần của năm
        int month = today.getMonthValue();
        return month;
    }

    public static double string2Time(String time) {
        String aTime[] = time.split(":");
        return Double.parseDouble(aTime[0]) + Double.parseDouble(aTime[1]) / 60;
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

    public static Date string2Date(String strDate, String fmIn) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmIn);
            java.util.Date parsedDate = dateFormat.parse(strDate);
            return new Date(parsedDate.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String date2String(Date d, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(d);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
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
            return Integer.valueOf(strDate);
        } catch (NumberFormatException ex) {
            logger.warn(ex.getMessage());
            return null;
        }
    }

    public static Integer date2Int(Long date, String fmOut) {
        try {
            Date d = new Date(date);
            String strDate = date2String(d, fmOut);
            return Integer.valueOf(strDate);
        } catch (NumberFormatException ex) {
            logger.warn(ex.getMessage());
            return null;
        }
    }

    public static Integer currentInt(String fmOut) {
        try {
            String crdStr = date2String(currentDate(), fmOut);
            return Integer.valueOf(crdStr);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    public static Integer stringDate2Int(String input, String fmInput, String fmOut) {
        try {
            String strDate = date2String(string2Date(input, fmInput), fmOut);
            return Integer.valueOf(strDate);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
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
            logger.warn(ex.getMessage());
            return null;
        }
    }

    public static String timestamp2String(Timestamp ts, String fmOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmOut);
            return dateFormat.format(ts);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return "";
        }
    }

    /**
     *
     * @param input Long format as String
     * @param forMat Ex: yyyyMMddhhmiss
     * @return
     */
    public static Timestamp long2Timestamp(Long input, String forMat) {
        try {
            String strInputDate = String.valueOf(input);
            SimpleDateFormat dateFormat = new SimpleDateFormat(forMat);
            return new Timestamp(dateFormat.parse(strInputDate).getTime());
        } catch (ParseException ex) {
            logger.warn(ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param input Long Timestamp value
     * @return
     */
    public static Timestamp long2Timestamp(Long input) {
        try {
            return new Timestamp(input);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return null;
        }
    }

    public static Long timestamp2Long(Timestamp ts, String fmOut) {
        try {
            String strTimestamp = timestamp2String(ts, fmOut);
            return Long.valueOf(strTimestamp);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Long current2Long(String fmOut) {
        try {
            String crdStr = timestamp2String(currentTimestamp(), fmOut);
            return Long.valueOf(crdStr);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    public static Long date2Long(Date input, String fmOut) {
        try {
            Timestamp ts = new Timestamp(input.getTime());
            String dateStr = timestamp2String(ts, fmOut);
            return Long.valueOf(dateStr);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    public static String timeString(Time ti, String fmOut) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmOut);
            return dateFormat.format(ti);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
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
        int index = strMonthYear.indexOf("/");
        if (index < 0) {
            return "";
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(d);
        var maxDate = cl.getActualMaximum(Calendar.DATE);
        return maxDate + "/" + strMonthYear;
    }

    public static boolean isFriday(Timestamp ts) {
        if (ts == null) {
            return false;
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(ts);
        return cl.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
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

    /**
     * long format yyyyMMddHHmmssSSS
     *
     * @return
     */
    public static long nowToLong() {
        try {
            String str = timestamp2String(currentTimestamp(), "yyyyMMddHHmmssSSS");
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            return Long.MIN_VALUE;
        }
    }

    /**
     * long format yyyyMMdd
     *
     * @return
     */
    public static long nowToLongDate() {
        try {
            String str = timestamp2String(currentTimestamp(), "yyyyMMdd");
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            return Long.MIN_VALUE;
        }
    }

    /**
     * long format yyyyMMddHHmmss
     *
     * @return
     */
    public static long nowToLongDateTime() {
        try {
            String str = timestamp2String(currentTimestamp(), "yyyyMMddHHmmss");
            return Long.parseLong(str);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return Long.MIN_VALUE;
        }
    }

    /**
     * long format yyyyMMddHHmmssSSS
     *
     * @return
     */
    public static String nowToString() {
        try {
            return timestamp2String(currentTimestamp(), "yyyyMMddHHmmssSSS");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return "";
        }
    }

    /**
     * long format yyyyMMddHHmmssSSS
     *
     * @param d
     * @return
     */
    public static long dateToLong(Date d) {
        try {
            Timestamp ts = new Timestamp(d.getTime());
            String str = timestamp2String(ts, "yyyyMMddHHmmssSSS");
            return Long.parseLong(str);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return 0;
        }
    }

    /**
     *
     * @param date GMT +0
     * @param fmIn dd/MM/yyyy
     * @return
     */
    public static long dateToLongTime(String date, String fmIn) {
        try {
            Date d = string2Date(date, fmIn); // GMT +0
            return d.getTime();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return Long.MIN_VALUE;
        }
    }

    /**
     *
     * @param d
     * @param fmOut example format: yyyyMMddHHmmssSSS
     * <br> => long = 20220304143044321
     * @return
     */
    public static long dateToLong(Date d, String fmOut) {
        try {
            Timestamp ts = new Timestamp(d.getTime());
            String str = timestamp2String(ts, fmOut);
            return Long.parseLong(str);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return 0;
        }
    }

    public static long dateToLongEndDay(String date, String fmIn) {
        try {
            Date d = string2Date(date, fmIn); // GMT +0
            Today td = new Today(d.getTime());
            return td.endOf();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return Long.MIN_VALUE;
        }
    }

    public static String long2Date(Long input, String fmOut) {
        String result = "";
        try {
            Timestamp ts = new Timestamp(input);
            result = timestamp2String(ts, fmOut);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return result;
    }

    public static String long2Date_ddMM4Y(Long input) {
        String result = "";
        try {
            Timestamp ts = new Timestamp(input);
            result = timestamp2String(ts, "dd/MM/yyyy");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return result;
    }

    public static String long2Date_ddMM4YHHmm(Long input) {
        String result = "";
        try {
            Timestamp ts = new Timestamp(input);
            result = timestamp2String(ts, "dd/MM/yyyy HH:mm");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return result;
    }

    /**
     * Lưu ý java.sql.Date chỉ biểu thị ngày không có giờ
     *
     * @param input
     * @return
     */
    public static Date long2Date(Long input) {
        Date result = null;
        try {
            result = new Date(input);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return result;
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

    public static String extractDayOfWeek(Date date) {
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
            long longEnd = currentDate().getTime();
            long distance = longEnd - longStart;
            result = (int) (distance / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    /**
     * Date longtime milisecond
     *
     * @param longDate
     * @return
     */
    public static int totalDayOfMonth(long longDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(longDate);
        int year = cal.get(Calendar.YEAR);
        // Calendar Month from 0 - 11
        // Calendar Month from 0 - 11 || YearMonth -> Month 1 - 12 ==> Phải cộng thêm 1 khi dùng tháng ở đây
        YearMonth specificYearMonth = YearMonth.of(year, cal.get(Calendar.MONTH) + 1);
        int totalDaysSpecific = specificYearMonth.lengthOfMonth();
        return totalDaysSpecific;
    }

    public static String getYearOld(long longDate) {
//        Today td = new Today(10, 10, 2023);
        Today td = new Today();
        String invalidDate = "Ngày sinh không hợp lệ";
        Today birth = new Today(new Timestamp(longDate));
        int years = td.getYear() - birth.getYear();
        int months = td.getMonth() - birth.getMonth();
        int days = td.getDay() - birth.getDay() - 1;
        if (years < 0) {
            return invalidDate;
        } else if (years == 0) {
            if (months < 0) {
                return invalidDate;
            } else if (months == 0) {
                if (days < 0) {
                    return invalidDate;
                } else {
                    days += 1;
                    if (days > 31) {
                        return invalidDate;
                    }
                }
            } else { // month > 0
                if (days <= 0) {
                    months -= 1; // Giảm tháng đi 1;
                    days = td.getDay() + (totalDayOfMonth(longDate) - birth.getDay());
                } else {
                    days += 1;
                    if (days > 31) {
                        return invalidDate;
                    }
                }
            }
        } else if (years > 0) {
            if (months < 0) {
                years -= 1; // Giảm năm đi 1
                months = (td.getMonth() + 12) - birth.getMonth(); // => Đương nhiên tháng > 0
                if (days <= 0) {
                    months -= 1; // Giảm tháng đi 1;
                    days = td.getDay() + (totalDayOfMonth(longDate) - birth.getDay());
                } else {
                    days += 1;
                    if (days > 31) {
                        return invalidDate;
                    }
                }
            } else if (months == 0) {
                if (days < 0) {
                    years = years - 1;
                    months = 11;
                    days = td.getDay() + (totalDayOfMonth(longDate) - birth.getDay());
                } else {
                    days += 1;
                    if (days > 31) {
                        return invalidDate;
                    }
                }
            } else { // month > 0
                if (days <= 0) {
                    months -= 1; // Giảm tháng đi 1;
                    days = td.getDay() + (totalDayOfMonth(longDate) - birth.getDay());
                } else {
                    days += 1;
                    if (days > 31) {
                        return invalidDate;
                    }
                }
            }
        }
        // In ra kết quả
        if (years > 2) {
            return years + " năm" + (months > 0 ? (" - " + months + " tháng") : "");
        } else {
            months += years * 12;
            if (months >= 2) {
                return months + " tháng" + (days > 0 ? (" - " + days + " ngày") : "");
            } else {
                days += months * 30;
                return days + " ngày";
            }
        }
    }

}
