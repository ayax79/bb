/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox;

import com.blackbox.user.SexEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class UtilsUnitTest {

    @Test
    public void testGenerateGuid() {
        final String guid = Utils.generateGuid();
        assertNotNull(guid);
        assertEquals(40, guid.length());
    }

    @Test
    public void testApplyGuid() {
        BBPersistentObject persistent = new BBPersistentObject() {

        };
        Utils.applyGuid(persistent);
        final String guid = persistent.getGuid();
        assertNotNull(guid);
        assertEquals(40, guid.length());
    }

    @Test
    public void testName() {
        String s = Utils.enumName(SexEnum.MALE);
        assertEquals("MALE", s);
    }

}
