package com.blackbox.presentation.action.activity;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * @author A.J. Wright
 */
public class LegendsActivityActionBean extends ActivityActionBean {

    @Override
    protected Resolution handle() {
        return new ForwardResolution("/ajax/dash/legends.jspf");
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

}