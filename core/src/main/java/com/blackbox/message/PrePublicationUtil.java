package com.blackbox.message;

import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivity;
import com.blackbox.activity.IActivityThread;
import com.blackbox.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Pre-publication entails pushing a message to a cache then allowing for it to be fetched and pushed onto the activity
 * threads for that NetworkType. While this is happening, the current asynchronous implementation of publication will
 * push the message to the database, we will time out the cache, and the activity threads will be filled in from database
 * data.
 *
 * @author colin@blackboxrepublic.com
 */
public final class PrePublicationUtil {

    private static final Logger logger = LoggerFactory.getLogger(PrePublicationUtil.class);

    @SuppressWarnings({"unchecked"})
    public static Collection<IActivityThread> applyPrePublishedMessages(User user, Collection<IActivityThread> activityThreads, ICacheManager<String, Collection<Message>> prePublishedMessageCache) {
        Collection<com.blackbox.message.Message> prePublishedMessages = prePublishedMessageCache.get(user.getGuid());
        if (prePublishedMessages == null || prePublishedMessages.isEmpty()) {
            return activityThreads;
        }

        for (Message prePublishedMessage : prePublishedMessages) {
            // this handles case where server processed message before we were called. great job server!
            if (weHaveProcessedMessageAlready(prePublishedMessage, activityThreads)) {
                logger.debug("already proceed that one: " + prePublishedMessage);
                continue;
            }

            // this is the case where we have a message published that does not have an activity yet...
            if (prePublishedMessage.getParentActivity() == null) {
                bindMessageOntoStream(prePublishedMessage, activityThreads);
                continue;
            }

            // now, push the message onto activity for which the message is destined...
            for (IActivityThread activityThread : activityThreads) {
                if (activityThread.getParent().getGuid().equals(prePublishedMessage.getParentActivity().getGuid())) {
                    if (activityThread.getChildren().contains(prePublishedMessage)) {
                        continue; // let the cache time out drop the message...
                    } else if (activityThread.getNetworkTypes().contains(prePublishedMessage.getRecipientDepth())) {    // we have a cached item that has not been pulled through backend stream fetch so let's add it to correct network type stream...
                        activityThread.getChildren().add(prePublishedMessage);
                    }
                }
            }

        }
        List listed = newArrayList(activityThreads);
        Collections.sort(listed);
        return listed;
    }

    static void bindMessageOntoStream(Message prePublishedMessage, Collection<IActivityThread> activityThreads) {
        ActivityThread<IActivity> newActivityThread = new ActivityThread<IActivity>();
        newActivityThread.setParent(prePublishedMessage);
        newActivityThread.setNetworkTypes(newArrayList(prePublishedMessage.getRecipientDepth()));
        activityThreads.add(newActivityThread);
    }

    private static boolean weHaveProcessedMessageAlready(Message prePublishedMessage, Collection<IActivityThread> activityThreads) {
        for (IActivityThread activityThread : activityThreads) {
            if (activityThread.getParent() != null && activityThread.getParent().equals(prePublishedMessage)) {
                return true;
            }
        }
        return false;
    }

    public static void prePublish(Message message, ICacheManager<String, Collection<Message>> prePublishedMessageCache) {
        String owner = message.getArtifactMetaData().getArtifactOwner().getGuid();
        Collection<Message> messages = prePublishedMessageCache.get(owner);
        if (messages == null) {
            messages = new HashSet<Message>();
            prePublishedMessageCache.put(owner, messages);
        }
        messages.add(message);
    }


}
