package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

public class FaqActionBean extends BaseBlackBoxActionBean {

    public Resolution execute() {
        return new ForwardResolution("/faq.jsp");
    }

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.faq;
    }
}
