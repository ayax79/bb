package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.activity.ActivityTypeEnum;

/**
 * @author A.J. Wright
 */
public class ActivityTypeHandler extends OrdinalEnumTypeHandler {

    public ActivityTypeHandler() {
        super(ActivityTypeEnum.class);
    }
}
