package com.blackbox.presentation.action.search;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.Profile;
import com.blackbox.foundation.user.PaginationResults;

/**
 *
 *
 */
public class NoOpSearcher<M> implements ISearcher<M> {

    @Override
    public void setManager(M manager) {
    }

    @Override
    public M getManager() {
        return null;
    }

    @Override
    public ExploreRequest lastSearchCriteria(User currentUser, Profile currentProfile) {
        return new ExploreRequest();
    }

    @Override
    public PaginationResults<?> search(ExploreRequest explore) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
