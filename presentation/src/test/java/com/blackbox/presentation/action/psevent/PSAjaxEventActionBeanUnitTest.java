package com.blackbox.presentation.action.psevent;

import org.junit.Test;
import org.joda.time.DateTime;

import java.text.ParseException;

import junit.framework.Assert;
import static junit.framework.Assert.*;

/**
 * @author A.J. Wright
 */
public class PSAjaxEventActionBeanUnitTest {


    @Test
    public void parseEventDateTest() throws ParseException {
        String testDate = "10/31/2009";
        String testTime = "7:00 PM";
        DateTime dateTime = new PSAjaxEventActionBean().parseEventDate(testDate, testTime);

        assertNotNull(dateTime);
        assertEquals(19, dateTime.getHourOfDay());
        assertEquals(0, dateTime.getMinuteOfHour());
        assertEquals(10, dateTime.getMonthOfYear());
        assertEquals(31, dateTime.getDayOfMonth());
        assertEquals(2009, dateTime.getYear());
    }                         
}
