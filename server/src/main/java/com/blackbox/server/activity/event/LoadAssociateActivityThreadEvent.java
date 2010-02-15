package com.blackbox.server.activity.event;

import com.blackbox.activity.AssociatedActivityFilterType;
import com.blackbox.util.Bounds;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadAssociateActivityThreadEvent extends BaseEvent<String> {

    private static final long serialVersionUID = 3756870162537974074L;
    private Bounds bounds;
    private AssociatedActivityFilterType filterType;

    public LoadAssociateActivityThreadEvent() {
    }

    public LoadAssociateActivityThreadEvent(String guid) {
        super(guid);
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public AssociatedActivityFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(AssociatedActivityFilterType filterType) {
        this.filterType = filterType;
    }
}