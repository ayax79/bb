package com.blackbox.presentation.action.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.joda.time.DateTime;

import java.util.ArrayList;

import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.LocalizableError;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertEquals;

/**
 * @author A.J. Wright
 */
public class EighteenOrOlderTypeConverterTest {
    @Test
    public void testConvertFail() {
        String date = "01/18/2000";

        EighteenOrOlderTypeConverter converter = new EighteenOrOlderTypeConverter();
        ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
        DateTime dateTime = converter.convert(date, null, errors);
        assertNotNull(dateTime);
        assertFalse(errors.isEmpty());
        LocalizableError validationError = (LocalizableError) errors.get(0);
        assertEquals("converter.date.mustBeEighteen", validationError.getMessageKey());


    }

    @Test
    public void testConvertPass() {
        String date = "01/18/1986";

        EighteenOrOlderTypeConverter converter = new EighteenOrOlderTypeConverter();
        ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
        DateTime dateTime = converter.convert(date, null, errors);
        assertNotNull(dateTime);
        assertTrue(errors.isEmpty());
    }
}
