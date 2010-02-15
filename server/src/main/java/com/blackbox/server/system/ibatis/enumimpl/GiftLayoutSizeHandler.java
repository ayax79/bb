package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.gifting.GiftLayout;

/**
 * @author A.J. Wright
 */
public class GiftLayoutSizeHandler extends OrdinalEnumTypeHandler {

    public GiftLayoutSizeHandler() {
        super(GiftLayout.GiftLayoutSize.class);
    }

}
