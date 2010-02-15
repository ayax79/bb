package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.social.Ignore;

/**
 * @author A.J. Wright
 */
public class IgnoreTypeHandler extends OrdinalEnumTypeHandler {

    public IgnoreTypeHandler() {
        super(Ignore.IgnoreType.class);
    }

}
