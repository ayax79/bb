package com.blackbox.util;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.activity.ActivityFactory;
import com.blackbox.foundation.activity.ActivityRequest;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.PrePublicationUtil;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Affirm;
import com.blackbox.foundation.util.Bounds;
import org.joda.time.DateTime;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.HashMapCacheManager;

import java.util.Arrays;
import java.util.Collection;

import static com.blackbox.foundation.social.NetworkTypeEnum.*;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author colin@blackboxrepublic.com
 */
public class MessagesHelper {

    private IMessageManager messageManager;
    private IActivityManager activityManager;
    private ICacheManager<String, Collection<Message>> prePublishedMessageCache;

    public MessagesHelper(IMessageManager messageManager, IActivityManager activityManager, ICacheManager<String, Collection<Message>> prePublishedMessageCache) {
        this.activityManager = Affirm.isNotNull(activityManager, "activityManager");
        this.messageManager = Affirm.isNotNull(messageManager, "messageManager");
        this.prePublishedMessageCache = Affirm.isNotNull(prePublishedMessageCache, "prePublishedMessageCache");
    }

    public MessagesHelper(IMessageManager messageManager, IActivityManager activityManager) {
        this(messageManager, activityManager, new HashMapCacheManager<String, Collection<Message>>());
    }

    public Message publishMessage(User poster, String body, NetworkTypeEnum depth) {
        Message message = ActivityFactory.createMessage();
        message.setBody(body);
        message.setOwnerGuid(poster.getGuid());
        message.setPostDate(new DateTime());
        message.setRecipientDepth(depth);
        message.setOwnerType(EntityType.USER.ordinal());
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        PrePublicationUtil.prePublish(message, prePublishedMessageCache);
        return messageManager.publish(message);
    }

    public void publishChildMessage(User poster, Message parentMessage, String body, NetworkTypeEnum depth) {
        Message message = ActivityFactory.createMessage();
        message.setParentGuid(parentMessage.getGuid());
        message.setBody(body);
        message.setOwnerGuid(poster.getGuid());
        message.setPostDate(new DateTime());
        message.setRecipientDepth(depth);
        message.setOwnerType(EntityType.USER.ordinal());
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        PrePublicationUtil.prePublish(message, prePublishedMessageCache);
        messageManager.publish(message);
    }

    public Collection<IActivityThread> fetchMessages(User viewer, NetworkTypeEnum... breadth) {
        return fetchMessages(viewer, Arrays.asList(breadth));
    }

    public Collection<IActivityThread> fetchMessages(User viewer, Collection<NetworkTypeEnum> breadth) {
        Collection<IActivityThread> serverActivitiesThread = activityManager.loadActivityThreads(new ActivityRequest(viewer.getEntityReference(), newArrayList(breadth), new Bounds(0, 10)));
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        return PrePublicationUtil.applyPrePublishedMessages(viewer, serverActivitiesThread, prePublishedMessageCache);
    }

    public Collection<IActivityThread> fetchFriendsMessages(User viewer) {
        return fetchMessages(viewer, newArrayList(FRIENDS));
    }

    public Collection<IActivityThread> fetchFollowingMessages(User viewer) {
        return fetchMessages(viewer, newArrayList(FOLLOWING));
    }

    public Collection<IActivityThread> fetchAllMessages(User viewer) {
        return fetchMessages(viewer, newArrayList(FRIENDS, FOLLOWING, ALL_MEMBERS, WORLD));
    }

}
