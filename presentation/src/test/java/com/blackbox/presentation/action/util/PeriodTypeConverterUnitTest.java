package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.validation.ValidationError;
import org.joda.time.ReadablePeriod;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * @author A.J. Wright
 */
public class PeriodTypeConverterUnitTest {
    @Test
    public void testConvert() {

        PeriodTypeConverter typeConverter = new PeriodTypeConverter();
        typeConverter.setLocale(Locale.US);

        Collection<ValidationError> errors = new ArrayList<ValidationError>();
        ReadablePeriod period = typeConverter.convert("8:30", null, errors);

        assertNotNull(period);
        assertTrue(errors.isEmpty());

    }

    @Test
    public void testFail() {
        PeriodTypeConverter typeConverter = new PeriodTypeConverter();
        typeConverter.setLocale(Locale.US);

        Collection<ValidationError> errors = new ArrayList<ValidationError>();
        ReadablePeriod period = typeConverter.convert("83we0", null, errors);

        assertNull(period);
        assertFalse(errors.isEmpty());

    }
}
