package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.EntityType;

/**
 * @author A.J. Wright
 */
public class EntityTypeHandler extends OrdinalEnumTypeHandler {

    public EntityTypeHandler() {
        super(EntityType.class);
    }
}
