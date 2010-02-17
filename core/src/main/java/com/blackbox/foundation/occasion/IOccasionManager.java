/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.occasion;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.user.PaginationResults;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 */
public interface IOccasionManager {

    Occasion createOccasion(Occasion occasion);

    Occasion updateOccasion(Occasion occasion);

    void deleteOccasion(String guid);

    Occasion loadByGuid(String guid);

    Occasion loadByWebUrl(String webUrl);

    List<Occasion> loadByOwnerGuid(String guid);

    List<SearchResult<Occasion>> searchOccasions(String query);

    List<SearchResult<Occasion>> searchOccasions(String query, String location);

    List<Occasion> loadOccasions(OccasionRequest request);

    PaginationResults<Occasion> loadOccasionsByAttendee(String guid, int startIndex, int maxResults);

    ExploreRequest lastSearch(String guid);

    PaginationResults<SearchResult<Occasion>> explore(ExploreRequest explore);

    void updateAttendence(OccasionRequest request);

    List<Occasion> loadOccasionsForScene(OccasionRequest request);

    void acceptInvitationRequest(OccasionRequest request);

    void rejectInvitationRequest(OccasionRequest request);

    void publish(Occasion occasion);

    @Transactional
    void addOccasionsFromXml(String occasionsXml);
}
