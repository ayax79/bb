package com.blackbox.foundation.common;

import com.blackbox.foundation.activity.ActivityTypeEnum;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.util.Affirm;
import com.google.common.base.Predicate;

/**
 * @author colin@blackboxrepublic.com
 */
public class ActivityThreadParentOwnerTypePredicate implements Predicate<IActivityThread> {

    private ActivityTypeEnum entityType;

    public ActivityThreadParentOwnerTypePredicate(ActivityTypeEnum activityType) {
        this.entityType = Affirm.isNotNull(activityType, "activityType", IllegalArgumentException.class);
    }

    @Override
    public boolean apply(IActivityThread input) {
        return entityType.equals(input.getParent().getActivityType());
    }
}
