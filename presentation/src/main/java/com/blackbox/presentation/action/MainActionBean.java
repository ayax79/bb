package com.blackbox.presentation.action;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 *
 * @author andrew
 */
@UrlBinding("/main")
public class MainActionBean extends BaseBlackBoxActionBean {

    public Resolution main() {
        return new ForwardResolution("/main.jsp");
    }


}
