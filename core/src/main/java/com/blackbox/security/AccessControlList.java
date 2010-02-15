/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import com.blackbox.BBPersistentObject;
import com.blackbox.Utils;
import static com.blackbox.security.ApplicationPermission.cloneApplicationPermission;
import static com.blackbox.security.Role.cloneRole;
import static com.blackbox.security.SocialPermission.cloneSocialPermission;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Aggregate of all the Access Rights to a persistable Object.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "accessControlList")
@InstrumentedClass
public class AccessControlList extends BBPersistentObject implements Serializable {
    private static final long serialVersionUID = -1619140195479021338L;
    private Set<Role> roles;
    private Set<SocialPermission> socialPermissions;
    private Set<ApplicationPermission> applicationPermissions;

    public AccessControlList() {
//        roles = newHashSet();
//        socialPermissions = newHashSet();
//        applicationPermissions = newHashSet();
    }

    public Set<SocialPermission> getSocialPermissions() {
        return socialPermissions;
    }

    public void setSocialPermissions(Set<SocialPermission> socialPermissions) {
        this.socialPermissions = socialPermissions;
    }

    public Set<ApplicationPermission> getApplicationPermissions() {
        return applicationPermissions;
    }

    public void setApplicationPermissions(Set<ApplicationPermission> applicationPermissions) {
        this.applicationPermissions = applicationPermissions;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static AccessControlList cloneAccessControlList(AccessControlList acl) {
        AccessControlList acl2 = createAccessControlList();
        if (acl.applicationPermissions != null) {
            acl2.applicationPermissions = new HashSet<ApplicationPermission>(acl.applicationPermissions.size());
            for (ApplicationPermission applicationPermission : acl.applicationPermissions) {
                acl2.applicationPermissions.add(cloneApplicationPermission(applicationPermission));
            }
        }

        if (acl.roles != null) {
            acl2.roles = new HashSet<Role>(acl.roles.size());
            for (Role role : acl.roles) {
                acl2.roles.add(cloneRole(role));
            }
        }

        if (acl.socialPermissions != null) {
            acl2.socialPermissions = new HashSet<SocialPermission>(acl.socialPermissions.size());
            for (SocialPermission sp : acl.socialPermissions) {
                acl2.socialPermissions.add(cloneSocialPermission(sp));
            }
        }

        return acl2;
    }

    public static AccessControlList createAccessControlList() {
        AccessControlList acl = new AccessControlList();
        Utils.applyGuid(acl);
        return acl;
    }
}
