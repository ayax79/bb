/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity.listener;

import com.blackbox.activity.ActivityRequest;
import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivityThread;
import com.blackbox.activity.IActivity;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.activity.event.LoadActivityThreadEvent;
import com.blackbox.util.Bounds;
import com.blackbox.social.NetworkTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.NavigableSet;
import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadActivityThreadEvent.class)
@SuppressWarnings("unchecked")
public class LoadActivityThreadListener extends BaseBlackboxListener<LoadActivityThreadEvent, Collection<IActivityThread>> {
    final private static Logger logger = LoggerFactory.getLogger(LoadActivityThreadListener.class);

    @Resource(name = "individualActivityThreadResultCache")
    private ICacheManager<ActivityThreadCacheKey, Collection<IActivityThread>> individualActivityThreadResultCache;

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    public void setIndividualActivityThreadResultCache(ICacheManager<ActivityThreadCacheKey, Collection<IActivityThread>> individualActivityThreadResultCache) {
        this.individualActivityThreadResultCache = individualActivityThreadResultCache;
    }

    @Override
    public void handle(LoadActivityThreadEvent loadActivityThreadEvent, ResultReference<Collection<IActivityThread>> result) {
        ActivityRequest request = loadActivityThreadEvent.getType();
        ActivityThreadCacheKey cacheKey = new ActivityThreadCacheKey(request.getOwner().getGuid(), request.getBounds(), request.getTypes());
        Collection<IActivityThread> iActivityThreadCollection = individualActivityThreadResultCache.get(cacheKey);
        if (iActivityThreadCollection == null) {
            iActivityThreadCollection = activityStreamDao.loadActivities(request.getOwner(), request.getTypes(), request.getBounds());
//            individualActivityThreadResultCache.put(cacheKey, iActivityThreadCollection);
        }
        result.setResult(iActivityThreadCollection);

    }

    private static class ActivityThreadCacheKey implements Serializable {

        private static final long serialVersionUID = 3318910437077611518L;
        private String guid;
        private Bounds criteria;
        private Collection<NetworkTypeEnum> types;
        private ActivityThreadCacheKey(String guid, Bounds criteria, Collection<NetworkTypeEnum> types) {
            this.guid = guid;
            this.criteria = criteria;
            this.types = types;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ActivityThreadCacheKey that = (ActivityThreadCacheKey) o;

            if (criteria != null ? !criteria.equals(that.criteria) : that.criteria != null) return false;
            if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;
            if (types != null ? !types.equals(that.types) : that.types != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = guid != null ? guid.hashCode() : 0;
            result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
            result = 31 * result + (types != null ? types.hashCode() : 0);
            return result;
        }

        public Collection<NetworkTypeEnum> getTypes() {

            return types;
        }

        public String getGuid() {
            return guid;
        }

        public Bounds getCriteria() {
            return criteria;
        }

        @Override
        public String toString() {
            return "ActivityThreadCacheKey{" +
                    "guid='" + guid + '\'' +
                    ", criteria=" + criteria +
                    ", types=" + types +
                    '}';
        }
    }

}