package com.blackbox.presentation.action.util;

import com.blackbox.presentation.extension.JodaShortDateTimeConverter;
import org.joda.time.DateTime;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.LocalizableError;

import java.util.Collection;

/**
 * @author A.J. Wright
 */
public class EighteenOrOlderTypeConverter extends JodaShortDateTimeConverter {

    @Override
    public DateTime convert(String input, Class<? extends DateTime> targetType, Collection<ValidationError> errors) {

        DateTime dt = super.convert(input, targetType, errors);
        if (dt != null && errors.isEmpty()) {
            DateTime eighteen = new DateTime().minusYears(18);

            if (dt.isAfter(eighteen)) {
                errors.add(new LocalizableError("converter.date.mustBeEighteen"));
            }
        }

        return dt;
    }
}
