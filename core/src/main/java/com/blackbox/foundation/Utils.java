/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation;

import com.blackbox.foundation.commerce.Inventory;
import com.blackbox.foundation.commerce.Product;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.social.Category;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.lib.crypto.MessageDigestUtils;
import org.yestech.lib.util.Pair;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static Object[] array(Object... objects) {
        return objects;
    }

    public static String[] array(String... strings) {
        return strings;
    }

    public static String generateGuid() {
        final String rawGuid = UUID.randomUUID().toString() + System.currentTimeMillis();
        return MessageDigestUtils.sha1Hash(rawGuid);
    }

    public static void applyGuids(Collection<? extends BBPersistentObject> persistables) {
        if (persistables != null) {
            for (BBPersistentObject persistable : persistables) {
                applyGuid(persistable);
            }
        }
    }

    public static void applyGuid(BBPersistentObject persistable) {
        String guid = generateGuid();
        persistable.setGuid(guid);
    }

    public static void applyGuid(EntityReference owner) {
        String guid = generateGuid();
        owner.setOwnerIdentifier(guid);
    }

    public static String enumName(Enum e) {
        return e == null ? null : e.name();
    }

    public static int enumOridinal(Enum e) {
        return e == null ? -1 : e.ordinal();
    }

    @SuppressWarnings({"unchecked"})
    public static Enum[] enumValues(Class<Enum<?>> e) {
        try {
            Method method = e.getMethod("values");
            return (Enum[]) method.invoke(null);

        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
        return null;
    }

    public static Enum enumOrdinalValueOf(Class<Enum<?>> e, int ordinal) {
        Enum[] array = enumValues(e);
        return array != null ? array[ordinal] : null;
    }


    public static Map<String, Class<?>> xmlAliases() {
        HashMap<String, Class<?>> aliases = newHashMap();
        aliases.put("product", Product.class);
        aliases.put("category", Category.class);
        aliases.put("inventory", Inventory.class);
        aliases.put("address", Address.class);
        return aliases;
    }

    public static boolean isPersistable(Object obj) {
        return obj instanceof BBPersistentObject;
    }

    /**
     * Returns the current date and time in UTC.
     *
     * @return the UTC date and time
     */
    public static DateTime getCurrentDateTime() {
        return new DateTime(DateTimeZone.UTC);
    }

    /**
     * Returns the supplied date and time in UTC.
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @param hourOfDay
     * @param minuteOfHour
     * @param secondOfMinute
     * @param millisOfSecond
     * @return
     */
    public static DateTime getDateTime(int year,
                                       int monthOfYear,
                                       int dayOfMonth,
                                       int hourOfDay,
                                       int minuteOfHour,
                                       int secondOfMinute,
                                       int millisOfSecond) {
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, DateTimeZone.UTC);
    }

    /**
     * Returns the status code for the given url.
     * <p/>
     * This will attempt a GET on the url and return the code.
     *
     * @param url The url to check.
     * @return the status code (including 404 in *two* cases: resource not available <strong>or</strong> url is invalid url
     * @throws java.io.IOException If there is a error loading
     */
    public static int checkUrl(String url) throws IOException {
        try {
            GetMethod get = new GetMethod(url);
            HttpClient client = new HttpClient();
            return client.executeMethod(get);
        } catch (IllegalArgumentException e) {
            return 404; // emulates resource not there when a *non* url is passed in... 
        }
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <K, V> Pair<V, K> reversePair(Pair<K, V> pair) {
        return new Pair<V, K>(pair.getSecond(), pair.getFirst());
    }


    public static <K, V> Pair<K, V> createPair(Map.Entry<K, V> entry) {
        return new Pair<K, V>(entry.getKey(), entry.getValue());
    }

    public static String getChildBodyString(Element parent, String name) {
        if (parent != null) {
            Element child = parent.getChild(name);
            if (child != null) {
                String text = child.getTextTrim();
                if (isNotBlank(text)) {
                    return text;
                }
            }
        }
        return null;
    }

    public static DateTime getBodyChildSecondsAsDate(Element parent, String name) {
        String body = getChildBodyString(parent, name);
        return body != null ? new DateTime(Long.parseLong(body) * 1000L) : null;
    }

    /**
     * Version that is serializable
     */
    @SuppressWarnings({"JavaDoc"})
    public static <F, T> List<T> transform(
            List<F> fromList, Function<? super F, ? extends T> function) {
        List<T> list = Lists.transform(fromList, function);
        return new ArrayList<T>(list);

    }

    @SuppressWarnings({"JavaDoc", "unchecked"})
    public static <F, T> Collection<T> transform(
            Collection<F> fromList, Function<? super F, ? extends T> function) {
        Collection<T> list = (Collection<T>) Collections2.transform(fromList, function);
        return new ArrayList<T>(list);

    }

    /**
     * Logs message at error onto logger parameter with message parameter
     *
     * @throws BlackBoxException wrapping the exception parameter
     */
    public static void logWrapAndThrow(String message, Logger logger, Exception exception) {
        logger.error(message, exception);
        throw new BlackBoxException(message, exception);
    }


}
