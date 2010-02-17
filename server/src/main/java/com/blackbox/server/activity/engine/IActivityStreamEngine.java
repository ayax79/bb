package com.blackbox.server.activity.engine;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.AssociatedActivityFilterType;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;

import java.util.Collection;

/**
 *
 *
 */
public interface IActivityStreamEngine {
    void save(IActivity activity, EntityReference entity);

    IActivity getLastActivity(EntityReference entity);

    Collection<IActivityThread> loadThreads(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds);

    Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds);

    void removePrimary(IActivity activity, NetworkTypeEnum type, boolean parent);

    void removeAssociated(IActivity activity, NetworkTypeEnum type);

    void setLastActivity(IActivity activity);
}
