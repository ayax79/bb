/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity.event;

import com.blackbox.EntityReference;
import com.blackbox.activity.ActivityTypeEnum;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class LoadActivityStreamByTypeEvent extends BaseEvent<EntityReference> {
    private int limit;
    private ActivityTypeEnum activityType;
    private static final long serialVersionUID = 6435613167760036520L;

    public LoadActivityStreamByTypeEvent(EntityReference entityReference, ActivityTypeEnum activityType) {
        super(entityReference);
        this.activityType = activityType;
    }

    public LoadActivityStreamByTypeEvent(EntityReference entityReference, int limit, ActivityTypeEnum activityType) {
        super(entityReference);
        this.limit = limit;
        this.activityType = activityType;
    }

    public int getLimit() {
        return limit;
    }

    public ActivityTypeEnum getActivityType() {
        return activityType;
    }
}