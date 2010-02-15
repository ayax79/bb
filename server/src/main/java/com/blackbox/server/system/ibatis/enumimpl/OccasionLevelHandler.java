package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.occasion.OccasionLevel;

public class OccasionLevelHandler extends OrdinalEnumTypeHandler {

    public OccasionLevelHandler() {
        super(OccasionLevel.class);
    }

}
