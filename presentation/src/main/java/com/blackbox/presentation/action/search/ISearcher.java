package com.blackbox.presentation.action.search;

import com.blackbox.search.ExploreRequest;
import com.blackbox.user.Profile;
import com.blackbox.user.User;
import com.blackbox.user.PaginationResults;

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
