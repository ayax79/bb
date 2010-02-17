/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blackbox.server.activity;

import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaRecipient;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.social.Relationship;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


/**
 * @author boo
 */
public final class ActivityUtil {

    public static boolean isParent(IActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity can't be null");
        }
        return activity.getParentActivity() == null;
    }

    public static boolean isComment(IActivity activity) {
        return !isParent(activity);
    }

    public static boolean checkNetworkDepth(ActivityThread activityThread, NetworkTypeEnum networkDepth) {
        if (activityThread == null) {
            throw new IllegalArgumentException("activity can't be null");
        }
        final IActivity parentActivity = activityThread.getParent();
        return checkNetworkDepth(parentActivity, networkDepth);
    }

    public static boolean checkNetworkDepth(IActivity activity, NetworkTypeEnum networkDepth) {
        if (networkDepth == null) {
            throw new IllegalArgumentException("network depth can't be null");
        }
        if (activity == null) {
            throw new IllegalArgumentException("activity can't be null");
        }
        final NetworkTypeEnum recipientDepth = activity.getRecipientDepth();
        return networkDepth.equals(recipientDepth);
    }

    public static Message cloneActivity(Message message, Relationship relationship) {
        Message clonedMessage = Message.cloneMessage(message);
        ArrayList<IRecipient> iRecipients = newArrayList();
        iRecipients.add(new MessageRecipient(relationship.getToEntity()));
        clonedMessage.setRecipients(iRecipients);
        return clonedMessage;
    }

    public static MediaMetaData cloneActivity(MediaMetaData mediaMetaData, Relationship relationship) {
        MediaMetaData clonedMediaMetaData = MediaMetaData.cloneMediaMetaData(mediaMetaData);
        ArrayList<IRecipient> iRecipients = newArrayList();
        iRecipients.add(new MediaRecipient(relationship.getToEntity(), mediaMetaData));
        clonedMediaMetaData.setRecipients(iRecipients);
        return clonedMediaMetaData;
    }


    /**
     * Will convert a list of activity that contain children and parents to a list of ActivityThread object.
     * <p/>
     * Also processes out duplicates.
     *
     * @param flatActivities A list of flat activities.
     * @return a list of activity thread objects.
     */
    //TODO:  REMOVE HACK  didnt have time to clean up!!!!!
    public static <T extends IActivity> List<IActivityThread<T>> createActivityThreadListTyped(final List<T> flatActivities) {

        final ImmutableMultimap<String, T> mapped = Multimaps.index(flatActivities, new Function<T, String>() {
            @Override
            public String apply(T from) {
                ActivityReference parent = from.getParentActivity();
                if (parent != null && parent.getOwnerType() != null) {
                    return parent.getGuid();
                } else {
                    return "".intern();
                }
            }
        });

        List<String> guids = new ArrayList<String>();
        ImmutableCollection<T> rootList = mapped.get("".intern());
        List<IActivityThread<T>> threads = newArrayList();
        for (T from : rootList) {
            ImmutableCollection<T> children = mapped.get(from.getGuid());

            // skip duplicates
            if (guids.contains(from.getGuid())) {
                continue;
            }
            guids.add(from.getGuid());

            LinkedList<T> processedChildren = new LinkedList<T>();
            for (T child : children) {
                if (guids.contains(child.getGuid())) {
                    continue;
                }

                processedChildren.add(child);
                guids.add(child.getGuid());
            }

            IActivityThread<T> mt = new ActivityThread<T>(from, processedChildren);
            threads.add(mt);
        }
        return threads;
    }

    /**
     * Will convert a list of activity that contain children and parents to a list of ActivityThread object.
     * <p/>
     * Also processes out duplicates.
     *
     * @param flatActivities A list of flat activities.
     * @return a list of activity thread objects.
     */
    public static <T extends IActivity> List<IActivityThread> createActivityThreadList(final List<T> flatActivities) {

        final ImmutableMultimap<String, T> mapped = Multimaps.index(flatActivities, new Function<T, String>() {
            @Override
            public String apply(T from) {
                ActivityReference parent = from.getParentActivity();
                if (parent != null && parent.getOwnerType() != null) {
                    return parent.getGuid();
                } else {
                    return "".intern();
                }
            }
        });

        List<String> guids = new ArrayList<String>();
        ImmutableCollection<T> rootList = mapped.get("".intern());
        List<IActivityThread> threads = newArrayList();
        for (T from : rootList) {
            ImmutableCollection<T> children = mapped.get(from.getGuid());

            // skip duplicates
            if (guids.contains(from.getGuid())) {
                continue;
            }
            guids.add(from.getGuid());

            LinkedList<T> processedChildren = new LinkedList<T>();
            for (T child : children) {
                if (guids.contains(child.getGuid())) {
                    continue;
                }

                processedChildren.add(child);
                guids.add(child.getGuid());
            }

            IActivityThread<T> mt = new ActivityThread<T>(from, processedChildren);
            threads.add(mt);
        }
        return threads;
    }


    public static IActivity trimForCache(IActivity activityToTrim) {
        IActivity activity = null;
        try {
            if (activityToTrim instanceof Message) {
                activity = (IActivity) BeanUtils.cloneBean(activityToTrim);
            } else if (activityToTrim instanceof MediaMetaData) {
                activity = (IActivity) BeanUtils.cloneBean(activityToTrim);
            } else if (activityToTrim instanceof Occasion) {
                activity = (IActivity) BeanUtils.cloneBean(activityToTrim);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        return activity;

    }
}
