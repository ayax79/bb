/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.security.event;

import com.blackbox.foundation.user.IUser;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 * represents the logout event
 * 
 * @author Artie Copeland
 * @version $Revision: $
 */
@EventResultType(IUser.class)
public class LogoutEvent extends BaseEvent<IUser> {
    private static final long serialVersionUID = -6475251948798665375L;

    public LogoutEvent(IUser user) {
        super(user);
    }

}