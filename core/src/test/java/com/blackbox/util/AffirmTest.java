/**
 *******************************************************************************
 * Licensed Materials - Property of NWEA *
 * Copyright NWEA 2009 All Rights Reserved *
 * Created on 3/6/2009 *
 *******************************************************************************
 */
package com.blackbox.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class AffirmTest {

    @Test
    public void testThatBooleanString() {
        /* test that(true) and that(false) which should fail the false */
        try {
            Affirm.that(true, "test true");
            Affirm.that(false, "test false");
        } catch (Throwable t) {
            /* check that AffirmationException was thrown, and message is the correct one */
            assertEquals("Exception thrown should be an AffirmationException.", AffirmationException.class, t.getClass());
            assertFalse("AffirmationException error message should not equal \"test true\".", "test true".equals(t.getMessage()));
            assertTrue("AffirmationException error message should equal \"test false\".", "test false".equals(t.getMessage()));
        }
    }

    @Test
    public void testThatBooleanStringClassOfRuntimeException() {
        /* test that(true) and that(false) which should fail the false and throw an IllegalStateException */
        try {
            Affirm.that(true, "IllegalArgumentException test", IllegalArgumentException.class);
            Affirm.that(false, "IllegalStateException test", IllegalStateException.class);
        } catch (Throwable t) {
            /* check that IllegalStateException was thrown, and message is the correct one */
            assertEquals("Exception thrown should be an IllegalStateException.", IllegalStateException.class, t.getClass());
            assertFalse("IllegalStateException error message should not equal \"test true\".", "IllegalArgumentException test".equals(t.getMessage()));
            assertTrue("IllegalStateException error message should equal \"test false\".", "IllegalStateException test".equals(t.getMessage()));
        }
    }

    @Test
    public void testIsNotNullString() {
        /* test "not null" & null which should fail the null */
        try {
            Affirm.isNotNull("not null", "NotNullString");
            Affirm.isNotNull(null, "NullString");
        } catch (Throwable t) {
            /* check that AffirmationException was thrown, and message is the correct one */
            assertEquals("Exception thrown should be an AffirmationException.", AffirmationException.class, t.getClass());
            assertFalse("AffirmationException error message should not equal \"NotNullString cannot be null here\".", "NotNullString cannot be null here".equals(t.getMessage()));
            assertTrue("AffirmationException error message should equal \"NullString cannot be null here\".", "NullString cannot be null here".equals(t.getMessage()));
        }

        /* test isNotNull() returns original value */
        Object original = new Object();
        Object value = Affirm.isNotNull(original, "Object");
        assertSame("Affirm.isNotNull(T, String) did not return original value.", original, value);
    }

    @Test
    public void testIsNotNullStringClassOfRuntimeException() {
        /* test "not null" & null which should fail the null and throw an IllegalStateException */
        try {
            Affirm.isNotNull("not null", "IllegalArgumentException", IllegalArgumentException.class);
            Affirm.isNotNull(null, "IllegalStateException", IllegalStateException.class);
        } catch (Throwable t) {
            /* check that IllegalStateException was thrown, and message is the correct one */
            assertEquals("Exception thrown should be an IllegalStateException.", IllegalStateException.class, t.getClass());
            assertFalse("IllegalStateException error message should not equal \"IllegalArgumentException cannot be null here\".", "IllegalArgumentException cannot be null here".equals(t.getMessage()));
            assertTrue("IllegalStateException error message should equal \"IllegalStateException cannot be null here\".", "IllegalStateException cannot be null here".equals(t.getMessage()));
        }

        /* test isNotNull() returns original value */
        Object original = new Object();
        Object value = Affirm.isNotNull(original, "Object", IllegalArgumentException.class);
        assertSame("Affirm.isNotNull(T, String, Class<? extends RuntimeException>) did not return original value.", original, value);
    }

    @Test
    public void testIsNotNullStringStringClassOfRuntimeException() {
        /* test "not null" & null which should fail the null and throw an IllegalStateException with additional detail */
        try {
            Affirm.isNotNull("not null", "IllegalArgumentException", "failed", IllegalArgumentException.class);
            Affirm.isNotNull(null, "IllegalStateException", "failed", IllegalStateException.class);
        } catch (Throwable t) {
            /* check that IllegalStateException was thrown, and message is the correct one */
            assertEquals("Exception thrown should be an IllegalStateException.", IllegalStateException.class, t.getClass());
            assertFalse("IllegalStateException error message should not equal \"IllegalArgumentException cannot be null here. more information: failed\".",
                    "IllegalArgumentException cannot be null here. more information: failed".equals(t.getMessage()));
            assertTrue("IllegalStateException error message should equal \"IllegalStateException cannot be null here. more information: failed\".",
                    "IllegalStateException cannot be null here. more information: failed".equals(t.getMessage()));
        }

        /* test isNotNull() returns original value */
        Object original = new Object();
        Object value = Affirm.isNotNull(original, "Object", "failed", IllegalArgumentException.class);
        assertSame("Affirm.isNotNull(T, String, String, Class<? extends RuntimeException>) did not return original value.", original, value);
    }

    @Test
    public void testFail() {
        /* fail and check exception type and message */
        try {
            Affirm.fail("failed message", IllegalStateException.class);
        } catch (Throwable t) {
            assertEquals("Exception thrown should be an IllegalStateException.", IllegalStateException.class, t.getClass());
            assertTrue("IllegalStateException error message should equal \"failed message\".",
                    "failed message".equals(t.getMessage()));
        }
    }

    @Test
    public void testIsAnySuccess() {
        Affirm.isAny(TestEnum.one, TestEnum.one);
        Affirm.isAny(TestEnum.one, TestEnum.one, TestEnum.two);
        Affirm.isAny(TestEnum.one, TestEnum.one, TestEnum.five);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyTargetSet() {
        Affirm.isAny(TestEnum.one);
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongTargetSet() {
        Affirm.isAny(TestEnum.one, TestEnum.two, TestEnum.three, TestEnum.four, TestEnum.five);
    }

    public static enum TestEnum {
        one, two, three, four, five
    }


    @Test
    public void testIsNotSuccess() {
        Affirm.isNot(TestEnum.one, TestEnum.two, "should not throw!", IllegalArgumentException.class);
        Affirm.isNot(null, TestEnum.two, "should not throw!", IllegalArgumentException.class);
        Affirm.isNot(TestEnum.one, null, "should not throw!", IllegalArgumentException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotWhenSame1() {
        Affirm.isNot(TestEnum.one, TestEnum.one, IllegalArgumentException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotWhenSame2() {
        Object o = new Object();
        Affirm.isNot(o, o, IllegalArgumentException.class);
    }

}
