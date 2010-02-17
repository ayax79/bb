package com.blackbox.presentation.action.search;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.Profile;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.social.Address;

/**
 *
 *
 */
public class OccasionSearcher implements ISearcher<IOccasionManager> {
    private IOccasionManager manager;

    @Override
    public void setManager(IOccasionManager manager) {
        this.manager = manager;
    }

    @Override
    public IOccasionManager getManager() {
        return manager;
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