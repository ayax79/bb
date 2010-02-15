package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.Status;

/**
 * @author A.J. Wright
 */
public class StatusHandler extends OrdinalEnumTypeHandler {

    public StatusHandler() {
        super(Status.class);
    }
}
