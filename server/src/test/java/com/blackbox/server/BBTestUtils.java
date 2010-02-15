/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server;

import org.yestech.event.event.IEvent;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.listener.IListener;
import org.yestech.lib.hibernate.YesHibernateOperations;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Base clase for integration testing that sets up the hibernate session correctly.
 */
public final class BBTestUtils {
    private BBTestUtils() {
    }

    @SuppressWarnings({"ConstantConditions"})
    public static void assertValidListener(IListener listener) {

        final ListenedEvents annotation = listener.getClass().getAnnotation(ListenedEvents.class);
        if (annotation == null) {
            fail("Listener must contain an ListenedEvents annotation");
        }

        final Class<? extends IEvent>[] classes = annotation.value();
        if (classes == null || classes.length == 0) {
            fail("Listener did not specify and Events to listen for");
        }
    }

    public static YesHibernateOperations proxyTemplate(InvocationHandler handler) {
        return (YesHibernateOperations) Proxy.newProxyInstance(
                BBTestUtils.class.getClassLoader(),
                new Class[]{YesHibernateOperations.class},
                handler);

    }


}