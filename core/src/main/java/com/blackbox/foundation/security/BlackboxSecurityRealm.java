/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.realm.AuthorizingRealm;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class BlackboxSecurityRealm extends AuthorizingRealm {

    private IAuthenticationManager authenticationManager;
    private IAuthorizationManager authorizationManager;

    public BlackboxSecurityRealm() {
        super(new BlackboxCredentialMatcher());
    }

    public IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    @Resource(name = "authorizationManager")
    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    public IAuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Resource(name = "authenticationManager")
    public void setAuthenticationManager(IAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return getAuthorizationManager().load(principalCollection);
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        @SuppressWarnings({"UnnecessaryLocalVariable"})
        IBlackBoxAuthenticationInfo info = getAuthenticationManager().load(authenticationToken);
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // just hack the damn thing to make it work.
        return true;
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        super.onLogout(principals);
        getAuthenticationManager().logout(SecurityUtil.getUser(principals).getGuid());
    }
}
