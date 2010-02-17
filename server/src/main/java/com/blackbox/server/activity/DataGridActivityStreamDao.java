package com.blackbox.server.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.*;
import com.blackbox.server.activity.engine.EngineFactory;
import com.blackbox.server.activity.engine.EngineType;
import com.blackbox.server.activity.engine.IActivityStreamEngine;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 */
@Repository("dataGridActivityStreamDao")
@SuppressWarnings("unchecked")
public class DataGridActivityStreamDao implements IActivityStreamDao {
    final private static Logger logger = LoggerFactory.getLogger(DataGridActivityStreamDao.class);

    @Override
    public IActivity getLatestActivity(EntityReference entity) {
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(EngineType.PERSONAL);
        return activityStreamEngine.getLastActivity(entity);
    }

    @Override
    public void setLatestActivity(IActivity activity) {
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(EngineType.PERSONAL);
        activityStreamEngine.setLastActivity(activity);
    }

    /**
     * Only loads the activities the entity published.
     *
     * @param entity
     * @param filterType
     * @param bounds
     * @return
     */
    @Override
    public Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds) {
        if (logger.isInfoEnabled()) {
            logger.info("called loadPublishedActivities");
        }
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(EngineType.PERSONAL);
        return activityStreamEngine.loadThreads(entity, filterType, bounds);
    }

    @Override
    public void deleteSender(IActivity activity) {
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(EngineType.PERSONAL);
        activityStreamEngine.removePrimary(activity, null, true);
    }

    /**
     * Deleted the full group of child activities
     *
     * @param activity
     * @param type     Type of relationship
     */
    @Override
    public void deleteAssociated(IActivity activity, NetworkTypeEnum type) {
        if (NetworkTypeEnum.ALL_MEMBERS.equals(type) || NetworkTypeEnum.WORLD.equals(type)) {
            EngineFactory.create(EngineType.GLOBAL).removeAssociated(activity, type);
        } else {
            EngineFactory.create(EngineType.FOLLOW).removeAssociated(activity, type);
            EngineFactory.create(EngineType.FRIEND).removeAssociated(activity, type);
        }
    }

    @Override
    public void deletePrimary(IActivity activity, NetworkTypeEnum type, boolean parent) {
        if (NetworkTypeEnum.ALL_MEMBERS.equals(type) || NetworkTypeEnum.WORLD.equals(type)) {
            EngineFactory.create(EngineType.GLOBAL).removePrimary(activity, type, parent);
        } else {
            EngineFactory.create(EngineType.FOLLOW).removePrimary(activity, type, parent);
            EngineFactory.create(EngineType.FRIEND).removePrimary(activity, type, parent);
        }
    }

    @Override
    public Collection<IActivityThread> loadActivities(EntityReference entity, Collection<NetworkTypeEnum> types, Bounds bounds) {
        if (logger.isInfoEnabled()) {
            logger.info("called loadActivities");
        }
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(types);
        return activityStreamEngine.loadThreads(entity, types, bounds);
    }

    @Override
    public void saveRecipient(IActivity activity, NetworkTypeEnum type) {
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(type);
        EntityReference recipient = activity.getRecipients().get(0).getRecipient();
        activityStreamEngine.save(activity, recipient);
    }

    @Override
    public void saveRecipient(List<IActivity> activities, NetworkTypeEnum type) {
        for (IActivity activity : activities) {
            saveRecipient(activity, type);
        }
    }

    @Override
    public void saveSender(List<IActivity> activities, EntityReference entity) {
        for (IActivity activity : activities) {
            saveSender(activity, entity);
        }
    }

    @Override
    public void saveSender(IActivity activityToSave, EntityReference entity) {
        IActivityStreamEngine activityStreamEngine = EngineFactory.create(activityToSave);
        activityStreamEngine.save(activityToSave, entity);
        //save to followers if needed
        if (NetworkTypeEnum.FRIENDS.equals(activityToSave.getRecipientDepth())) {
            //saveSender for personal/mebox
            activityStreamEngine = EngineFactory.create(EngineType.FOLLOW);
            activityStreamEngine.save(activityToSave, entity);
        }
        //saveSender for personal/mebox
        activityStreamEngine = EngineFactory.create(EngineType.PERSONAL);
        activityStreamEngine.save(activityToSave, entity);
    }

    @Override
    public Collection<IActivityThread> loadPublicActivity(int startIndex, int maxResults) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActivityThread loadActivityThreadByParentGuid(String guid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Activity activity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<IActivity> loadChildren(String parentGuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds, NetworkTypeEnum... types) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reindex() {
    }
}
