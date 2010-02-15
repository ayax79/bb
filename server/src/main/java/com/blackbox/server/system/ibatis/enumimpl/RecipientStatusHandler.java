package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.activity.IRecipient;

/**
 * @author A.J. Wright
 */
public class RecipientStatusHandler extends OrdinalEnumTypeHandler {

    public RecipientStatusHandler() {
        super(IRecipient.RecipientStatus.class);
    }
}
