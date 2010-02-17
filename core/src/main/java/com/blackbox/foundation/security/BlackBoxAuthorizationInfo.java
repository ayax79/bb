/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import com.google.common.collect.ImmutableSet;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Set;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class BlackBoxAuthorizationInfo implements IBlackBoxAuthorizationInfo {
    private Set<String> roles;
    private Set<String> permissionsAsString;
    private Set<Permission> permissions;

    public void setRoles(Set<String> roles) {
        this.roles = ImmutableSet.copyOf(roles);
    }

    public void setPermissionsAsString(Set<String> permissionsAsString) {
        this.permissionsAsString = ImmutableSet.copyOf(permissionsAsString);
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = ImmutableSet.copyOf(permissions);
    }

    @Override
    public Collection<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<String> getStringPermissions() {
        return permissionsAsString;
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "BlackBoxAuthorizationInfo{" +
                "roles=" + roles +
                ", permissionsAsString=" + permissionsAsString +
                ", permissions=" + permissions +
                '}';
    }
}
