package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;
import com.blackbox.user.User;

/**
 * @author A.J. Wright
 */
public class UserTypeHandler extends OrdinalEnumTypeHandler {

    public UserTypeHandler() {
        super(User.UserType.class);
    }
    
}
