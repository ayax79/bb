/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import com.blackbox.EntityReference;
import com.blackbox.social.NetworkTypeEnum;

import java.util.Collection;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IActivityManager {

    Collection<IActivityThread> loadActivityThreads(ActivityRequest request);

    IActivity loadLastActivity(EntityReference owner);

    Collection<ActivityThread<IActivity>> loadAssociatedActivity(String guid,
                                                                 AssociatedActivityFilterType filterType,
                                                                 int startIndex,
                                                                 int maxResults);

    IActivityThread loadActivityThreadByParentGuid(String parentGuid);

    IActivity publish(Activity activity);

    Collection<IActivity> loadChildrenActivityByParent(String parentGuid);


    Collection<ActivityThread<IActivity>> loadAssociatedActivityFilterNetworkTypes(String guid,
                                                                                   AssociatedActivityFilterType filterType,
                                                                                   NetworkTypeEnum[] networkTypes,
                                                                                   int startIndex,
                                                                                   int maxResults);
}
