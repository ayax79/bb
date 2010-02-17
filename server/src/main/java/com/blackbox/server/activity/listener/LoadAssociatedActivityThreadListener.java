/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity.listener;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.activity.event.LoadAssociateActivityThreadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadAssociateActivityThreadEvent.class)
@SuppressWarnings("unchecked")
public class LoadAssociatedActivityThreadListener extends BaseBlackboxListener<LoadAssociateActivityThreadEvent, Collection<IActivityThread>> {
    final private static Logger logger = LoggerFactory.getLogger(LoadAssociatedActivityThreadListener.class);

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    @Override
    public void handle(LoadAssociateActivityThreadEvent loadAssociateActivityThreadEvent, ResultReference<Collection<IActivityThread>> result) {
        String guid = loadAssociateActivityThreadEvent.getType();
        EntityReference owner = EntityReference.createEntityReference(guid);
        result.setResult(activityStreamDao.loadPublishedActivities(owner, loadAssociateActivityThreadEvent.getFilterType(), loadAssociateActivityThreadEvent.getBounds()));

    }
}