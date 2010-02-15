package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Locale;
import java.util.Collection;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

/**
 * @author A.J. Wright
 */
public class CommaStringConverter implements TypeConverter<String[]> {

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public String[] convert(String input, Class<? extends String[]> targetType, Collection<ValidationError> errors) {

        if (StringUtils.isNotBlank(input)) {
            String[] strings = input.split(",");
            
            // process out any empty strings and trim everything
            ArrayList<String> list = new ArrayList<String>(strings.length);
            for (String string : strings) {
                string = string.trim();
                if (StringUtils.isNotEmpty(string)) {
                    list.add(string);
                }
            }

            return list.toArray(new String[list.size()]);
        }
        return null;
    }
}
