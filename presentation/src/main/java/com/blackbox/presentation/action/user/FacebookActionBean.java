/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.google.code.facebookapi.FacebookWebappHelper;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 *
 *
 */
public class FacebookActionBean extends BaseBlackBoxActionBean {

    private static final String API_KEY = "ff61ec2716a36b248bb9445e8b85bbe0";
    private static final String SECRET = "93b8c8f527eb4a63088b776a50cf1f49";

    public Resolution login() {

        FacebookWebappHelper<Object> fbHelper =
                FacebookWebappHelper.newInstanceJson(getContext().getRequest(), getContext().getResponse(), API_KEY, SECRET);

        fbHelper.requireLogin("http://localhost:8080");


        return new ForwardResolution("/dashboard.jsp");
    }
}
