package com.blackbox.server.activity;

import com.blackbox.EntityReference;
import com.blackbox.activity.Activity;
import com.blackbox.activity.AssociatedActivityFilterType;
import com.blackbox.activity.IActivity;
import com.blackbox.activity.IActivityThread;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.Bounds;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 */
public interface IActivityStreamDao {
    IActivity getLatestActivity(EntityReference entity);

    Collection<IActivityThread> loadActivities(EntityReference owner, Collection<NetworkTypeEnum> types, Bounds bounds);

    void saveSender(IActivity activity, EntityReference sender);

    Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds);

    void deletePrimary(IActivity activity, NetworkTypeEnum type, boolean parent);

    void saveSender(List<IActivity> activities, EntityReference sender);

    void saveRecipient(List<IActivity> activities, NetworkTypeEnum type);

    void saveRecipient(IActivity activity, NetworkTypeEnum type);

    void deleteSender(IActivity activity);

    void deleteAssociated(IActivity activity, NetworkTypeEnum type);

    void setLatestActivity(IActivity activity);

    Collection<IActivityThread> loadPublicActivity(int startIndex, int maxResults);

    IActivityThread loadActivityThreadByParentGuid(String guid);

    void save(Activity activity);

    Collection<IActivity> loadChildren(String parentGuid);

    Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds, NetworkTypeEnum... types);

    void reindex();
}
