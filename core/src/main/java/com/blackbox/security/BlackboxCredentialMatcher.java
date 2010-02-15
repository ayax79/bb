package com.blackbox.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author A.J. Wright
 */
public class BlackboxCredentialMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (token instanceof BlackBoxAuthenticationToken) {
            return super.doCredentialsMatch(token, info);
        } else if (token instanceof UsernamePasswordToken) {
            BlackBoxAuthenticationToken at = SecurityUtil.convertToken((UsernamePasswordToken) token);
            return super.doCredentialsMatch(at, info);
        } else if (token instanceof UsernameOnlyAuthToken) {
            return true;
        }
        return false;
    }
}
