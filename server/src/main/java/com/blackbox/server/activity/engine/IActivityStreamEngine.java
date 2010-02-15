package com.blackbox.server.activity.engine;

import com.blackbox.activity.IActivity;
import com.blackbox.activity.AssociatedActivityFilterType;
import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivityThread;
import com.blackbox.EntityReference;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.Bounds;

import java.util.List;
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
