/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.InetAddress;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "authenticationToken")
public class BlackBoxAuthenticationToken extends UsernamePasswordToken {

    private String guid;
    private SecurityRealmEnum realm;
    private static final long serialVersionUID = 6951648809261066829L;
    private String oldHash;

    public BlackBoxAuthenticationToken() {
    }

    public BlackBoxAuthenticationToken(String s, char[] chars) {
        super(s, chars);
    }

    public BlackBoxAuthenticationToken(String s, String s1) {
        super(s, s1);
    }

    public BlackBoxAuthenticationToken(String s, char[] chars, InetAddress inetAddress) {
        super(s, chars, inetAddress);
    }

    public BlackBoxAuthenticationToken(String s, String s1, InetAddress inetAddress) {
        super(s, s1, inetAddress);
    }

    public BlackBoxAuthenticationToken(String s, char[] chars, boolean b) {
        super(s, chars, b);
    }

    public BlackBoxAuthenticationToken(String s, String s1, boolean b) {
        super(s, s1, b);
    }

    public BlackBoxAuthenticationToken(String s, char[] chars, boolean b, InetAddress inetAddress) {
        super(s, chars, b, inetAddress);
    }

    public BlackBoxAuthenticationToken(String s, String s1, boolean b, InetAddress inetAddress) {
        super(s, s1, b, inetAddress);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public SecurityRealmEnum getRealm() {
        return realm;
    }

    public void setRealm(SecurityRealmEnum realm) {
        this.realm = realm;
    }

    public String getOldHash() {
        return oldHash;
    }

    public void setOldHash(String oldHash) {
        this.oldHash = oldHash;
    }
}
