package com.blackbox.server.social.event;

import org.yestech.event.event.BaseEvent;
import com.blackbox.social.Vouch;

public class VouchEvent extends BaseEvent<Vouch> {

    public VouchEvent(Vouch vouch) {
        super(vouch);
    }
}
