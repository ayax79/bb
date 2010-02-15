package com.blackbox.server.occasion.event;

import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadLastExplorerEvent extends BaseEvent<String> {
    private static final long serialVersionUID = -6716424535311243343L;

    public LoadLastExplorerEvent() {
    }

    public LoadLastExplorerEvent(String s) {
        super(s);
    }
}
