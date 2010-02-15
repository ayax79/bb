package com.blackbox.server.activity.engine;

import com.blackbox.activity.IActivity;
import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.DescendingActivityAndGuidComparator;
import com.blackbox.activity.IActivityThread;
import com.blackbox.EntityReference;
import static com.blackbox.server.activity.engine.LockManager.locateFollowersPrimaryLock;
import com.blackbox.server.activity.ActivityUtil;
import static com.blackbox.server.activity.ActivityUtil.isParent;
import com.blackbox.util.Bounds;
import com.blackbox.social.NetworkTypeEnum;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 *
 */
@Component("privateFollowActivityStreamEngine")
@SuppressWarnings("unchecked")
public class PrivateFollowActivityStreamEngine extends BasePrivateActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(PrivateFollowActivityStreamEngine.class);

    @Resource(name = "privateFollowingActivityDataStore")
    private ICacheManager<String, NavigableSet<IActivity>> privateFollowingActivityDataStore;

    public ICacheManager<String, NavigableSet<IActivity>> getPrivateFollowingActivityDataStore() {
        return privateFollowingActivityDataStore;
    }

    public void setPrivateFollowingActivityDataStore(ICacheManager<String, NavigableSet<IActivity>> privateFollowingActivityDataStore) {
        this.privateFollowingActivityDataStore = privateFollowingActivityDataStore;
    }

    @Override
    protected void removeParent(IActivity activity, NetworkTypeEnum type) {
        String senderGuid = activity.getSender().getGuid();
        removeActivity(activity, senderGuid, privateFollowingActivityDataStore, locateFollowersPrimaryLock(senderGuid));
    }

    @Override
    protected void removedAllAssociated(IActivity activity) {
        for (String guids : privateFollowingActivityDataStore.keys()) {
            removeActivity(activity, guids, privateFollowingActivityDataStore, locateFollowersPrimaryLock(guids));
        }
    }

    @Override
    public void save(IActivity activity, EntityReference entity) {
        String senderGuid = entity.getGuid();
        IActivity trimmedActivity = ActivityUtil.trimForCache(activity);
        if (isParent(trimmedActivity)) {
            //only saveSender if not the sender
//            if (!StringUtils.equals(senderGuid, trimmedActivity.getSender().getGuid())) {
                saveParent(trimmedActivity, senderGuid);
//            }
        } else {
            saveChild(trimmedActivity);
        }
    }


    private void saveParent(IActivity activityToSave, String guid) {
        //TODO:  Add blocks
        Lock lock = locateFollowersPrimaryLock(guid);
        try {
            lock.lock();
            NavigableSet<IActivity> stream = privateFollowingActivityDataStore.get(guid);
            if (stream == null) {
                stream = createNewSet(DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
            }
            stream.add(activityToSave);
            privateFollowingActivityDataStore.put(guid, stream);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        String entityGuid = entity.getGuid();
        NavigableSet<IActivity> stream = getStreamForReadingOnly(entityGuid, privateFollowingActivityDataStore,
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