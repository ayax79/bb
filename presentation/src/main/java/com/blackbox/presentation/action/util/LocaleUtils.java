/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 *
 */
public final class LocaleUtils
{
    private LocaleUtils()
    {
    }

    public static ResourceBundle getResourceBundle(Locale locale)
    {
        return PropertyResourceBundle.getBundle("StripesResources", locale);
    }

    public static String getMessage(Locale l, String key, Object... params)
    {
        ResourceBundle bundle = getResourceBundle(l);
        String value = bundle.getString(key);
        return MessageFormat.format(value, params);
    }

}
