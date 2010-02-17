/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import com.blackbox.foundation.user.*;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

/**
 *
 *
 */
@UrlBinding("/user/{username}")
public class UserActionBean extends BaseBlackBoxActionBean
{
    @SpringBean("userManager")
    private IUserManager userManager;

    @Validate(required = true) private String username;
    private User user;
    private Profile profile;

    public Resolution show() throws JSONException {
        user = userManager.loadUserByUsername(username);
        if (user != null)
        {
            profile = userManager.loadProfileByUserGuid(user.getGuid());

        }

        if (getView() == ViewType.json) {
            getContext().getResponse().setHeader("Stripes-Success", "true");

            String content = "";
            if (user != null) {
                JSONObject json = toJSON(user);
                content = json.toString();
            }
            
            return new StreamingResolution("text", new StringReader(content));
        }


        return new ForwardResolution("/user.jsp");
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public User getUser()
    {
        return user;
    }

    public Profile getProfile()
    {
        return profile;
    }

    public void setUserManager(IUserManager userManager)
    {
        this.userManager = userManager;
    }

}
