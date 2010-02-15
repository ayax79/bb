/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.security.event;

import com.blackbox.security.IBlackBoxAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@EventResultType(IBlackBoxAuthorizationInfo.class)
public class AuthorizationEvent extends BaseEvent<PrincipalCollection> {
    private static final long serialVersionUID = -4985817414233349686L;

    public AuthorizationEvent(PrincipalCollection principalCollection) {
        super(principalCollection);
    }
}
