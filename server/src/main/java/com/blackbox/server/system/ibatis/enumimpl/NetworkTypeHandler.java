package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.social.NetworkTypeEnum;

/**
 * @author A.J. Wright
 */
public class NetworkTypeHandler extends OrdinalEnumTypeHandler {

    public NetworkTypeHandler() {
        super(NetworkTypeEnum.class);
    }
}
