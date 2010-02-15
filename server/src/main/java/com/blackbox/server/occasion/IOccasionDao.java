/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion;

import com.blackbox.occasion.Attendee;
import com.blackbox.occasion.AttendingStatus;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionRequest;
import com.blackbox.search.ExploreRequest;
import com.blackbox.user.PaginationResults;
import com.blackbox.util.Bounds;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 */
public interface IOccasionDao {

    @Transactional
    Occasion save(Occasion occasion);

    @Transactional
    void delete(Occasion occasion);

    List<Occasion> loadByOwnerGuid(String guid);

    Occasion loadByGuid(String guid);

    Occasion loadByWebUrl(String webUrl);

    List<Occasion> search(String query);

    List<Occasion> search(String query, String address);

    List<Occasion> loadOccasions(OccasionRequest request);

    PaginationResults<Occasion> loadOccasionsByAttendee(String guid, Bounds bounds);

    List<Occasion> explore(ExploreRequest er);

    List<Occasion> loadOccasionsForScene(OccasionRequest request);

//    Attendee loadAttendeeByAttendeeGuidAndOccasionGuid(String occasionGuid, String attendeeGuid);

    Attendee loadAttendeeByGuidAndOccasionGuid(String occasionGuid, String attendeeUserGuid);

    @Transactional(readOnly = false)
    void update(Attendee attendee);

    int getTotalAttendees(String occasionGid, AttendingStatus attending);

    List<Occasion> loadUnAcknowledged(String guid, int limit);

    int loadTotalUnAcknowledged(String guid);

    Attendee loadAttendeeByGuidAndOccasionGuid(String occasionGuid, String attendeeUserGuid, String ownerGuid);

    void reindex();
}
