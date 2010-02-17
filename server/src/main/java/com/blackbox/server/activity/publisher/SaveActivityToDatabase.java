/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity.publisher;

import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.foundation.activity.Activity;
import com.blackbox.foundation.activity.ActivityFactory;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveActivityToDatabase {
    final private static Logger logger = LoggerFactory.getLogger(SaveActivityToDatabase.class);

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    public IActivityStreamDao getActivityStreamDao() {
        return activityStreamDao;
    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Activity activity = ActivityFactory.transform(camelMessage.getBody());
                if (activity != null) {
                    activityStreamDao.save(activity);
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}