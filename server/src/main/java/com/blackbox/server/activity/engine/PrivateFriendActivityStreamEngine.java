package com.blackbox.server.activity.engine;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.ActivityThread;
import com.blackbox.foundation.activity.DescendingActivityAndGuidComparator;
import com.blackbox.foundation.activity.IActivityThread;

import static com.blackbox.server.activity.engine.LockManager.locateFriendsPrimaryLock;
import com.blackbox.server.activity.ActivityUtil;
import static com.blackbox.server.activity.ActivityUtil.isParent;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.social.NetworkTypeEnum;
import static com.google.common.collect.Lists.newArrayList;

import javax.annotation.Resource;
import java.util.NavigableSet;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

import org.yestech.cache.ICacheManager;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
@Component("privateFriendActivityStreamEngine")
@SuppressWarnings("unchecked")
public class PrivateFriendActivityStreamEngine extends BasePrivateActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(PrivateFriendActivityStreamEngine.class);
    
    @Resource(name = "privateFriendsActivityDataStore")
    private ICacheManager<String, NavigableSet<IActivity>> privateFriendsActivityDataStore;

    public ICacheManager<String, NavigableSet<IActivity>> getPrivateFriendsActivityDataStore() {
        return privateFriendsActivityDataStore;
    }

    public void setPrivateFriendsActivityDataStore(ICacheManager<String, NavigableSet<IActivity>> privateFriendsActivityDataStore) {
        this.privateFriendsActivityDataStore = privateFriendsActivityDataStore;
    }

    @Override
    protected void removeParent(IActivity activity, NetworkTypeEnum type) {
        String senderGuid = activity.getSender().getGuid();
        removeActivity(activity, senderGuid, privateFriendsActivityDataStore, locateFriendsPrimaryLock(senderGuid));
    }

    @Override
    protected void removedAllAssociated(IActivity activity) {
        for (String guids : privateFriendsActivityDataStore.keys()) {
            removeActivity(activity, guids, privateFriendsActivityDataStore, locateFriendsPrimaryLock(guids));
        }
    }

    @Override
    public void save(IActivity activity, EntityReference entity) {
        String guid = entity.getGuid();
        IActivity trimmedActivity = ActivityUtil.trimForCache(activity);
        if (isParent(trimmedActivity)) {
            saveParent(trimmedActivity, guid);
        } else {
            saveChild(trimmedActivity);
        }
    }

    private void saveParent(IActivity activityToSave, String guid) {
        //TODO:  Add blocks
        Lock lock = locateFriendsPrimaryLock(guid);
        try {
            lock.lock();
            NavigableSet<IActivity> stream = privateFriendsActivityDataStore.get(guid);
            if (stream == null) {
                stream = createNewSet(DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
            }
            stream.add(activityToSave);
            privateFriendsActivityDataStore.put(guid, stream);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        String entityGuid = entity.getGuid();
        NavigableSet<IActivity> stream = getStreamForReadingOnly(entityGuid, privateFriendsActivityDataStore,
                DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
        List<IActivityThread> threads = newArrayList();
        if (stream != null) {
            int startIdx = bounds.getStartIndex();
            int totalThread = bounds.getMaxResults();
            int currentPosition = 0;
            int numberAdded = 0;
            Iterator<IActivity> listIterator = stream.iterator();
            while (listIterator.hasNext() && numberAdded < totalThread) {
                IActivity parentActivity = listIterator.next();
                if (currentPosition >= startIdx) {
                    ActivityThread activityThread = new ActivityThread();
                    activityThread.setParent(parentActivity);
                    loadComments(parentActivity, activityThread, types);
                    threads.add(activityThread);
                    ++numberAdded;
                }
                ++currentPosition;
            }
        }
        return threads;
    }
}