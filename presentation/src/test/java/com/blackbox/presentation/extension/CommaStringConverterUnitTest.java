package com.blackbox.presentation.extension;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import static java.util.Arrays.asList;
import java.util.List;

import com.blackbox.presentation.action.util.CommaStringConverter;

/**
 * @author A.J. Wright
 */
public class CommaStringConverterUnitTest {
    @Test
    public void testConvert() {

        String test = "foo, bar   , baz ";
        CommaStringConverter converter = new CommaStringConverter();
        String[] result = converter.convert(test, null, null);
        assertNotNull(result);
        assertEquals(3, result.length);

        List<String> list = asList(result);
        assertTrue(list.contains("foo"));
        assertTrue(list.contains("bar"));
        assertTrue(list.contains("baz"));
    }
}
