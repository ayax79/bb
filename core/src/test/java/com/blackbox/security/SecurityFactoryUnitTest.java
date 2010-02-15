/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SecurityFactoryUnitTest {
    @Test
    public void testCreateApplicationPermission() {
        final ApplicationPermission permission = SecurityFactory.createPermission(PermissionTypeEnum.APPLICATION);
        assertTrue(permission instanceof ApplicationPermission);
        assertEquals(PermissionTypeEnum.APPLICATION, permission.getType());
    }

    @Test
    public void testCreateSocialPermission() {
        final SocialPermission permission = SecurityFactory.createPermission(PermissionTypeEnum.SOCIAL);
        assertTrue(permission instanceof SocialPermission);
        assertEquals(PermissionTypeEnum.SOCIAL, permission.getType());
    }
}
