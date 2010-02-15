package com.blackbox.server.activity.engine;

import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivity;
import com.blackbox.activity.AscendingActivityAndGuidComparator;
import static com.blackbox.server.activity.engine.LockManager.locateNonGlobalChildLock;
import com.blackbox.social.NetworkTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.locks.Lock;

/**
 *
 *
 */
@SuppressWarnings("unchecked")
public abstract class BasePrivateActivityStreamEngine extends BaseActivityStreamEngine {
    final private static Logger logger = LoggerFactory.getLogger(BasePrivateActivityStreamEngine.class);
    
    @Resource(name = "privateChildActivityDataStore")
    private ICacheManager<String, NavigableSet<IActivity>> privateChildActivityDataStore;

    public ICacheManager<String, NavigableSet<IActivity>> getPrivateChildActivityDataStore() {
        return privateChildActivityDataStore;
    }

    public void setPrivateChildActivityDataStore(ICacheManager<String, NavigableSet<IActivity>> privateChildActivityDataStore) {
        this.privateChildActivityDataStore = privateChildActivityDataStore;
    }

    @Override
    protected void removeParent(IActivity activity, NetworkTypeEnum type) {
        throw new UnsupportedOperationException("Not Implemented...");
    }

    @Override
    public void removeAssociated(IActivity activity, NetworkTypeEnum type) {
        privateChildActivityDataStore.flush(activity.getGuid());
        removedAllAssociated(activity);
    }

    protected void removedAllAssociated(IActivity activity) {
        throw new UnsupportedOperationException("Not Implemented..");
    }

    protected void removeActivity(IActivity activity, String guidToCheck, ICacheManager<String, NavigableSet<IActivity>> dataStore, Lock lock) {
        //TODO:  Add blocks
        NavigableSet<IActivity> navigableSet = null;
        try {
            lock.lock();
            navigableSet = dataStore.get(guidToCheck);
            if (navigableSet != null) {
                boolean removed = removedActivityFromSet(activity, navigableSet);
                if (removed) {
                    dataStore.put(guidToCheck, navigableSet);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeComment(IActivity activity, NetworkTypeEnum type) {
        //TODO:  Add blocks
        String parentGuid = activity.getParentActivity().getGuid();
        //TODO:  Find better way to find
        //do a long search for it
        Lock lock = locateNonGlobalChildLock(parentGuid);
        try {
            lock.lock();
            NavigableSet<IActivity> navigableSet = privateChildActivityDataStore.get(parentGuid);
            if (navigableSet != null) {
                boolean removed = removedActivityFromSet(activity, navigableSet);
                if (removed) {
                    privateChildActivityDataStore.put(parentGuid, navigableSet);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    protected void saveChild(IActivity activity) {
        //TODO:  Add blocks
        String parentGuid = activity.getParentActivity().getGuid();
        Lock lock = locateNonGlobalChildLock(parentGuid);
        try {
            lock.lock();
            NavigableSet<IActivity> stream = privateChildActivityDataStore.get(parentGuid);
            if (stream == null) {
                stream = createNewSet(AscendingActivityAndGuidComparator.getAscendingActivityComparator());
            }
            stream.add(activity);
            privateChildActivityDataStore.put(parentGuid, stream);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void loadComments(IActivity parentActivity, ActivityThread activityThread, Collection<NetworkTypeEnum> types) {
        //TODO:  Add blocks
        if (logger.isInfoEnabled()) {
            logger.info("called loadComments");
        }
        //need to abstract out
        final String parentGuid = parentActivity.getGuid();
            final NavigableSet<IActivity> navigableSet = getPrivateChildActivityDataStore().get(parentGuid);
            if (navigableSet != null) {
                for (IActivity aNavigableSet : navigableSet) {
                    activityThread.addChild(aNavigableSet);
                }
            }

    }

}
