package com.blackbox.presentation.action.search;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.controller.LifecycleStage;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.social.Address;


public class AjaxExploreActionBean extends BaseExploreActionBean {

    private User viewer;

    @DefaultHandler
    public Resolution people() {
        viewer = userManager.loadUserByGuid(getCurrentUser().getGuid());
        return new ForwardResolution("/ajax/explore/people.jspf");
    }

    public Resolution events() {
        viewer = userManager.loadUserByGuid(getCurrentUser().getGuid());
        return new ForwardResolution("/ajax/explore/events.jspf");
    }

    public User getViewer() {
        return viewer;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }

}
