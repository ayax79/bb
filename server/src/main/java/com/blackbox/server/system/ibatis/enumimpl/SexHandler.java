package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.user.SexEnum;

/**
 * @author A.J. Wright
 */
public class SexHandler extends OrdinalEnumTypeHandler {

    public SexHandler() {
        super(SexEnum.class);
    }

}
