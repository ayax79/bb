/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.social.NetworkTypeEnum;

import java.util.Collection;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IActivityManager {

    Collection<IActivityThread> loadActivityThreads(ActivityRequest request);

    Collection<ActivityThread<IActivity>> loadAssociatedActivityFilterNetworkTypes(String guid,
                                                                                   AssociatedActivityFilterType filterType,
                                                                                   NetworkTypeEnum[] networkTypes,
                                                                                   int startIndex,
                                                                                   int maxResults);

    IActivity loadLastActivity(EntityReference owner);

    Collection<ActivityThread<IActivity>> loadAssociatedActivity(String guid,
                                                                 AssociatedActivityFilterType filterType,
                                                                 int startIndex,
                                                                 int maxResults);

    IActivityThread loadActivityThreadByParentGuid(String parentGuid);

    IActivity publish(Activity activity);

    Collection<IActivity> loadChildrenActivityByParent(String parentGuid);

}
