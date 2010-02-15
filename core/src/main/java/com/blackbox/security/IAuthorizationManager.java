/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.security;

import org.apache.shiro.subject.PrincipalCollection;

/**
 *
 *
 */
public interface IAuthorizationManager {

    IBlackBoxAuthorizationInfo load(PrincipalCollection principalCollection);
}