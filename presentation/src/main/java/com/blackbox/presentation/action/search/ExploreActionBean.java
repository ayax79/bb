package com.blackbox.presentation.action.search;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.PaginationResults;
import net.sourceforge.stripes.action.*;
import static org.apache.commons.lang.StringUtils.isBlank;


public class ExploreActionBean extends BaseExploreActionBean {

    private PaginationResults<?> results;

    @DontValidate
    @DefaultHandler                                                          
    public Resolution begin() {
        return new ForwardResolution("/explore.jsp");
    }

    public Resolution user() {
        explore.cacheResults(true);
        results = searcher.search(explore);
        return new ForwardResolution("/ajax/explore/results.jspf");
    }

    public Resolution event() {
        explore.cacheResults(true);
        results = searcher.search(explore);
        return new ForwardResolution("/ajax/explore/event-results.jspf");
    }

    public Resolution gifts() {
        return new ForwardResolution("changeme");
    }

    public Resolution camps() {
        return new ForwardResolution("changeme");
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public PaginationResults<?> getResults() {
        return results;
    }

    public ExploreRequest getExplore() {
        return explore;
    }

    public void setExplore(ExploreRequest explore) {
        this.explore = explore;
    }

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.explore;
    }

}
