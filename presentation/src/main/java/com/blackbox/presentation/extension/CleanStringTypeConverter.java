package com.blackbox.presentation.extension;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Overrides the string type converter so that strings are properly escaped. If you wish html to pass through you must
 * use {@link net.sourceforge.stripes.validation.StringTypeConverter} explicitly via the
 * {@link net.sourceforge.stripes.validation.Validate} annotation.
 * 
 * @author A.J. Wright
 */
public class CleanStringTypeConverter implements TypeConverter<String> {

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public String convert(String input, Class<? extends String> targetType, Collection<ValidationError> errors) {
        if (StringUtils.isNotEmpty(input)) {
            input = StringEscapeUtils.escapeHtml(input);
        }
        return input;
    }
}
