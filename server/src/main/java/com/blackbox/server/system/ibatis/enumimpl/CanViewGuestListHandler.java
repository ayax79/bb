package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.occasion.OccasionWebDetail;

/**
 * @author A.J. Wright
 */
public class CanViewGuestListHandler extends OrdinalEnumTypeHandler {

    public CanViewGuestListHandler() {
        super(OccasionWebDetail.CanViewGuestList.class);
    }
    
}
