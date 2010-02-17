package com.blackbox.server.gifting.event;

import org.yestech.event.event.BaseEvent;
import com.blackbox.foundation.activity.IActivity;

/**
 * @author A.J. Wright
 */
public class CreateGiftEvent extends BaseEvent<IActivity> {
    public CreateGiftEvent(IActivity iActivity) {
        super(iActivity);
    }
}
