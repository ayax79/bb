package com.blackbox.foundation.util;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Date;

import static junit.framework.Assert.*;

/**
 * @author colin@blackboxrepublic.com
 */
public class BoundsTest {


    @Test(expected = UnsupportedOperationException.class)
    public void boundLessStartIndexIsImmutable() {
        Bounds.boundLess().setStartIndex(Integer.MAX_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void boundLessStartDateIsImmutable() {
        Bounds.boundLess().setStartDate(new DateTime());
    }

    @Test
    public void hasChanged() {
        Bounds bounds = new Bounds();
        assertFalse(bounds.isChanged());
        bounds.setStartIndex(bounds.getStartIndex());
        assertFalse(bounds.isChanged());
        bounds.setStartDate(bounds.getStartDate());
        assertFalse(bounds.isChanged());
        bounds.setStartDate(null);
        assertTrue(bounds.isChanged());
        bounds = new Bounds();
        assertFalse(bounds.isChanged());
        bounds.setStartIndex(bounds.getStartIndex() + 1);
        assertTrue(bounds.isChanged());
    }
}
