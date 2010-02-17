package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.foundation.Status;
import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;

/**
 * @author A.J. Wright
 */
public class StatusHandler extends OrdinalEnumTypeHandler {

    public StatusHandler() {
        super(Status.class);
    }
}
