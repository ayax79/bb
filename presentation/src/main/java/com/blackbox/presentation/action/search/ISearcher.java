package com.blackbox.presentation.action.search;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.user.Profile;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.PaginationResults;

/**
 *
 *
 */
public interface ISearcher<M> {
    void setManager(M manager);

    M getManager();

    ExploreRequest lastSearchCriteria(User user, Profile profile);

    PaginationResults<?> search(ExploreRequest explore);
}
