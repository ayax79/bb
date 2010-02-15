package com.blackbox.server.activity.engine;

import com.blackbox.EntityReference;
import com.blackbox.activity.*;
import com.blackbox.server.activity.ActivityUtil;
import static com.blackbox.server.activity.ActivityUtil.isParent;
import static com.blackbox.server.activity.engine.LockManager.*;
import static com.blackbox.server.activity.engine.LockManager.locateGlobalLock;
import static com.blackbox.server.activity.engine.LockManager.locateGlobalChildMetaDataLock;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.Bounds;
import static com.google.common.collect.Lists.newArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.locks.Lock;

/**
 *
 *
 */
@Component("globalActivityStreamEngine")
@SuppressWarnings("unchecked")
public class GlobalActivityStreamEngine extends BaseActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(GlobalActivityStreamEngine.class);
    private int totalGlobalItems = 1000;
    private int totalGlobalBucketSizes = PAGE_SIZE;

    @Resource(name = "globalActivityDataStore")
    private ICacheManager<Integer, NavigableSet<IActivity>> globalActivityDataStore;

    @Resource(name = "globalChildActivityDataStore")
    private ICacheManager<String, NavigableSet<IActivity>> globalChildActivityDataStore;


    public int getTotalGlobalItems() {
        return totalGlobalItems;
    }

    public void setTotalGlobalItems(int totalGlobalItems) {
        this.totalGlobalItems = totalGlobalItems;
    }

    public int getTotalGlobalBucketSizes() {
        return totalGlobalBucketSizes;
    }

    public void setTotalGlobalBucketSizes(int totalGlobalBucketSizes) {
        this.totalGlobalBucketSizes = totalGlobalBucketSizes;
    }

    public ICacheManager<Integer, NavigableSet<IActivity>> getGlobalActivityDataStore() {
        return globalActivityDataStore;
    }

    public void setGlobalActivityDataStore(ICacheManager<Integer, NavigableSet<IActivity>> globalActivityDataStore) {
        this.globalActivityDataStore = globalActivityDataStore;
    }

    public ICacheManager<String, NavigableSet<IActivity>> getGlobalChildActivityDataStore() {
        return globalChildActivityDataStore;
    }

    public void setGlobalChildActivityDataStore(ICacheManager<String, NavigableSet<IActivity>> globalChildActivityDataStore) {
        this.globalChildActivityDataStore = globalChildActivityDataStore;
    }

    @Override
    protected Lock locateMetaDataLock(String metaDataKey) {
        return locateGlobalChildMetaDataLock(metaDataKey);
    }

    @Override
    protected void removeComment(IActivity activity, NetworkTypeEnum type) {
        String parentActivityGuid = activity.getParentActivity().getGuid();
        String globalChildMetaDataKey = generateGlobalChildMetaDataKey(parentActivityGuid);
        //TODO:  Find better way to find
        //do a long search for it
        ActivityMetaData data = getActivityMetaDataDataStore().get(globalChildMetaDataKey);
        int lastGlobalBucketIdentifier = data.getGlobalChildLastBucketIdentifier();
        boolean removed = false;
        int currentBucketIdentifier = 0;
        while (!removed && currentBucketIdentifier <= lastGlobalBucketIdentifier) {
            Lock lock = locateGlobalChildLock(parentActivityGuid);
            try {
                String cacheKey = generateGlobalChildBucketKey(parentActivityGuid, currentBucketIdentifier);
                NavigableSet<IActivity> navigableSet = globalChildActivityDataStore.get(cacheKey);
                if (navigableSet != null) {
                    removed = removedActivityFromSet(activity, navigableSet);
                    if (removed) {
                        globalChildActivityDataStore.put(cacheKey, navigableSet);
                    }
                }
            } finally {
                lock.unlock();
            }
            ++currentBucketIdentifier;
        }
    }

    @Override
    public void removeAssociated(IActivity activity, NetworkTypeEnum type) {
        globalChildActivityDataStore.flush(activity.getGuid());
    }

    @Override
    protected void removeParent(IActivity activity, NetworkTypeEnum type) {
        //TODO:  Find better way to find
        //do a long search for it
        ActivityMetaData data = getActivityMetaDataDataStore().get(LockManager.GLOBAL_KEY);
        int lastGlobalBucketIdentifier = data.getGlobalParentLastBucketIdentifier();
        boolean removed = false;
        int currentBucketIdentifier = 0;
        while (!removed && currentBucketIdentifier <= lastGlobalBucketIdentifier) {
            Lock lock = locateGlobalLock();
            try {
                lock.lock();
                NavigableSet<IActivity> navigableSet = globalActivityDataStore.get(currentBucketIdentifier);
                if (navigableSet != null) {
                    removed = removedActivityFromSet(activity, navigableSet);
                    if (removed) {
                        globalActivityDataStore.put(currentBucketIdentifier, navigableSet);
                    }
                }
            } finally {
                lock.unlock();
            }
            ++currentBucketIdentifier;
        }
    }

    @Override
    public void save(IActivity activity, EntityReference entity) {
        IActivity trimmedActivity = ActivityUtil.trimForCache(activity);
        if (isParent(trimmedActivity)) {
            saveParent(trimmedActivity);
        } else {
            saveChild(trimmedActivity);
        }
    }

    private String generateGlobalChildBucketKey(final String guid, int childLastBucketIdentifier) {
        return guid + childLastBucketIdentifier;
    }

    private void saveParent(IActivity activityToSave) {
        Lock lock = locateGlobalLock();
        try {
            lock.lock();
            ActivityMetaData data = getActivityMetaDataDataStore().get(LockManager.GLOBAL_KEY);
            if (data == null) {
                data = new ActivityMetaData();
            }
            int lastBucketIdentifier = data.getGlobalParentLastBucketIdentifier();
            NavigableSet<IActivity> stream = globalActivityDataStore.get(lastBucketIdentifier);
            int lastBucketSize = data.getGlobalParentLastBucketSize();
            if (lastBucketIdentifier < 0 || lastBucketSize >= totalGlobalBucketSizes) {
                stream = createNewSet(DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
                lastBucketSize = 1;
                ++lastBucketIdentifier;
            } else {
                ++lastBucketSize;
            }
            stream.add(activityToSave);
            globalActivityDataStore.put(lastBucketIdentifier, stream);
            data.setGlobalParentLastBucketSize(lastBucketSize);
            data.setGlobalParentLastBucketIdentifier(lastBucketIdentifier);
            updatedMetaDataStore(LockManager.GLOBAL_KEY, data, locateGlobalLock());
        } finally {
            lock.unlock();
        }
    }

    private void saveChild(IActivity activity) {
        String parentActivityGuid = activity.getParentActivity().getGuid();
        Lock lock = locateGlobalChildLock(parentActivityGuid);
        try {
            lock.lock();
            String globalChildMetaDataKey = generateGlobalChildMetaDataKey(parentActivityGuid);
            ActivityMetaData data = getActivityMetaDataDataStore().get(globalChildMetaDataKey);
            if (data == null) {
                data = new ActivityMetaData();
            }
            int lastBucketIdentifier = data.getGlobalChildLastBucketIdentifier();
            NavigableSet<IActivity> stream = globalChildActivityDataStore.get(generateGlobalChildBucketKey(parentActivityGuid, lastBucketIdentifier));
            int lastBucketSize = data.getGlobalChildLastBucketSize();
            if (stream == null || lastBucketIdentifier < 0 || lastBucketSize >= totalGlobalBucketSizes) {
                stream = createNewSet(AscendingActivityAndGuidComparator.getAscendingActivityComparator());
                lastBucketSize = 1;
                ++lastBucketIdentifier;
            } else {
                ++lastBucketSize;
            }
            stream.add(activity);
            globalChildActivityDataStore.put(generateGlobalChildBucketKey(parentActivityGuid, lastBucketIdentifier), stream);
            data.setGlobalChildLastBucketSize(lastBucketSize);
            data.setGlobalChildLastBucketIdentifier(lastBucketIdentifier);
            //saveSender metadata
            updatedMetaDataStore(globalChildMetaDataKey, data, locateGlobalChildMetaDataLock(parentActivityGuid));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        ActivityMetaData data = getActivityMetaDataDataStore().get(LockManager.GLOBAL_KEY);
        if (data == null) {
            data = new ActivityMetaData();
        }
        int currentParentBucketIdentifier = data.getGlobalParentLastBucketIdentifier();
        List<IActivityThread> threads = newArrayList();
        int startIdx = bounds.getStartIndex();
        int totalThread = bounds.getMaxResults();
        int currentPosition = 0;
        int numberAdded = 0;
        while (numberAdded < totalThread && currentParentBucketIdentifier > -1) {
            NavigableSet<IActivity> stream = getStreamForReadingOnly(currentParentBucketIdentifier, globalActivityDataStore,
                    DescendingActivityAndGuidComparator.getDescendingActivityAndGuidComparator());
            if (stream != null) {
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
            --currentParentBucketIdentifier;
        }
        return threads;
    }

    @Override
    protected void loadComments(IActivity parentActivity, ActivityThread activityThread, Collection<NetworkTypeEnum> types) {
        if (logger.isInfoEnabled()) {
            logger.info("called loadComments");
        }
        //need to abstract out
        final String parentActivityGuid = parentActivity.getGuid();
        final String globalChildMetaDataKey = generateGlobalChildMetaDataKey(parentActivityGuid);
        ActivityMetaData data = getActivityMetaDataDataStore().get(globalChildMetaDataKey);
        if (data == null) {
            data = createMetaData(globalChildMetaDataKey);
        }
        int childLastBucketIdentifier = data.getGlobalChildLastBucketIdentifier();
        int totalFound = 0;
        while (totalFound < 5 && childLastBucketIdentifier > -1) {
            String generateGlobalChildBucketKey = generateGlobalChildBucketKey(parentActivityGuid, childLastBucketIdentifier);
            final NavigableSet<IActivity> navigableSet = globalChildActivityDataStore.get(generateGlobalChildBucketKey);
            if (navigableSet != null) {
                for (IActivity aNavigableSet : navigableSet) {
                    activityThread.addChild(aNavigableSet);
                    ++totalFound;
                }
            }
            --childLastBucketIdentifier;
        }
    }
}
