/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.security.event;

import com.blackbox.security.IBlackBoxAuthenticationInfo;
import com.blackbox.security.SecurityRealmEnum;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.IEvent;

import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@EventResultType(IBlackBoxAuthenticationInfo.class)
public class AuthenticationEvent implements IEvent {

    private final String eventName = UUID.randomUUID().toString();
    private AuthenticationToken authenticationToken;
    private SecurityRealmEnum realm;
    private IBlackBoxAuthenticationInfo authenticationInfo;

    public AuthenticationEvent(AuthenticationToken authenticationToken, SecurityRealmEnum realm,
                               IBlackBoxAuthenticationInfo authenticationInfo) {
        this.authenticationToken = authenticationToken;
        this.realm = realm;
        this.authenticationInfo = authenticationInfo;
    }

    public IBlackBoxAuthenticationInfo getAuthenticationInfo() {
        return authenticationInfo;
    }

    public String getEventName() {
        return eventName;
    }

    public AuthenticationToken getAuthenticationToken() {
        return authenticationToken;
    }

    public SecurityRealmEnum getRealm() {
        return realm;
    }

}
