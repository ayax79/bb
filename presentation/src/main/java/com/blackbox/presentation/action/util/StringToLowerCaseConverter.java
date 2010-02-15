package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class StringToLowerCaseConverter implements TypeConverter<String> {

    private Locale locale;

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String convert(String input, Class<? extends String> targetType, Collection<ValidationError> errors) {
		if (StringUtils.isNotEmpty(input)) {
			input = input.toLowerCase(locale);
		}
		return input;
    }
}