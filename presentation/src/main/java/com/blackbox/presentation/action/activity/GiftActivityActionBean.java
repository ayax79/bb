package com.blackbox.presentation.action.activity;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * @author A.J. Wright
 */
public class GiftActivityActionBean extends ActivityActionBean {

    @Override
    protected Resolution handle() {
        return new ForwardResolution("/ajax/dash/gifts.jspf");
    }
}