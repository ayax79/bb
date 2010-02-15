/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.security;

import com.blackbox.security.IAuthorizationManager;
import com.blackbox.security.IBlackBoxAuthorizationInfo;
import com.blackbox.server.security.event.AuthorizationEvent;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;
import org.yestech.event.multicaster.BaseServiceContainer;

/**
 *
 *
 */
@Service("authorizationManager")
public class AuthorizationManager extends BaseServiceContainer implements IAuthorizationManager
{
    @Override
    public IBlackBoxAuthorizationInfo load(PrincipalCollection principalCollection)
    {
        return (IBlackBoxAuthorizationInfo) getEventMulticaster().process(new AuthorizationEvent(principalCollection));
    }
}