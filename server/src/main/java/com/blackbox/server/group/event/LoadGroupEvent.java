package com.blackbox.server.group.event;

import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadGroupEvent extends BaseEvent<String>
{
    private static final long serialVersionUID = 5656558850301798939L;

    public LoadGroupEvent(String guid)
    {
        super(guid);
    }
}
