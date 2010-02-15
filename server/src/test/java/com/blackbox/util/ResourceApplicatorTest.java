package com.blackbox.util;

import org.junit.Test;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author colin@blackboxrepublic.com
 */
public class ResourceApplicatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void resourceAnnotationNotFound() throws IllegalAccessException {
        ResourceApplicator.injectByType(new NoResources(), new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resourceAnnotationFoundButWrongType() throws IllegalAccessException {
        ResourceApplicator.injectByType(new OneStringResource(), new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resourceAnnotationFoundButMoreThanOneWithCorrectType() throws IllegalAccessException {
        ResourceApplicator.injectByType(new TwoStringResource(), "what about bob?");
    }

    @Test
    public void resourceAnnotationFoundWithCorrectType() throws IllegalAccessException {
        OneStringResource target = new OneStringResource();
        ResourceApplicator.injectByType(target, "what about bob?");
        assertEquals("what about bob?", target.getPayload());
    }

    @Test
    public void resourceAnnotationFoundWithCorrectTypeUsingNameAsDescriminator() throws IllegalAccessException {
        TwoStringResource target = new TwoStringResource();
        ResourceApplicator.injectByTypeAndName(target, "what about bob?", "pickme!");
        assertNull(target.getPayload1());
        assertEquals("what about bob?", target.getPayload2());
    }

    private static class NoResources {
    }

    private static class OneStringResource {

        @Resource
        String payload;

        public String getPayload() {
            return payload;
        }
    }

    private static class TwoStringResource {
        @Resource
        String payload1;

        @Resource(name = "pickme!")
        String payload2;

        public String getPayload1() {
            return payload1;
        }

        public String getPayload2() {
            return payload2;
        }
    }
}
