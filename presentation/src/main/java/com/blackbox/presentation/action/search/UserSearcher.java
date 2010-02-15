package com.blackbox.presentation.action.search;

import com.blackbox.search.ExploreRequest;
import com.blackbox.social.Address;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.user.Profile;
import com.blackbox.user.PaginationResults;

/**
 *
 *
 */
public class UserSearcher implements ISearcher<IUserManager> {
    private IUserManager manager;

    @Override
    public IUserManager getManager() {
        return manager;
    }

    @Override
    public void setManager(IUserManager manager) {
        this.manager = manager;
    }

    @Override
    public ExploreRequest lastSearchCriteria(User user, Profile profile) {
        // grab the existing explore results
        ExploreRequest explore = manager.lastSearch(user.getGuid());
        if (explore != null) {
            // reset pagination
            explore.setMaxResults(10);
            explore.setStartIndex(0);
        }

        if (explore == null) {
            explore = new ExploreRequest();
            Address currentAddress = profile.getCurrentAddress();
            if (currentAddress != null) {
                explore.setZipCode(currentAddress.getZipCode());
            }
        }
        explore.setUserGuid(user.getGuid());
        return explore;
    }

    @Override
    public PaginationResults<?> search(ExploreRequest explore) {
        return manager.explore(explore);
    }
}
