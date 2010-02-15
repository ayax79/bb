/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.util;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;

import java.util.Calendar;

public class DateUtil {

    /**
     * @deprecated Please use {@link org.joda.time.DateMidnight}
     */
    @Deprecated
    public static Calendar getTodayMidnight() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    public static DateTime join(DateTime dt, ReadablePeriod period, AmPmEnum ampm) {
        dt = dt.plus(period);
        if (ampm == AmPmEnum.PM) dt = dt.plusHours(12);
        return dt;
    }

    public static DateTime firstDayOfMonth() {
        DateMidnight dm = new DateMidnight();
        return dm.minusDays(dm.getDayOfMonth()).toDateTime();
    }

    public static PeriodParser createPeriodTimeParser() {
        return timeBuilder().toParser();
    }

    public static PeriodFormatter createPeriodTimeFormatter() {
        return timeBuilder().toFormatter();
    }


    private static PeriodFormatterBuilder timeBuilder() {
        return new PeriodFormatterBuilder()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes();
    }

}
