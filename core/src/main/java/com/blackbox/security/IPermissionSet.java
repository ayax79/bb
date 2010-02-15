/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import java.util.Set;

/**
 * An aggregate or all the Permissios.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IPermissionSet {

    public Set<SocialPermission> getSocialPermissions();

    public void setSocialPermissions(Set<SocialPermission> socialPermissions);

    public Set<ApplicationPermission> getApplicationPermissions();

    public void setApplicationPermissions(Set<ApplicationPermission> applicationPermissions);

}