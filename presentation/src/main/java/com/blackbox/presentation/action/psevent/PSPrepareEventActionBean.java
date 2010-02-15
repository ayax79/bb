package com.blackbox.presentation.action.psevent;

import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

public class PSPrepareEventActionBean extends PSBaseEventActionBean {

    @DontValidate
    public Resolution prepare() throws Exception {
        cleanHttpSession();
        return new RedirectResolution(PSCreateEventActionBean.class);
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
