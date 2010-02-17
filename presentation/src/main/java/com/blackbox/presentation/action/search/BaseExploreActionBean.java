package com.blackbox.presentation.action.search;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.extension.JodaPeriodConverter;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.occasion.IOccasionManager;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.integration.spring.SpringBean;

import javax.servlet.http.HttpSession;
import javax.annotation.PostConstruct;

import static org.apache.commons.lang.BooleanUtils.isTrue;

public abstract class BaseExploreActionBean extends BaseBlackBoxActionBean {

    public static final String EXPLORE_REQUEST_SESSION_KEY = "com.blackbox.presentation.action.search.EXPLORE_REQUEST";

    @SpringBean("userManager")
    protected IUserManager userManager;

    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;

    protected ISearcher searcher;

    @ValidateNestedProperties({
            @Validate(field = "registrationDate", converter = JodaPeriodConverter.class),
            @Validate(field = "lastOnline", converter = JodaPeriodConverter.class)
    })
    protected ExploreRequest explore;

    @Before(stages = LifecycleStage.BindingAndValidation)
    public void buildExploreRequest() {
        SearcherFactory.init(userManager, occasionManager);
        searcher = SearcherFactory.create(getContext());
        explore = searcher.lastSearchCriteria(getCurrentUser(), getCurrentProfile());
    }


    public ExploreRequest getExplore() {
        return explore;
    }

    public void setExplore(ExploreRequest explore) {
        this.explore = explore;
    }

    protected HttpSession getSession() {
        return getContext().getRequest().getSession();
    }

    public boolean isInRelationship() {
        return isTrue(getExplore().isInRelationship());
    }

    public boolean isVouched() {
        return isTrue(getExplore().isVouched());
    }

    public boolean isSingle() {
        return isTrue(getExplore().isSingle());
    }

    public boolean isSeekingFriends() {
        return isTrue(getExplore().isSeekingFriends());
    }

    public boolean isSeekingDating() {
        return isTrue(getExplore().isSeekingDating());
    }

    public boolean isSeekingRelationships() {
        return isTrue(getExplore().isSeekingRelationships());
    }

    public boolean isSeekingLove() {
        return isTrue(getExplore().isSeekingLove());
    }

    public boolean isSeekingSnuggling() {
        return isTrue(getExplore().isSeekingSnuggling());
    }

    public boolean isSeekingHookups() {
        return isTrue(getExplore().isSeekingHookups());
    }

    public boolean isGenderMale() {
        return isTrue(getExplore().isGenderMale());
    }

    public boolean isGenderUnspecified() {
        return isTrue(getExplore().isGenderUnspecified());
    }

    public boolean isGenderFemale() {
        return isTrue(getExplore().isGenderFemale());
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }
}
