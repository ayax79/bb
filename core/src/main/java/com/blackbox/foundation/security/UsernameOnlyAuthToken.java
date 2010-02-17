package com.blackbox.foundation.security;

import org.apache.shiro.authc.InetAuthenticationToken;

import java.net.InetAddress;

/** 
 * An auth token that only supplies a username.
 * This is used by registration when we want to auto login a user after they have completed registration.
 *
 * @author A.J. Wright
 */
public class UsernameOnlyAuthToken implements InetAuthenticationToken {

    private String username;
    private InetAddress inetAddress;
    private SecurityRealmEnum realm;

    public UsernameOnlyAuthToken() {
    }

    public UsernameOnlyAuthToken(String username) {
        this.username = username;
    }

    public UsernameOnlyAuthToken(String username, InetAddress inetAddress) {
        this.username = username;
        this.inetAddress = inetAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getUsername();
    }

    public SecurityRealmEnum getRealm() {
        return realm;
    }

    public void setRealm(SecurityRealmEnum realm) {
        this.realm = realm;
    }
}
