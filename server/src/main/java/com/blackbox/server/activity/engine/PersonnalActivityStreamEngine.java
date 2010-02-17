package com.blackbox.server.activity.engine;

import com.blackbox.foundation.EntityReference;
import static com.blackbox.foundation.activity.AscendingActivityComparator.getAscendingActivityComparator;

import com.blackbox.foundation.activity.AssociatedActivityFilterType;
import static com.blackbox.foundation.activity.DescendingActivityThreadComparator.getDescendingActivityThreadComparator;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import static com.blackbox.server.activity.ActivityUtil.createActivityThreadList;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.foundation.util.PaginationUtil;
import com.blackbox.foundation.social.NetworkTypeEnum;
import static com.blackbox.foundation.social.NetworkTypeEnum.*;
import com.blackbox.foundation.util.Bounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.sort;
import java.util.List;
import java.util.NavigableSet;

/**
 *
 *
 */
@Component("personnalActivityStreamEngine")
@SuppressWarnings("unchecked")
public class PersonnalActivityStreamEngine extends BasePrivateActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(PersonnalActivityStreamEngine.class);

    @Resource(name = "messageDao")
    private IMessageDao messageDao;

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Resource(name = "privateActivityDataStore")
    private ICacheManager<String, NavigableSet<IActivity>> privateActivityDataStore;

    public ICacheManager<String, NavigableSet<IActivity>> getPrivateActivityDataStore() {
        return privateActivityDataStore;
    }

    public void setPrivateActivityDataStore(ICacheManager<String, NavigableSet<IActivity>> privateActivityDataStore) {
        this.privateActivityDataStore = privateActivityDataStore;
    }

    public IMessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public IMediaDao getMediaDao() {
        return mediaDao;
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    @Override
    public void removePrimary(IActivity activity, NetworkTypeEnum type, boolean parent) {
//        String senderGuid = activity.getSender().getGuid();
//        Lock lock = locateMeBoxLock(senderGuid);
//        try {
//            lock.lock();
//            NavigableSet<IActivity> stream = privateActivityDataStore.get(senderGuid);
//            if (stream != null) {
//                boolean removed = removedActivityFromSet(activity, stream);
//                if (removed) {
//                    privateActivityDataStore.put(senderGuid, stream);
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
    }

    @Override
    public void removeComment(IActivity activity, NetworkTypeEnum type) {
//        String senderGuid = activity.getSender().getGuid();
//        //remove mebox
//        Lock lock = locateMeBoxLock(senderGuid);
//        try {
//            lock.lock();
//            NavigableSet<IActivity> navigableSet = privateActivityDataStore.get(senderGuid);
//            if (navigableSet != null) {
//                boolean removed = removedActivityFromSet(activity, navigableSet);
//                if (removed) {
//                    privateActivityDataStore.put(senderGuid, navigableSet);
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
    }

    @Override
    public void save(IActivity activity, EntityReference entity) {
//        String guid = entity.getGuid();
//        Lock lock = locateMeBoxLock(guid);
//        try {
//            lock.lock();
//            NavigableSet<IActivity> stream = privateActivityDataStore.get(guid);
//            if (stream == null) {
//                stream = createNewSet(DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
//            }
//            stream.add(activity);
//            privateActivityDataStore.put(guid, stream);
//        } finally {
//            lock.unlock();
//        }
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds) {
        String guid = entity.getGuid();
        List<Message> list0 = messageDao.loadAllAssociatedMessages(guid, ALL_MEMBERS, FRIENDS, WORLD);
        List<MediaMetaData> list1 = mediaDao.loadAllAssociatedMedia(guid, ALL_MEMBERS, FRIENDS, WORLD);
        List<IActivity> union = new ArrayList<IActivity>(list0.size() + list1.size());
        filterActivity(filterType, guid, list0, union);
        filterActivity(filterType, guid, list1, union);
        sort(union, getAscendingActivityComparator());
        List<IActivityThread> threads = createActivityThreadList(union);
        sort(threads, getDescendingActivityThreadComparator());
        int startIndex = bounds.getStartIndex();
        int maxResults = bounds.getMaxResults();
        return PaginationUtil.subList(threads, startIndex, maxResults);


        //        String entityGuid = entity.getGuid();
//        NavigableSet<IActivity> stream = getStreamForReadingOnly(entityGuid, privateActivityDataStore, DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
//        int startIdx = bounds.getStartIndex();
//        int totalThread = bounds.getMaxResults();
//        List<ActivityThread> threads = newArrayList();
//        int currentPosition = 0;
//        int numberAdded = 0;
//        Iterator<IActivity> listIterator = stream.iterator();
//        while (listIterator.hasNext() && numberAdded < totalThread) {
//            IActivity parentActivity = listIterator.next();
//            if (currentPosition >= startIdx) {
//                ActivityThread activityThread = new ActivityThread();
//                activityThread.setParent(parentActivity);
//
//                // if we are filtering by owner don't add things we aren't the owner for
//                // vice versa for participant
//                if (filterType == AssociatedActivityFilterType.OWNER &&
//                        !parentActivity.getSender().getGuid().equals(entityGuid)) {
//                    continue;
//                } else if (filterType == AssociatedActivityFilterType.PARTICIPANT &&
//                        parentActivity.getSender().getGuid().equals(entityGuid)) {
//                    continue;
//                }
//
//                threads.add(activityThread);
//                ++numberAdded;
//            }
//            ++currentPosition;
//        }
//        return threads;
    }

    private <T extends IActivity> void filterActivity(AssociatedActivityFilterType filterType, String guid, List<T> fromList, List<IActivity> toList) {
        for (IActivity activity : fromList) {
            // if we are filtering by owner don't add things we aren't the owner for
            // vice versa for participant
            if (filterType == AssociatedActivityFilterType.OWNER &&
                    !activity.getSender().getGuid().equals(guid)) {
                continue;
            } else if (filterType == AssociatedActivityFilterType.PARTICIPANT &&
                    activity.getSender().getGuid().equals(guid)) {
                continue;
            }
            toList.add(activity);
        }
    }
}