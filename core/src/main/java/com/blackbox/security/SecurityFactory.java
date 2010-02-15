/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import com.blackbox.Utils;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
public class SecurityFactory {

    public static <P extends BaseBBPermission> P createPermission(PermissionTypeEnum permissionTypeEnum) {
        BaseBBPermission permission;
        if (PermissionTypeEnum.SOCIAL.equals(permissionTypeEnum)) {
            permission = new SocialPermission();
        } else if (PermissionTypeEnum.APPLICATION.equals(permissionTypeEnum)) {
            permission = new ApplicationPermission();
        } else {
            throw new RuntimeException("invalid permission type");
        }
        Utils.applyGuid(permission);

        return (P) permission;
    }
}
