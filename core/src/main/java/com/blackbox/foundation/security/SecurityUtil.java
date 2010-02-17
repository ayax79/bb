/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import com.blackbox.foundation.user.IUser;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.PrincipalCollection;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
final public class SecurityUtil {
    private static final String WILDCARD_SEPARATOR = ":";
    private static final String ALL_PERMISSION = "*";

    public static IUser getUser(PrincipalCollection principals) {
        return (IUser) principals.asSet().iterator().next();
    }

    public static IUser getUser(IBlackBoxAuthenticationInfo blackBoxAuthenticationInfo) {
        return getUser(blackBoxAuthenticationInfo.getPrincipals());
    }

    public static WildcardPermission convertPermission(SocialPermission permission) {
        return new WildcardPermission(convertPermissionToWildcardString(permission));
    }

    public static WildcardPermission convertPermission(ApplicationPermission permission) {
        return new WildcardPermission(convertPermissionToWildcardString(permission));
    }

    public static String convertPermissionToWildcardString(SocialPermission permission) {
        StringBuilder builder = new StringBuilder();
        builder.append(permission.getRelationshipType()).append(WILDCARD_SEPARATOR).append(permission.getDepth());
        builder.append(WILDCARD_SEPARATOR).append(permission.getRelationshipType());
        return builder.toString();
    }

    public static String convertPermissionToWildcardString(ApplicationPermission permission) {
        StringBuilder builder = new StringBuilder();
        builder.append(permission.getCategory()).append(WILDCARD_SEPARATOR).append(permission.getAccess());
        if (StringUtils.isNotBlank(permission.getField())) {
            builder.append(WILDCARD_SEPARATOR).append(permission.getField());
        } else {
            builder.append(WILDCARD_SEPARATOR).append(ALL_PERMISSION);
        }
        return builder.toString();
    }

    public static String convertPermissionToWildcardString(BaseBBPermission bbPermission) {
        String permission = null;
        if (checkPermission(bbPermission, PermissionTypeEnum.APPLICATION)) {
            permission = convertPermissionToWildcardString((ApplicationPermission) bbPermission);
        } else if (checkPermission(bbPermission, PermissionTypeEnum.SOCIAL)) {
            permission = convertPermissionToWildcardString((SocialPermission) bbPermission);
        }
        return permission;
    }

    public static WildcardPermission convertPermission(BaseBBPermission bbPermission) {
        WildcardPermission permission = null;
        if (checkPermission(bbPermission, PermissionTypeEnum.APPLICATION)) {
            permission = convertPermission((ApplicationPermission) bbPermission);
        } else if (checkPermission(bbPermission, PermissionTypeEnum.SOCIAL)) {
            permission = convertPermission((SocialPermission) bbPermission);
        }
        return permission;
    }

    public static boolean checkPermission(BaseBBPermission permission, PermissionTypeEnum permissionType) {
        if (permissionType == null) {
            throw new IllegalArgumentException("permission type can't be null");
        }
        if (permission == null) {
            throw new IllegalArgumentException("permission can't be null");
        }
        final PermissionTypeEnum permissionTypeEnum = permission.getType();
        return permissionTypeEnum.equals(permissionType);
    }

    public static BlackBoxAuthenticationToken convertToken(UsernamePasswordToken token) {
        BlackBoxAuthenticationToken bbToken = new BlackBoxAuthenticationToken(
                token.getUsername(), sha1Hash(new String(token.getPassword())));
        bbToken.setRealm(SecurityRealmEnum.WEB);
        return bbToken;
    }
}
