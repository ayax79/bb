package com.blackbox.server.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.Activity;
import com.blackbox.foundation.activity.AssociatedActivityFilterType;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.common.TwoBounds;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 */
public interface IActivityStreamDao {

    IActivity getLatestActivity(EntityReference entity);

    /**
     * Loads activities for owner and type using bounds
     *
     * @param bounds is a 2 pack
     * @return
     */
    Collection<IActivityThread> loadActivities(EntityReference owner, Collection<NetworkTypeEnum> types, TwoBounds bounds);

    Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds);

    Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds, NetworkTypeEnum... types);

    void saveSender(IActivity activity, EntityReference sender);

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

    void reindex();
}
