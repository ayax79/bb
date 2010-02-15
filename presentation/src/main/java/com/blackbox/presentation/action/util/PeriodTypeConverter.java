package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.util.StringUtil;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Locale;

import com.blackbox.util.DateUtil;

/**
 * @author A.J. Wright
 */
public class PeriodTypeConverter implements TypeConverter<ReadablePeriod> {
    private Locale locale;

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ReadablePeriod convert(String input, Class<? extends ReadablePeriod> targetType, Collection<ValidationError> errors) {

        PeriodParser parser = DateUtil.createPeriodTimeParser();


        try {
            MutablePeriod period = new MutablePeriod();
            int val = parser.parseInto(period, input, 0, locale);
            if (val < 0) {
                errors.add(new LocalizableError("converter.period.invalidTime"));
                return null;
            }
            return period;

        }
        catch (IllegalArgumentException e) {
            errors.add(new LocalizableError("converter.period.invalidTime"));
            return null;
        }
    }
}
