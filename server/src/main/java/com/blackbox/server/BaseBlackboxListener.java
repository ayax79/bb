package com.blackbox.server;

import org.yestech.event.listener.IListener;
import org.yestech.event.event.IEvent;
import org.yestech.event.multicaster.IEventMulticaster;

/**
 *
 *
 */
public abstract class BaseBlackboxListener<EVENT extends IEvent, RESULT> implements IListener<EVENT, RESULT> {

    @Override
    public void setMulticaster(IEventMulticaster<EVENT, RESULT> eventresultiEventMulticaster) {
    }

    @Override
    public IEventMulticaster<EVENT, RESULT> getMulticaster() {
        return null;
    }

    @Override
    public void register() {
    }

    @Override
    public void deregister() {
    }
}
