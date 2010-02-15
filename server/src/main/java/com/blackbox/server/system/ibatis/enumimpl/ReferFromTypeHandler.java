package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.user.ReferFrom;

/**
 * @author A.J. Wright
 */
public class ReferFromTypeHandler extends OrdinalEnumTypeHandler {

    public ReferFromTypeHandler() {
        super(ReferFrom.ReferFromType.class);
    }
    
}
