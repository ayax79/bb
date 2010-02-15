/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.util;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 *
 *
 */
public class DateTimeUtils extends org.joda.time.DateTimeUtils
{

    public static DateTimeFormatter getDateTimeFormatter(Locale l)
    {
        DateTimeFormatter formatter = DateTimeFormat.mediumDateTime();
        return formatter.withLocale(l);
    }

    public static String formatDateTime(DateTime dateTime, Locale l)
    {
        DateTimeFormatter formatter = getDateTimeFormatter(l);
        return dateTime.toString(formatter);
    }
    public static boolean equalDate(DateTime a,DateTime b){
        if(a.getYear()==b.getYear()&&a.getMonthOfYear()==b.getMonthOfYear()&&a.getDayOfMonth()==b.getDayOfMonth()){
            return true;
        }
        return false;
    }
    public static boolean isToday(DateTime time){
        DateTime today=new DateTime(new Date());
        if(equalDate(time,today)){
            return true;
        }
        return false;
    }

}
