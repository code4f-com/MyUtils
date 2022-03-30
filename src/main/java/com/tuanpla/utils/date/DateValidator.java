/*
 * Copyright 2022 by Tuanpla
 * https://tuanpla.com
 */
package com.tuanpla.utils.date;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author tuanp
 */
public class DateValidator {

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
    private final String dateFormat;

    /**
     * Format validator
     *
     * @param dateFormat
     */
    public DateValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean isValidFormat() {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        try {
            Date d = new Date(System.currentTimeMillis());
            sdf.format(d);
            return true;
        } catch (IllegalArgumentException ile) {
            return false;
        }
    }
}
