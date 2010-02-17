package com.blackbox.foundation.util;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * A way to bind onto @Resource annotated package-accessible fields outside of Ioc configuration...
 *
 * @author colin@blackboxrepublic.com
 */
public class ResourceApplicator {

    public static void injectByType(Object target, Object resource) throws IllegalAccessException {
        injectByTypeAndName(target, resource, null);
    }

    public static void injectByTypeAndName(Object target, Object resource, String name) throws IllegalAccessException {
        boolean foundAndSet = false;
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Resource.class) && field.getType().equals(resource.getClass()) && resourceNameIsEqual(name, field)) {
                Affirm.that(!foundAndSet, "Found more than one @Resource with that type, cannot continue like this!", IllegalArgumentException.class);
                field.set(target, resource);
                foundAndSet = true;
            }
        }
        Affirm.that(foundAndSet, "@Resource not able to be bound to resource parameter as no fields aligned with request: " + target + " resource: " + resource, IllegalArgumentException.class);
    }

    private static boolean resourceNameIsEqual(String name, Field field) {
        return name == null || field.getAnnotation(Resource.class).name().equals(name);
    }
}
