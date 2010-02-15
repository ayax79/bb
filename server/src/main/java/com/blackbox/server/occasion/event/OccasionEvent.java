package com.blackbox.server.occasion.event;

import com.blackbox.occasion.Occasion;
import org.yestech.event.event.IEvent;

/**
 * @author A.J. Wright
 */
public interface OccasionEvent extends IEvent {

    Occasion getOccasion();

}
