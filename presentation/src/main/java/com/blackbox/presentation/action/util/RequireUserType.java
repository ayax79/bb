package com.blackbox.presentation.action.util;

import com.blackbox.foundation.user.User;

import java.lang.annotation.*;

@Inherited
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireUserType {

    User.UserType[] value();
}
