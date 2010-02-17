package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.user.ViewedBy;

/**
 * @author A.J. Wright
 */
public class ViewedByTypeHandler extends OrdinalEnumTypeHandler {

    public ViewedByTypeHandler() {
        super(ViewedBy.ViewedByType.class);
    }
    
}
