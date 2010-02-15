/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;

/**
 *
 *
 */
public class UserJsonActionBean extends BaseBlackBoxActionBean
{
    @SpringBean(value = "userManager")
    private IUserManager userManager;

    private String username;
    private String userGuid;

    @HandlesEvent("json")
    public Resolution loadUserAsJSON() throws JSONException
    {
        User user;
        if (username == null)
        {
            user = userManager.loadUserByUsername(username);
        }
        else
        {
            user = userManager.loadUserByGuid(userGuid);
        }
        JSONObject json = toJSON(user);
        return new StreamingResolution("text", new StringReader(json.toString()));
    }

    public void setUserManager(IUserManager userManager)
    {
        this.userManager = userManager;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
