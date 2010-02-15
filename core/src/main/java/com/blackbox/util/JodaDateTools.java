package com.blackbox.util;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.lucene.document.DateTools.Resolution;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;


public class JodaDateTools {


    private static final DateTimeFormatter YEAR_FORMAT = DateTimeFormat.forPattern("yyyy").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormat.forPattern("yyyyMM").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter DAY_FORMAT = DateTimeFormat.forPattern("yyyyMMdd").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter HOUR_FORMAT = DateTimeFormat.forPattern("yyyyMMddHH").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter MINUTE_FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmm").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter SECOND_FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmmss").withChronology(ISOChronology.getInstance());
    private static final DateTimeFormatter MILLISECOND_FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS").withChronology(ISOChronology.getInstance());

    // cannot create, the class has static methods only
    private JodaDateTools() {
    }

    public static DateTime stringToDate(String dateString) throws ParseException {
        DateTime date;

        if (dateString.length() == 4) {
            date = YEAR_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 6) {
            date = MONTH_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 8) {
            date = DAY_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 10) {
            date = HOUR_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 12) {
            date = MINUTE_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 14) {
            date = SECOND_FORMAT.parseDateTime(dateString);
        } else if (dateString.length() == 17) {
            date = MILLISECOND_FORMAT.parseDateTime(dateString);
        } else {
            throw new IllegalArgumentException("Input is not valid date string: " + dateString);
        }
        return date;
    }

    /**
     * Converts a Date to a string suitable for indexing.
     *
     * @param date       the date to be converted
     * @param resolution the desired resolution, see
     * @return a string in format <code>yyyyMMddHHmmssSSS</code> or shorter,
     *         depeding on <code>resolution</code>; using GMT as timezone
     */
    public static String dateToString(DateTime date, Resolution resolution) {
        return timeToString(date.getMillis(), resolution);
    }


    /**
     * Converts a millisecond time to a string suitable for indexing.
     *
     * @param time       the date expressed as milliseconds since January 1, 1970, 00:00:00 GMT
     * @param resolution the desired resolution, see
     *                   {@link #round(long, Resolution)}
     * @return a string in format <code>yyyyMMddHHmmssSSS</code> or shorter,
     *         depeding on <code>resolution</code>; using GMT as timezone
     */
    public static String timeToString(long time, Resolution resolution) {

        DateTime date = round(time, resolution);

        String result;
        if (resolution == Resolution.YEAR) {
            result = date.toString(YEAR_FORMAT);
        } else if (resolution == Resolution.MONTH) {
            result = date.toString(MONTH_FORMAT);
        } else if (resolution == Resolution.DAY) {
            result = date.toString(DAY_FORMAT);
        } else if (resolution == Resolution.HOUR) {
            result = date.toString(HOUR_FORMAT);
        } else if (resolution == Resolution.MINUTE) {
            result = date.toString(MINUTE_FORMAT);
        } else if (resolution == Resolution.SECOND) {
            result = date.toString(SECOND_FORMAT);
        } else if (resolution == Resolution.MILLISECOND) {
            result = date.toString(MILLISECOND_FORMAT);
        } else {
            throw new IllegalArgumentException("unknown resolution " + resolution);
        }
        return result;
    }

    /**
     * Limit a date's resolution. For example, the date <code>1095767411000</code>
     * (which represents 2004-09-21 13:50:11) will be changed to
     * <code>1093989600000</code> (2004-09-01 00:00:00) when using
     * <code>Resolution.MONTH</code>.
     *
     * @param time       The time to round
     * @param resolution The desired resolution of the date to be returned
     * @return the date with all values more precise than <code>resolution</code>
     *         set to 0 or 1, expressed as milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public static DateTime round(long time, Resolution resolution) {
        DateTime dateTime = new DateTime(time, ISOChronology.getInstanceUTC());

        if (resolution == Resolution.YEAR) {
            dateTime = dateTime.withMonthOfYear(0).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        } else if (resolution == Resolution.MONTH) {
            dateTime = dateTime.withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        } else if (resolution == Resolution.DAY) {
            dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        } else if (resolution == Resolution.HOUR) {
            dateTime = dateTime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        } else if (resolution == Resolution.MINUTE) {
            dateTime = dateTime.withSecondOfMinute(0).withMillisOfSecond(0);
        } else if (resolution == Resolution.SECOND) {
            dateTime = dateTime.withMillisOfSecond(0);
        } else if (resolution == Resolution.MILLISECOND) {
            // don't cut off anything
        } else {
            throw new IllegalArgumentException("unknown resolution " + resolution);
        }
        return dateTime;
    }

    public static DateTime convertFromDuration(String pattern) {

        if (isBlank(pattern)) {
            return null;
        }

        boolean isMinus = false;
        if (pattern.startsWith("-")) {
            isMinus = true;
            pattern = pattern.substring(1); //remove the first character
        }

        char duration = pattern.charAt(pattern.length() - 1);
        pattern = pattern.substring(0, pattern.length() - 1);
        int amount = Integer.parseInt(pattern);

        ReadablePeriod period;
        if (duration == 'M') {
            period = Months.months(amount);
        } else if (duration == 'D') {
            period = Days.days(amount);
        } else if (duration == 'W') {
            period = Weeks.weeks(amount);
        } else {
            throw new IllegalArgumentException("Unknown duration time " + amount);
        }

        if (isMinus) {
            return new DateTime().minus(period);
        } else {
            return new DateTime().plus(period);
        }
    }


}
