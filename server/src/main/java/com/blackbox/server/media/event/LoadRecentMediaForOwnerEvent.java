package com.blackbox.server.media.event;

import com.blackbox.EntityReference;
import org.yestech.event.event.IEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

/**
 *
 *
 */
@EventResultType(List.class)
public class LoadRecentMediaForOwnerEvent implements IEvent {

    private EntityReference owner;
    private int total;
    private static final long serialVersionUID = 8809147685385115341L;

    public LoadRecentMediaForOwnerEvent(EntityReference owner, int total) {
        this.owner = owner;
        this.total = total;
    }

    @Override
    public String getEventName() {
        return "LoadRecentMediaForOwnerEvent";
    }

    public EntityReference getOwner() {
        return owner;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadRecentMediaForOwnerEvent)) return false;

        LoadRecentMediaForOwnerEvent that = (LoadRecentMediaForOwnerEvent) o;

        if (total != that.total) return false;
        //noinspection RedundantIfStatement
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + total;
        return result;
    }

    @Override
    public String toString() {
        return "LoadRecentMediaForOwnerEvent{" +
                "owner=" + owner +
                ", total=" + total +
                '}';
    }
}
