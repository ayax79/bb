package com.blackbox.presentation.extension;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Locale;
import java.util.Collection;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.DateTime;

public class JodaShortDateTimeConverter implements TypeConverter<DateTime> {

    private Locale locale;

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public DateTime convert(String input, Class<? extends DateTime> targetType, Collection<ValidationError> errors) {

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy").withLocale(locale);
            return formatter.parseDateTime(input);
        } catch (Exception e) {
            errors.add(new LocalizableError("converter.date.invalidDate", input));
            return null;
        }

    }
}
