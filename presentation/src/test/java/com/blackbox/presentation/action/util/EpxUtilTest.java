package com.blackbox.presentation.action.util;

import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class EpxUtilTest {

    @Test
    public void testParseExpDate() {
        DateTime dateTime = EpxUtil.parseExpDate("0110");
        assertEquals(10, dateTime.getMonthOfYear());
        assertEquals(2001, dateTime.getYear());
    }

    @Test
    public void testParseAuthTranDate() {
        DateTime dateTime = EpxUtil.parseAuthTranDate("11/30/2009 10:34:29 PM");
        assertEquals(11, dateTime.getMonthOfYear());
        assertEquals(30, dateTime.getDayOfMonth());
        assertEquals(2009, dateTime.getYear());
        assertEquals(22, dateTime.getHourOfDay());
        assertEquals(34, dateTime.getMinuteOfHour());
        assertEquals(29, dateTime.getSecondOfMinute());
    }


}
