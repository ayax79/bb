package com.blackbox.server.activity.engine;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 *
 *
 */
@SuppressWarnings("unchecked")
public abstract class BaseActivityStreamEngine implements IActivityStreamEngine {
    protected static final int PAGE_SIZE = 25;

    @Resource(name = "activityMetaDataDataStore")
    private ICacheManager<String, ActivityMetaData> activityMetaDataDataStore;

    public ICacheManager<String, ActivityMetaData> getActivityMetaDataDataStore() {
        return activityMetaDataDataStore;
    }

    public void setActivityMetaDataDataStore(ICacheManager<String, ActivityMetaData> activityMetaDataDataStore) {
        this.activityMetaDataDataStore = activityMetaDataDataStore;
    }

    protected ActivityMetaData getMetaData(String guid) {
        return activityMetaDataDataStore.get(guid);
    }

    protected void updatedMetaDataStore(String guid, ActivityMetaData metadata, Lock lock) {
        try {
            lock.lock();
            activityMetaDataDataStore.put(guid, metadata);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setLastActivity(IActivity activity) {
        String guid = activity.getSender().getGuid();
        ActivityMetaData metadata = getMetaData(guid);
        if (metadata == null) {
            metadata = new ActivityMetaData();
        }
        metadata.setLastestActivity(ActivityFactory.clone(activity));
        updatedMetaDataStore(guid, metadata, LockManager.locateMeBoxLock(guid));
    }

    @Override
    public IActivity getLastActivity(EntityReference entity) {
        IActivity lastActivity = null;
        String guid = entity.getGuid();
        ActivityMetaData metadata = getMetaData(guid);
        if (metadata != null) {
            lastActivity = metadata.getLastestActivity();
        }
        return lastActivity;
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds) {
        throw new UnsupportedOperationException("Not implemented...");
    }

    @Override
    public Collection<IActivityThread> loadThreads(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        throw new UnsupportedOperationException("not implemented...");
    }

    protected NavigableSet<IActivity> getStreamForReadingOnly(String entityGuid, ICacheManager<String,
            NavigableSet<IActivity>> dataStore,Comparator<IActivity> comparator
                                                              ) {
        NavigableSet<IActivity> stream = dataStore.get(entityGuid);
        if (stream == null) {
            stream = createNewSet(comparator);
        }
        return stream;
    }

    protected NavigableSet<IActivity> getStreamForReadingOnly(Integer key, ICacheManager<Integer, NavigableSet<IActivity>> dataStore,
                                                              Comparator<IActivity> comparator) {
        NavigableSet<IActivity> stream = dataStore.get(key);
        if (stream == null) {
            stream = createNewSet(comparator);
        }
        return stream;
    }

    protected NavigableSet<IActivity> createNewSet(Comparator<IActivity> comparator) {
        NavigableSet<IActivity> stream;
        stream = new TreeSet(comparator);
        return stream;
    }

    protected ActivityMetaData createMetaData(String metaDataKey) {
        ActivityMetaData data;
        Lock lock = locateMetaDataLock(metaDataKey);
        try {
            lock.lock();
            data = new ActivityMetaData();
            activityMetaDataDataStore.put(metaDataKey, data);
        } finally {
            lock.unlock();
        }
        return data;
    }

    @Override
    public void removePrimary(IActivity activity, NetworkTypeEnum type, boolean parent) {
        if (parent) {
            removeParent(activity, type);
        } else {
            removeComment(activity, type);
        }
    }

    @Override
    public void save(IActivity activity, EntityReference entity) {
        throw new UnsupportedOperationException("not implemented...");
    }

    @Override
    public void removeAssociated(IActivity activity, NetworkTypeEnum type) {
        throw new UnsupportedOperationException("not implemented...");
    }

    protected void removeComment(IActivity activity, NetworkTypeEnum type) {
        throw new UnsupportedOperationException("not implemented...");
    }

    protected void removeParent(IActivity activity, NetworkTypeEnum type) {
        throw new UnsupportedOperationException("not implemented...");
    }

    protected boolean removedActivityFromSet(IActivity activity, NavigableSet<IActivity> navigableSet) {
        boolean removed = false;
        Iterator<IActivity> iActivityIterator = navigableSet.iterator();
        while (!removed && iActivityIterator.hasNext()) {
            IActivity iActivity = iActivityIterator.next();
            if (iActivity.getGuid().equals(activity.getGuid())) {
                iActivityIterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    protected Lock locateMetaDataLock(String metaDataKey) {
        throw new UnsupportedOperationException("not implemented...");
    }

    protected void loadComments(IActivity parentActivity, ActivityThread activityThread, Collection<NetworkTypeEnum> types) {
        throw new UnsupportedOperationException("not implemented...");
    }
}
