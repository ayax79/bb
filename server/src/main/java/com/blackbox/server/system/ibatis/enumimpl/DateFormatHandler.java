package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.foundation.occasion.OccasionLayout;

/**
 * @author A.J. Wright
 */
public class DateFormatHandler extends OrdinalEnumTypeHandler {
    
    public DateFormatHandler() {
        super(OccasionLayout.DateFormat.class);
    }

}
