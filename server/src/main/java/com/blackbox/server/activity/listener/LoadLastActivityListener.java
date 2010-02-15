/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity.listener;

import com.blackbox.EntityReference;
import com.blackbox.activity.IActivity;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.activity.event.LoadLastActivityEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadLastActivityEvent.class)
@SuppressWarnings("unchecked")
public class LoadLastActivityListener extends BaseBlackboxListener<LoadLastActivityEvent, IActivity> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    @Override
    public void handle(LoadLastActivityEvent loadLastActivityEvent, ResultReference<IActivity> result) {
        EntityReference owner = loadLastActivityEvent.getType();
        IActivity activity = activityStreamDao.getLatestActivity(owner);
        result.setResult(activity);
    }
}