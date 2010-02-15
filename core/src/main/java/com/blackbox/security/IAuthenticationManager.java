/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 *
 */
public interface IAuthenticationManager {

    IBlackBoxAuthenticationInfo load(AuthenticationToken token);

    void logout(String userGuid);
}