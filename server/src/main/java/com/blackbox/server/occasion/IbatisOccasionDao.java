/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion;

import com.blackbox.Status;
import com.blackbox.occasion.*;
import com.blackbox.search.ExploreRequest;
import com.blackbox.server.util.GeoHelper;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.user.PaginationResults;
import com.blackbox.util.Bounds;
import org.compass.core.CompassOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blackbox.server.util.PersistenceUtil.insertOrUpdate;
import static com.blackbox.util.PaginationUtil.buildPaginationResults;
import static com.google.common.collect.Lists.newArrayList;

/**
 *
 *
 */
@Repository("occasionDao")
public class IbatisOccasionDao implements IOccasionDao {

    private static final String[] SEARCH_FIELDS = {"description", "name", "owner.name", "owner.username"};

    private static final Logger log = LoggerFactory.getLogger(IbatisOccasionDao.class);

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;
    @Resource(name = "compassTemplate")
    CompassOperations compassTemplate;

    @Override
    @Transactional
    public Occasion save(Occasion occasion) {
        OccasionWebDetail wd = occasion.getOccasionWebDetail();
        if (wd != null) insertOrUpdate(wd, template, "OccasionWebDetail.insert", "OccasionWebDetail.update");
        OccasionLayout layout = occasion.getLayout();
        if (layout != null) insertOrUpdate(layout, template, "OccasionLayout.insert", "OccasionLayout.update");
        insertOrUpdate(occasion, template, "Occasion.insert", "Occasion.update");

        List<Attendee> attendees = occasion.getAttendees();
        if (attendees != null) {
            for (Attendee attendee : attendees) {

                if (insertOrUpdate(attendee, template, "Attendee.insert", "Attendee.update").getOperation() ==
                        PersistenceUtil.Info.OperationType.INSERT) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("attendees_guid", attendee.getGuid());
                    params.put("occasions_guid", occasion.getGuid());
                    template.insert("Attendee.insertJoin", params);
                }
            }
        }
        compassTemplate.save(occasion);
        return occasion;
    }

    @Override
    public Occasion loadByGuid(String guid) {
        return (Occasion) template.selectOne("Occasion.load", guid);
    }

    @Override
    public Occasion loadByWebUrl(String webUrl) {
        return (Occasion) template.selectOne("Occasion.loadByWebUrl", webUrl);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Occasion> loadByOwnerGuid(String guid) {
        return template.selectList("Occasion.loadByOwnerGuid", guid);
    }

    @Transactional
    @Override
    public void delete(Occasion occasion) {
        template.delete("Attendee.deleteJoinByOccasionsGuid", occasion.getGuid());
        template.delete("Attendee.deleteByOccasionsGuid", occasion.getGuid());
        template.delete("Occasion.delete", occasion.getGuid());
        template.delete("OccasionWebDetailExtraWebLink.deleteByOccasionWebDetailGuid", occasion.getOccasionWebDetail().getGuid());
        template.delete("OccasionWebDetail.delete", occasion.getOccasionWebDetail().getGuid());
        template.delete("OccasionLayout.delete", occasion.getLayout().getGuid());
        compassTemplate.delete(occasion);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Occasion> loadOccasions(final OccasionRequest request) {
        return template.selectList("Occasion.loadOccasions", request);

    }

    @Override
    public List<Occasion> search(String query) {
//        return searchTemplate.search(query, Occasion.class, SEARCH_FIELDS);
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Occasion> search(String query, String address) {
        throw new UnsupportedOperationException();
//        StringBuilder builder = new StringBuilder();
//
//        boolean isQuery = false;
//        if (isNotBlank(query)) {
//            builder.append(query);
//            isQuery = true;
//        }
//
//
//        if (isNotBlank(address)) {
//
//            if (isQuery) builder.append(" AND ( ");
//
//            String[] addressTokens = address.split("(\\s|,)");
//            //noinspection ForLoopReplaceableByForEach
//            for (int i = 0; i < addressTokens.length; i++) {
//                String token = addressTokens[i];
//                if (isNotBlank(token)) {
//                    token = token.toLowerCase();
//
//                    builder.append(" location.city:").append(token);
//                    builder.append(" location.zipCode:").append(token); //allow for short us zipcodes and long
//                }
//            }
//
//            if (isQuery) builder.append(" )");
//
//        }
//
//        return searchTemplate.search(builder.toString(), Occasion.class, SEARCH_FIELDS);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Occasion> explore(final ExploreRequest er) {

        if (er.getZipCode() != null) {
            GeoHelper.applyGeoDataBasedOnExploreRequestSituation(er);
        }
        if (er.getName() != null) {
            er.setName("%" + er.getName() + "%");
        }

        return template.selectList("Occasion.loadExplore", er);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PaginationResults<Occasion> loadOccasionsByAttendee(final String guid, final Bounds bounds) {
        List<Occasion> attendingOccasions = template.selectList("Occasion.loadAttendingOccasions", guid);
        List<Occasion> createdOccasions = template.selectList("Occasion.loadCreatedOccasions", guid);

        List<Occasion> occasions = newArrayList();
        occasions.addAll(attendingOccasions);
        for (Occasion createdOccasion : createdOccasions) {
            boolean found = false;
            for (Occasion attendingOccasion : attendingOccasions) {
                if (attendingOccasion.getGuid().equals(createdOccasion.getGuid())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                occasions.add(createdOccasion);
            }
        }

        Collections.sort(occasions, OccasionComparator.eventTimeDescending());
        return buildPaginationResults(occasions, bounds.getStartIndex(), bounds.getMaxResults());
    }


    @Override
    public List<Occasion> loadOccasionsForScene(OccasionRequest request) {
        Bounds bounds = request.getBounds();
        Map<String, Object> params = new HashMap<String, Object>(5);

        params.put("attendingStatus", AttendingStatus.ATTENDING.getDatabaseIdentifier());
        params.put("occasionStatus", Status.ENABLED.getValue());
        params.put("occasionType", OccasionType.OPEN.getDatabaseIdentifier());
        params.put("maxResults", bounds.getMaxResults());
        params.put("startIndex", bounds.getStartIndex());
        return template.selectList("Occasion.loadForScene", params);
    }

    @Override
    public Attendee loadAttendeeByGuidAndOccasionGuid(String occasionGuid, String attendeeUserGuid, String ownerGuid) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("attendeeUserGuid", attendeeUserGuid);
        params.put("occasionGuid", occasionGuid);
        params.put("ownerGuid", ownerGuid);
        return (Attendee) template.selectOne("Attendee.loadAttendeeByGidAndOccasionAndOccasionOwner", params);
    }

    public Attendee loadAttendeeByGuidAndOccasionGuid(String occasionGuid, String attendeeUserGuid) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("attendeeUserGuid", attendeeUserGuid);
        params.put("occasionGuid", occasionGuid);
        return (Attendee) template.selectOne("Attendee.loadAttendeeByGidAndOccasion", params);
    }

    @Transactional(readOnly = false)
    public void update(Attendee attendee) {
        PersistenceUtil.update(attendee, template, "Attendee.update");
    }

    @Override
    public int getTotalAttendees(String occasionGid, AttendingStatus attending) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", occasionGid);
        params.put("status", attending);
        return (Integer) template.selectOne("Attendee.countAttendees", params);
    }

    @Override
    public List<Occasion> loadUnAcknowledged(final String guid, final int limit) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("status", AttendingStatus.REQUESTED_INVITATION);
        params.put("acknowledged", false);

        return template.selectList("Occasion.loadUnAcknowledged", params);
    }

    @Override
    public int loadTotalUnAcknowledged(final String guid) {

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("status", AttendingStatus.REQUESTED_INVITATION);
        params.put("acknowledged", false);

        return (Integer) template.selectOne("Occasion.countAcknowledgedAndStatus", params);
    }

    @Override
    public void reindex() {
        List<Occasion> list = template.selectList("Occasion.loadAll");
        for (Occasion occasion : list) {
            log.info("Reindexing Occasion : " + occasion.getGuid());
            compassTemplate.save(occasion);
        }
    }
}
