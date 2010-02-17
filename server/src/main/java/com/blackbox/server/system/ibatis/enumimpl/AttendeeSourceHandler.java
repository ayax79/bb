package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.foundation.occasion.AttendeeSource;
import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;

/**
 * @author A.J. Wright
 */
public class AttendeeSourceHandler extends OrdinalEnumTypeHandler {

    public AttendeeSourceHandler() {
        super(AttendeeSource.class);
    }
}
