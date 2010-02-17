package com.blackbox.presentation.extension;

import static com.blackbox.foundation.util.JodaDateTools.convertFromDuration;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author A.J. Wright
 */
public class JodaPeriodConverter implements TypeConverter<DateTime> {

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public DateTime convert(String input, Class<? extends DateTime> targetType, Collection<ValidationError> errors) {
        DateTime resultDateTime=null;
        try {
            resultDateTime=convertFromDuration(input);
        } catch (Exception e) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            resultDateTime=formatter.parseDateTime(input);
        }
        return resultDateTime;
    }
}
