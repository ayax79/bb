/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity;

import com.blackbox.foundation.common.ActivitySenderOwnerPredicate;
import com.blackbox.foundation.common.ActivityThreadsMessageOwnerPredicate;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntityManager;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.message.IMessageManager;

import com.blackbox.server.activity.event.LoadActivityThreadEvent;
import com.blackbox.server.activity.event.LoadAssociateActivityThreadEvent;
import com.blackbox.server.activity.event.LoadLastActivityEvent;
import com.blackbox.server.activity.event.PublishActivityEvent;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
@Service("activityManager")
public class ActivityManager extends BaseServiceContainer implements IActivityManager {

    private static final Logger logger = LoggerFactory.getLogger(ActivityManager.class);

    @Resource
    IActivityStreamDao activityStreamDao;

    @Resource(name = "trainRockCommentCache")
    ICacheManager<String, List<TrainRockComment>> trainRockCommentCache;

    @Resource
    IMessageDao messageDao;

    @Resource
    IMediaDao mediaDao;

    @Resource
    IMediaManager mediaManager;

    @Resource
    IMessageManager messageManager;

    @Resource
    ISocialManager socialManager;

    @Resource
    IEntityManager entityManager;

    /**
     * load activity thread owned by owner and to a specific NetworkType
     */
    @Override
    public Collection<IActivityThread> loadActivityThreads(ActivityRequest request) {
        Collection<IActivityThread> threads = (Collection<IActivityThread>) getEventMulticaster().process(new LoadActivityThreadEvent(request));
        logger.debug(MessageFormat.format("For user [{0}], all threads [{1}]", request.getOwner().getGuid(), threads));
        return inflateStreamParts(trimIgnoredUserStuff(request, threads));
    }

    private Collection<IActivityThread> trimIgnoredUserStuff(ActivityRequest request, Collection<IActivityThread> threads) {
        List<EntityReference> ignored = loadIgnoresOnlyForAllMembersRequest(request);
        threads = Collections2.filter(threads, Predicates.not(new ActivityThreadsMessageOwnerPredicate(ignored)));
        for (IActivityThread thread : threads) {
            thread.setChildren(Collections2.filter(thread.getChildren(), Predicates.not(new ActivitySenderOwnerPredicate(ignored))));
        }
        return threads;
    }

    /**
     * Builds out a list of blocks (read: hard ignore) or an empty list should the request have not been for ALL_MEMBERS
     * For any non-all members request, the hard ignore functionality of deleting relationships will drop those from contention
     * way before this filtering is necessary.
     */
    private List<EntityReference> loadIgnoresOnlyForAllMembersRequest(ActivityRequest request) {
        return request.getTypes().contains(NetworkTypeEnum.ALL_MEMBERS) ? socialManager.loadBlocks(request.getOwner().getGuid()) : Collections.<EntityReference>emptyList();
    }

    private Collection<IActivityThread> inflateStreamParts(Collection<IActivityThread> threads) {
        for (IActivityThread thread : threads) {
            IActivity parentalUnit = thread.getParent();
            if (parentalUnit instanceof IVirtualGiftable) {
                EntityType type = (parentalUnit.getActivityType() == ActivityTypeEnum.MESSAGE ? EntityType.MESSAGE : EntityType.MEDIA);
                parentalUnit = (IActivity) entityManager.loadEntity(parentalUnit.getGuid(), type);
                thread.setParent(parentalUnit);
            } else {
                logger.info(MessageFormat.format("Dropping thread from activity thread because it [{0}] is not an IVirtualGiftable", parentalUnit));
            }
        }
        return threads;
    }

    @Override
    public IActivityThread loadActivityThreadByParentGuid(String guid) {
        return activityStreamDao.loadActivityThreadByParentGuid(guid);
    }

    @Override
    public Collection<ActivityThread<IActivity>> loadAssociatedActivity(String guid,
                                                                        AssociatedActivityFilterType filterType,
                                                                        int startIndex, int maxResults) {

        LoadAssociateActivityThreadEvent event = new LoadAssociateActivityThreadEvent(guid);
        Bounds bounds = new Bounds(startIndex, maxResults);
        event.setBounds(bounds);
        return (Collection<ActivityThread<IActivity>>) getEventMulticaster().process(event);
    }

    public Collection<ActivityThread<IActivity>> loadAssociatedActivityFilterNetworkTypes(String guid,
                                                                                          AssociatedActivityFilterType filterType,
                                                                                          NetworkTypeEnum[] networkTypes,
                                                                                          int startIndex, int maxResults) {

        Bounds bounds = new Bounds(startIndex, maxResults);

        // hack: casted down so I can return via the impl
        // noinspection RedundantCast
        return (Collection) activityStreamDao.loadPublishedActivities(
                new EntityReference(EntityType.USER, guid), filterType, bounds, networkTypes);
    }


    public IActivity loadLastActivity(EntityReference owner) {
        LoadLastActivityEvent event = new LoadLastActivityEvent(owner);
        return (IActivity) getEventMulticaster().process(event);
    }

    @Override
    public IActivity publish(Activity activity) {
        if (activity.getGuid() == null) {
            Utils.applyGuid(activity);
        }

        PublishActivityEvent publishActivityEvent = new PublishActivityEvent(activity);
        activity = (Activity) getEventMulticaster().process(publishActivityEvent);
        return activity;
    }

    @Override
    public Collection<IActivity> loadChildrenActivityByParent(String parentGuid) {
        Collection<IActivity> children = activityStreamDao.loadChildren(parentGuid);
        if (children == null) {
            children = newArrayList();
        }
        return children;
    }
}
