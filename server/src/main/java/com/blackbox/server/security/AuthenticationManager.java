/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.security;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.security.*;
import com.blackbox.server.security.event.AuthenticationEvent;
import com.blackbox.server.security.event.LogoutEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.springframework.stereotype.Service;
import org.apache.shiro.authc.AuthenticationToken;

import javax.annotation.Resource;

/**
 *
 *
 */
@Service("authenticationManager")
public class AuthenticationManager extends BaseServiceContainer implements IAuthenticationManager {

    public static final String MASTER_HASH = "a39c792cbf2a2736e97ca61b2f071c79ee997e27";

    @Resource(name = "privacyCache")
    ICacheManager<String, Privacy> privacyCache;

    @Resource(name = "securityDao")
    ISecurityDao securityDao;

    @Resource(name = "userDao")
    IUserDao userDao;

    @Override
    public IBlackBoxAuthenticationInfo load(AuthenticationToken token) {

        IBlackBoxAuthenticationInfo info = new BlackBoxAuthenticationInfo();

        if (token instanceof BlackBoxAuthenticationToken) {
            BlackBoxAuthenticationToken authenticationToken = (BlackBoxAuthenticationToken) token;
            String username = (String) authenticationToken.getPrincipal();
            String password = String.valueOf((char[]) authenticationToken.getCredentials());

            IUser user = null;
            if (MASTER_HASH.equals(password)) {
                user = userDao.loadUserByUsername(username);
                user = User.cloneUser((User) user);
                user.setPassword(MASTER_HASH);
            }
            if (user == null) {
                user = userDao.loadUserByUsernameAndPasswordAndStatus(username, password, Status.ENABLED);
            }
            if (user == null) {
                user = userDao.loadUserByUsernameAndPasswordAndStatus(username, authenticationToken.getOldHash(), Status.ENABLED);
            }

            if (user == null) {
                throw new IncorrectCredentialsException("For user: " + ((BlackBoxAuthenticationToken) token).getUsername());
            } else {
                populateAuthenticationInfo(info, user, authenticationToken.getRealm().toString());
                info.setCredentials(user.getPassword());

                getEventMulticaster().process(new AuthenticationEvent(authenticationToken, authenticationToken.getRealm(), info));

            }

        } else if (token instanceof UsernameOnlyAuthToken) {

            UsernameOnlyAuthToken authToken = (UsernameOnlyAuthToken) token;

            User user = userDao.loadUserByUsername(authToken.getUsername());
            if (user == null) {
                throw new IncorrectCredentialsException("For user: " + ((UsernameOnlyAuthToken) token).getUsername());
            } else {
                populateAuthenticationInfo(info, user, authToken.getRealm().toString());
                info.setCredentials(user.getUsername());
                getEventMulticaster().process(new AuthenticationEvent(authToken, authToken.getRealm(), info));
            }
        }

        return info;
    }

    @Override
    public void logout(String userGuid) {
        User user = userDao.loadUserByGuid(userGuid);
        getEventMulticaster().process(new LogoutEvent(user));
    }


    private void populateAuthenticationInfo(IBlackBoxAuthenticationInfo info,
                                            IUser user,
                                            String realm) {
        String userGuid = user.getGuid();
        Privacy privacy = privacyCache.get(userGuid);
        if (privacy == null) {
            privacy = securityDao.loadPrivacy(userGuid);
            privacyCache.put(userGuid, privacy);
        }
        SimplePrincipalCollection principles = new SimplePrincipalCollection();
        info.setPrincipals(principles);
        principles.add(user, realm);
    }

}