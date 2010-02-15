/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.user.listener;

import org.yestech.event.listener.BaseAggragateListener;
import org.yestech.event.multicaster.IEventMulticaster;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;
import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.user.IUser;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(RegisterUserEvent.class)
@AsyncListener
public class RegistrationListener extends BaseAggragateListener<RegisterUserEvent, IUser> {
    public RegistrationListener() {
        super();
    }

    @Override
    public void setMulticaster(IEventMulticaster iEventMulticaster) {
    }

    @Override
    public IEventMulticaster getMulticaster() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deregister() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
