/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.server.security;

import com.blackbox.security.ApplicationPermission;
import com.blackbox.security.BaseBBPermission;
import com.blackbox.security.SocialPermission;
import com.blackbox.security.Privacy;

/**
 *
 * @author boo
 */
public interface ISecurityDao {

    <K> K loadPermissionById(Class<? extends BaseBBPermission> clazz, String permissionGuid);

    ApplicationPermission save(ApplicationPermission permission);

    SocialPermission save(SocialPermission permission);

    Privacy loadPrivacy(String userGuid);
}
