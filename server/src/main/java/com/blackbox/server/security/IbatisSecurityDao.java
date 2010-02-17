/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.server.security;

import com.blackbox.foundation.security.ApplicationPermission;
import com.blackbox.foundation.security.BaseBBPermission;
import com.blackbox.foundation.security.Privacy;
import com.blackbox.foundation.security.SocialPermission;
import com.blackbox.server.util.PersistenceUtil;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 *
 * @author boo
 */
@Repository("securityDao")
public class IbatisSecurityDao implements ISecurityDao {

    @Resource
    SqlSessionOperations template;

    @Override
    public SocialPermission save(SocialPermission permission) {
        PersistenceUtil.insertOrUpdate(permission, template,
                "Permission.insertSocialPermission", "Permission.updateSocialPermission");
        return permission;
    }

    @Override
    public ApplicationPermission save(ApplicationPermission permission) {
        PersistenceUtil.insertOrUpdate(permission, template,
                "Permission.insertApplicationPermission", "Permission.updateApplicationPermission");
        return permission;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <K> K loadPermissionById(Class<? extends BaseBBPermission> clazz, String permissionGuid) {
        if (SocialPermission.class.isAssignableFrom(clazz)) {
            return (K) template.selectOne("Permission.loadSocialPermissionByGuid", permissionGuid);
        }
        else if (ApplicationPermission.class.isAssignableFrom(clazz)) {
            return (K) template.selectOne("Permission.loadApplicationPermissionByGuid", permissionGuid);
        }
        throw new IllegalArgumentException("Unknown permission class: "+clazz); 
    }

    @Override
    public Privacy loadPrivacy(String userGuid) {
        return (Privacy) template.selectOne("Privacy.load", userGuid);
    }
}
