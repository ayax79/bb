package com.blackbox.foundation.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * A way to assert values/conditions are as expected.
 */
public class Affirm {

    /**
     * Private constructor for static use only.
     */
    private Affirm() {
    }

    /**
     * Affirms the condition is true or throws an {@link AffirmationException} if it is not. The exception is thrown
     * with the specified message.
     *
     * @param condition boolean condition to test
     * @param message   message to be thrown with exception
     * @see Affirm#that(boolean, String, Class)
     */
    public static void that(boolean condition, String message) {
        that(condition, message, AffirmationException.class);
    }

    public static void that(boolean condition, ObjectParts message) {
        that(condition, message, AffirmationException.class);
    }

    /**
     * Affirms the condition is true or throws a descendant of {@link RuntimeException} if it is not. The exception is
     * thrown with the specified message.
     *
     * @param condition boolean condition to test
     * @param message   message to be thrown with exception
     * @param type      should be a descendant of {@link RuntimeException} and take a one String argument to a public
     *                  constructor or an {@link AffirmationException} will be thrown.
     */
    public static void that(boolean condition, String message, Class<? extends RuntimeException> type) {
        that(condition, type, message);
    }

    public static void that(boolean condition, ObjectParts message, Class<? extends RuntimeException> type) {
        that(condition, type, message.getParts());
    }

    /**
     * Affirms the condition is true or throws a descendant of {@link RuntimeException} if it is not. The exception is
     * thrown with the specified messages concatenated together.
     *
     * @param condition    boolean condition to test
     * @param messageParts individual message to be thrown with exception
     * @param type         should be a descendant of {@link RuntimeException} and take a one String argument to a public
     *                     constructor or an {@link AffirmationException} will be thrown.
     */
    private static void that(boolean condition, Class<? extends RuntimeException> type, Object... messageParts) {
        if (!condition) {
            createAndThrowTemplatedException(type, StringUtils.join(messageParts));
        }
    }

    public static void isAny(Enum<?> value, Enum<?>... values) {
        that(Arrays.asList(values).contains(value), IllegalStateException.class, "Value: [" + value + "] must exist in: [" + Arrays.asList(values) + "]");
    }

    /**
     * Affirms the supplied <code>value</code> is not null and throws an {@link AffirmationException} if
     * it is.
     *
     * @param value value to test for null
     * @param name  value reference name
     * @return the value parameter should the value not be null or throws a new instance AffirmationException otherwise
     * @see Affirm#that(boolean, String);
     */
    public static <T> T isNotNull(T value, String name) {
        return isNotNull(value, name, AffirmationException.class);
    }

    /**
     * Affirms the parameter is not null.
     *
     * @return the value parameter should the value not be null or throws a new instance of type parameter otherwise
     * @see Affirm#that(boolean, String, Class);
     */
    public static <T> T isNotNull(T value, String name, Class<? extends RuntimeException> type) {
        that(value != null, name + " cannot be null here", type);
        return value;
    }

    public static <T> T isNotNull(T value, String name, Object additionalMessage, Class<? extends RuntimeException> type) {
        that(value != null, type, name + " cannot be null here. more information: ", additionalMessage);
        return value;
    }

    public static <T> T isNotNull(T value, String name, ObjectParts additionalMessage, Class<? extends RuntimeException> type) {
        that(value != null, type, name + " cannot be null here. more information: ", additionalMessage.getParts());
        return value;
    }

    public static <T> T isNot(T value, T isNot, Class<? extends RuntimeException> type) {
        return isNot(value, isNot, null, type);
    }

    public static <T> T isNot(T value, T isNot, Object additionalMessage, Class<? extends RuntimeException> type) {
        return isNot(value, isNot, new ObjectParts(additionalMessage), type);
    }

    public static <T> T isNot(T value, T isNot, ObjectParts additionalMessage, Class<? extends RuntimeException> type) {
        that(!new EqualsBuilder().append(value, isNot).isEquals(), type, "values not allowed to be equal!", ObjectParts.getParts(additionalMessage), type);
        return isNot;
    }

    /**
     * @return null always!
     */
    public static <T> T fail(String message, Class<? extends RuntimeException> type) {
        fail(new ObjectParts(message), type);
        return null;
    }

    /**
     * @return null always!
     */
    public static <T> T fail(ObjectParts message, Class<? extends RuntimeException> type) {
        that(false, message, type);
        return null;
    }

    /**
     * Attempts to create and throw a RuntimeException type thing with the message parameter to its constructor. If not
     * able to do such a thing, it will throw an AffirmationException
     */
    private static void createAndThrowTemplatedException(Class<? extends RuntimeException> type, String message) {
        RuntimeException exception = new AffirmationException(message);
        try {
            Constructor<? extends Throwable> constructor = type.getConstructor(String.class);
            if (constructor != null && RuntimeException.class.isAssignableFrom(type)) {
                exception = (RuntimeException) constructor.newInstance(message);
            }
        } catch (Exception e) {
            /* no-op. just use the default one... */
        }
        throw exception;
    }

}