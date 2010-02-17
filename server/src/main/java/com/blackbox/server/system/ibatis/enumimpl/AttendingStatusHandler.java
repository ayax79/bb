package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.occasion.AttendingStatus;

public class AttendingStatusHandler extends OrdinalEnumTypeHandler {

    public AttendingStatusHandler() {
        super(AttendingStatus.class);
    }

}
