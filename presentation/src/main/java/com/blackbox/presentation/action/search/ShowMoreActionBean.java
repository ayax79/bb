package com.blackbox.presentation.action.search;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.search.ShowMoreActionBean.ViewAllType.following;
import static com.blackbox.presentation.action.search.ShowMoreActionBean.ViewAllType.friends;
import static com.blackbox.presentation.action.search.ShowMoreActionBean.ViewAllType.viewedBy;
import com.blackbox.user.IUserManager;
import com.blackbox.user.PaginationResults;
import com.blackbox.user.User;
import com.blackbox.user.ViewedBy;
import com.blackbox.social.ISocialManager;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.util.PaginationUtil;
import static com.blackbox.util.PaginationUtil.subList;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ShowMoreActionBean extends BaseBlackBoxActionBean {

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("socialManager")
    ISocialManager socialManager;

    public static enum ViewAllType {
        friends,
        following,
        viewedBy
    }

    private String identifier;
    private int startIndex = 0;
    private int maxResults = 25;


    protected ViewAllType viewAllType;
    private PaginationResults<User> paginationResults;


    public Resolution begin() {

        RelationshipNetwork network = socialManager.loadRelationshipNetwork(identifier);

        if (viewAllType == following || viewAllType == friends) {
            Collection<String> guidCollection;
            if (viewAllType == following) {
                guidCollection = network.getFollowing();
            } else {
                guidCollection = network.getFriends();
            }
            List<String> guids = subList(new ArrayList<String>(guidCollection), startIndex, maxResults);
            List<User> users = Lists.transform(guids, new Function<String, User>() {
                @Override
                public User apply(String from) {
                    return userManager.loadUserByGuid(from);
                }
            });
            // won't actually modify the size, but it works nice for creating tthe object.
            paginationResults = PaginationUtil.buildPaginationResults(users, startIndex, maxResults);
        } else if (viewAllType == viewedBy) {

            List<ViewedBy> list = userManager.loadViewersByDestGuid(identifier);
            list = subList(list, startIndex, maxResults);
            List<User> users = Lists.transform(list, new Function<ViewedBy, User>() {

                @Override
                public User apply(ViewedBy from) {
                    return from.getUser();
                }
            });
            paginationResults = PaginationUtil.buildPaginationResults(users, startIndex, maxResults);
        }
        return new ForwardResolution("/ajax/persona/show-more.jspf");
    }

    public ViewAllType getViewAllType() {
        return viewAllType;
    }

    public void setViewAllType(ViewAllType viewAllType) {
        this.viewAllType = viewAllType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public PaginationResults<User> getPaginationResults() {
        return paginationResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
