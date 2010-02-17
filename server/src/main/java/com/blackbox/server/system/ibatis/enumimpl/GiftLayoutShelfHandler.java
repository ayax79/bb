package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.gifting.GiftLayout;

/**
 * @author A.J. Wright
 */
public class GiftLayoutShelfHandler extends OrdinalEnumTypeHandler {

    public GiftLayoutShelfHandler() {
        super(GiftLayout.Shelf.class);
    }

}
