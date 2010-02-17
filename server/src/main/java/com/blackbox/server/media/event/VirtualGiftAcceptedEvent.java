package com.blackbox.server.media.event;

import org.yestech.event.event.BaseEvent;
import com.blackbox.foundation.activity.IActivity;

public class VirtualGiftAcceptedEvent extends BaseEvent<IActivity> {

    public VirtualGiftAcceptedEvent(IActivity virtualGiftMeta) {
        super(virtualGiftMeta);
    }
}
