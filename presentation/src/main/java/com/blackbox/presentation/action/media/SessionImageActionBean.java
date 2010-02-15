/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.media;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Used to stream temporary images with a reference stored in the session.
 *
 * @author A.J. Wright
 */
public class SessionImageActionBean extends BaseBlackBoxActionBean {

    public static final String SESSION_IMAGE_PARAM = "com.blackbox.presentation.SESSION_IMAGE";

    @DontValidate
    @HttpCache(allow = false)
    public Resolution display() throws IOException {
        HttpSession session = getContext().getRequest().getSession();
        FileBean fileBean = (FileBean) session.getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
        if (fileBean != null) {
            return new StreamingResolution(fileBean.getContentType(), fileBean.getInputStream());
        } else {
            return null;
        }

    }
}
