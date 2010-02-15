package com.blackbox.presentation.extension;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.joda.time.DateTime;

import java.util.Locale;
import java.util.Collection;
import java.util.ArrayList;

import net.sourceforge.stripes.validation.ValidationError;
import com.blackbox.presentation.extension.JodaShortDateTimeConverter;

/**
 * @author A.J. Wright
 */
public class JodaShortDateTimeConverterUnitTest {

    @Test
    public void testConvert() {
        JodaShortDateTimeConverter converter = new JodaShortDateTimeConverter();
        converter.setLocale(Locale.US);
        Collection<ValidationError> errors = new ArrayList<ValidationError>(1);
        DateTime date = converter.convert("01/09/1979", DateTime.class, errors);
        assertTrue(errors.isEmpty());
        assertNotNull(date);
        assertEquals(1979, date.getYear());
        assertEquals(1, date.getMonthOfYear());
        assertEquals(9, date.getDayOfMonth());
    }


}
