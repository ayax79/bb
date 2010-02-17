/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion;

import com.blackbox.foundation.activity.Activity;
import com.blackbox.foundation.activity.ActivityFactory;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.geo.GeoPoint;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.occasion.*;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.external.ITwitterClient;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.occasion.event.*;
import com.blackbox.server.social.IVouchDao;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.util.GeoUtil;
import com.blackbox.foundation.util.PaginationUtil;
import com.google.code.facebookapi.BundleActionLink;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.event.multicaster.IEventMulticaster;
import org.yestech.lib.i18n.CountryEnum;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.blackbox.foundation.Utils.getBodyChildSecondsAsDate;
import static com.blackbox.foundation.Utils.getChildBodyString;
import static com.blackbox.server.util.OccasionUtil.cleanAttendee;
import static com.blackbox.server.util.OccasionUtil.cleanAttendeeOcassion;
import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;
import static com.blackbox.foundation.util.PaginationUtil.buildPaginationResults;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 *
 *
 */
@Service("occasionManager")
public class OccasionManager extends BaseServiceContainer implements IOccasionManager {

    private static final Logger logger = LoggerFactory.getLogger(OccasionManager.class);

    @Resource(name = "eventMulticaster")
    IEventMulticaster eventMulticaster;

    @Resource(name = "occasionDao")
    IOccasionDao occasionDao;

    @Resource(name = "activityManager")
    IActivityManager activityManager;

    @Resource(name = "socialManager")
    ISocialManager socialManager;

    @Resource(name = "mediaDao")
    IMediaDao mediaDao;

    @Resource(name = "activityStreamDao")
    IActivityStreamDao activityStreamDao;

    @Resource
    IExternalCredentialsDao credentialsDao;

    @Resource
    IUrlShortener urlShortener;

    @Resource
    IUserManager userManager;

    @Resource
    IVouchDao vouchDao;

    @Resource(name = "twitterService")
    ITwitterClient twitterClient;

    @Resource(name = "eventExploreResultsCache")
    ICacheManager<ExploreRequest, List<SearchResult<Occasion>>> eventExploreResultsCache;

    @Resource(name = "eventLastExploreRequestCache")
    ICacheManager<String, ExploreRequest> eventLastExploreRequestCache;

    @Override
    @Transactional(readOnly = false)
    public void publish(Occasion occasion) {
        Activity activity = ActivityFactory.toActivity(occasion);
        activityStreamDao.save(activity);
        publishToTwitter(occasion);
        publishToFacebookEvent(occasion);
        publishToFacebookWall(occasion);
    }

    private void publishToFacebookWall(Occasion occasion) {
        if (occasion.isPublishToFacebook() && OccasionType.OPEN == occasion.getOccasionType()) {
            try {
                //enable when facebook integration is there...
                BundleActionLink link = new BundleActionLink();
                String url = generateOccasionUrl(occasion);
                String shortenUrl = urlShortener.shorten(url);
                link.setHref(shortenUrl);
                link.setText(StringUtils.substring(occasion.getName(), 0, 20) + "....");

                //enable when facebook integration is there...                
//                FacebookJsonRestClient client = facebookClient();
//                long userId = client.users_getLoggedInUser();
//                
//                client.stream_publish(occasion.getDescription(), null, newArrayList(link), null, userId);
            }
            catch (IOException e) {
                logger.error("Error creating a shorter url for event", e);
//            } catch (FacebookException e) {
//                logger.error("Error creating facebook event", e);
            }
        }
    }

    private FacebookJsonRestClient facebookClient() throws FacebookException {
        FacebookJsonRestClient client = new FacebookJsonRestClient("4cb68e747ac77669ffa47294696dda59", "4551610afc85f57a63b6565a539db3a7");
        return client;
    }

    private void publishToFacebookEvent(Occasion occasion) {
        if (occasion.isPublishToFacebook() && OccasionType.OPEN == occasion.getOccasionType()) {
//            try {
            String occasionUrl = generateOccasionUrl(occasion);
            Map<String, String> faceBookEvent = newHashMap();
            faceBookEvent.put("name", occasion.getName());
            faceBookEvent.put("category", String.valueOf(occasion.getFacebookCategory()));
            faceBookEvent.put("subcategory", String.valueOf(occasion.getFacebookSubCategory()));
            faceBookEvent.put("host", occasion.getHostBy());
            faceBookEvent.put("locaton", occasion.getLocation());
            faceBookEvent.put("descrption", occasion.getFacebookDescription() + " " + occasionUrl);
            faceBookEvent.put("privacy_type", "OPEN");
            faceBookEvent.put("email", occasion.getEmail());
            faceBookEvent.put("phone", occasion.getPhoneNumber());
            faceBookEvent.put("street", occasion.getAddress().getAddress1() + " " + occasion.getAddress().getAddress2());
            faceBookEvent.put("city", occasion.getAddress().getCity());
            faceBookEvent.put("start_time", String.valueOf(occasion.getEventTime().toDateTime(ISOChronology.getInstanceUTC()).getMillis()));
            faceBookEvent.put("end_time", String.valueOf(occasion.getEventEndTime().toDateTime(ISOChronology.getInstanceUTC()).getMillis()));

            //enable when facebook integration is there...
//            FacebookJsonRestClient client = facebookClient();
//            long userId = client.users_getLoggedInUser();
//                client.events_create(faceBookEvent);
//            } catch (FacebookException e) {
//                logger.error("Error creating facebook event", e);
//            }
        }
    }

    private void publishToTwitter(Occasion occasion) {
        if (occasion.isPublishToTwitter() && OccasionType.OPEN == occasion.getOccasionType()) {
            ExternalCredentials externalCredentials = credentialsDao.loadByOwnerAndCredType(occasion.getOwner().getGuid(), TWITTER);

            try {
                twitterClient.publish(buildTwitterMessage(occasion), externalCredentials.decryptUsername(), externalCredentials.decryptPassword());
            }
            catch (IOException e) {
                logger.error("Error publishing to twitter", e);
            }
        }
    }

    protected String buildTwitterMessage(Occasion occasion) throws IOException {
        String body = occasion.getTwitterDescription();
//        String occasionUrl = generateOccasionUrl(occasion);
//        String url = urlShortener.shorten(occasionUrl);
        String url = "http://vb.ly/BBR";

        int totalSize = body.length() + url.length() + 4; // plus one is for one extra space;

        StringBuilder builder = new StringBuilder();
        if (totalSize > 140) {
            body = body.substring(0, 140 - (url.length() + 4));
            builder.append(body)
                    .append("...");

        } else {
            builder.append(body);
        }

        return builder
                .append(" ")
                .append(url)
                .toString();
    }

    private String generateOccasionUrl(Occasion occasion) {
        String presentationUrl = urlShortener.getPresentationUrl();
        String occasionUrl = presentationUrl;
        if (!StringUtils.endsWith(presentationUrl, "/")) {
            occasionUrl += "/";
        }
        if (!StringUtils.endsWith("/community/", occasionUrl)) {
            occasionUrl += "/community/";
        }
        occasionUrl += "event/show/" + occasion.getGuid();
        return occasionUrl;
    }

    @Override
    @Transactional(readOnly = false)
    public Occasion createOccasion(Occasion occasion) {
        occasionDao.save(occasion);
        getEventMulticaster().process(new CreateOccasionEvent(occasion));
        return occasion;
    }


    @Override
    @Transactional(readOnly = false)
    public Occasion updateOccasion(Occasion occasion) {

        User owner = userManager.loadUserByGuid(occasion.getOwner().getGuid());
        occasion.setOwner(owner);

        occasionDao.save(occasion);
        //getEventMulticaster().process(new UpdateOccasionEvent(occasion));
        return occasion;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOccasion(String guid) {
        Occasion occasion = occasionDao.loadByGuid(guid);
        occasionDao.delete(occasion);
        getEventMulticaster().process(new DeleteOccasionEvent(occasion));
    }

    @Override
    @Transactional(readOnly = true)
    public Occasion loadByGuid(String guid) {
        Occasion occasion = occasionDao.loadByGuid(guid);
        cleanAttendeeOcassion(occasion);
        return occasion;
    }

    @Override
    @Transactional(readOnly = false)
    public Occasion loadByWebUrl(String webUrl) {
        Occasion occasion = occasionDao.loadByWebUrl(webUrl);
        cleanAttendeeOcassion(occasion);
        return occasion;
    }

    @Override
    @Transactional(readOnly = false)
    public List<Occasion> loadByOwnerGuid(String ownerGuid) {
        List<Occasion> occasions = occasionDao.loadByOwnerGuid(ownerGuid);
        if (occasions != null) {
            for (Occasion occasion : occasions) {
                cleanAttendee(occasion);
            }
        }
        return occasions;
    }

    @Override
    @Transactional(readOnly = false)
    public PaginationResults<Occasion> loadOccasionsByAttendee(String guid, int startIndex, int maxResults) {
        Bounds bounds = new Bounds(startIndex, maxResults);
        PaginationResults<Occasion> occasions = occasionDao.loadOccasionsByAttendee(guid, bounds);
        for (Occasion occasion : occasions.getResults()) {
            cleanAttendee(occasion);
            OccasionLayout layout = occasion.getLayout();
            String imageGuid = layout.getImageGuid();
            if (isNotBlank(imageGuid)) {
                MediaMetaData md = mediaDao.loadMediaMetaDataByGuid(imageGuid);
                layout.setTransiantImage(md);
            }
            occasion.setTotalAttendees(occasionDao.getTotalAttendees(occasion.getGuid(), AttendingStatus.ATTENDING));

        }
        return occasions;
    }

    @Override
    public List<SearchResult<Occasion>> searchOccasions(String query) {
        List<Occasion> list = occasionDao.search(query);
        return buildSearchResults(list);
    }

    @Override
    public List<SearchResult<Occasion>> searchOccasions(String query, String location) {
        List<Occasion> list = occasionDao.search(query, location);
        return buildSearchResults(list);
    }

    public void setActivityManager(IActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public void setSocialManager(ISocialManager socialManager) {
        this.socialManager = socialManager;
    }

    public void setVouchDao(IVouchDao vouchDao) {
        this.vouchDao = vouchDao;
    }

    protected List<SearchResult<Occasion>> buildSearchResults(List<Occasion> occasions) {
        ArrayList<SearchResult<Occasion>> results = new ArrayList<SearchResult<Occasion>>(occasions.size());
        for (Occasion occasion : occasions) {
            IActivity activity = activityManager.loadLastActivity(occasion.toEntityReference());
            RelationshipNetwork network = socialManager.loadRelationshipNetwork(occasion.getGuid());
            results.add(new SearchResult<Occasion>(occasion, activity, network, vouchDao.countVouchesForEntity(occasion.getGuid()), null, null));
        }
        return results;
    }

    @Override
    @Transactional
    public void addOccasionsFromXml(String occasionsXml) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(new StringReader(occasionsXml));
            Element rootElement = document.getRootElement();
            for (Object o : rootElement.getChildren()) {
                Element child = (Element) o;
                Occasion occasion = ActivityFactory.createOccasion();

                User owner = null;
                String username = getChildBodyString(child, "owner");
                if (username != null) {
                    owner = userManager.loadUserByUsername(username);
                    occasion.setOwner(owner);
                }
                if (owner == null) {
                    throw new IllegalArgumentException("Events must contain an owner element");
                }

                occasion.setGuid(getChildBodyString(child, "guid"));
                occasion.setName(getChildBodyString(child, "name"));
                occasion.setDescription(getChildBodyString(child, "description"));
                occasion.setEventTime(getBodyChildSecondsAsDate(child, "eventtime"));
                occasion.setEventEndTime(getBodyChildSecondsAsDate(child, "eventendtime"));
                occasion.setEventUrl(getChildBodyString(child, "eventurl"));
                occasion.setLocation(getChildBodyString(child, "location"));

                Address address = occasion.getAddress();
                address.setAddress1(getChildBodyString(child, "address1"));
                address.setCity(getChildBodyString(child, "city"));
                address.setState(getChildBodyString(child, "state"));
                address.setZipCode(getChildBodyString(child, "zipcode"));
                address.setCountry(CountryEnum.UNITED_STATES);


                MediaMetaData mmd = MediaMetaData.createMediaMetaData();
                mmd.setLocation(getChildBodyString(child, "location"));

                String avatarLocation = getChildBodyString(child, "avatarLocation");
                if (avatarLocation != null) {
                    mmd.setThumbnailLocation(avatarLocation);
                } else {
                    mmd.setThumbnailLocation(mmd.getLocation());
                }

                mmd.setFileName(determineFileName(occasion.getGuid(), mmd.getLocation()));
                mmd.setMimeType("application/octet-stream");
                mmd.setArtifactOwner(occasion.toEntityReference());
                mmd.setArtifactType(ArtifactType.IMAGE);
                mmd.setPostDate(new DateTime());
                mmd.setRecipientDepth(NetworkTypeEnum.WORLD);
                mmd.setLibrary(true); // don't put in stream.
                occasion.getLayout().setImageGuid(mmd.getGuid());
                occasion.setImageLocation(mmd.getLocation());

                mediaDao.save(mmd);
                occasionDao.save(occasion);

                Activity activity = ActivityFactory.toActivity(occasion);
                activityStreamDao.save(activity);
            }
        } catch (JDOMException e) {
            throw new BlackBoxException(MessageFormat.format("Unable to build occasions from xml [{0}]", occasionsXml), e);
        } catch (IOException e) {
            throw new BlackBoxException(MessageFormat.format("Unable to build occasions from xml [{0}]", occasionsXml), e);
        }
    }

    @Override
    public List<Occasion> loadOccasions(OccasionRequest request) {
        return PaginationUtil.subList(occasionDao.loadOccasions(request), request.getBounds());
    }

    @Override
    public ExploreRequest lastSearch(String guid) {
        LoadLastExplorerEvent event = new LoadLastExplorerEvent(guid);
        return (ExploreRequest) getEventMulticaster().process(event);
    }

    @Override
    public PaginationResults<SearchResult<Occasion>> explore(ExploreRequest er) {
        String userGuid = er.getUserGuid();
        boolean cacheResults = er.isCacheResults();
        if (cacheResults) {
            eventLastExploreRequestCache.put(userGuid, er);
        }
        List<SearchResult<Occasion>> results = eventExploreResultsCache.get(er);

        if (results == null || results.isEmpty()) {
            List<Occasion> occasions = occasionDao.explore(er);

            results = new ArrayList<SearchResult<Occasion>>(occasions.size());

            GeoUtil geoUtil = new GeoUtil();
            GeoPoint zipPoint = geoUtil.fetchGeoInfoForZipCode(er.getZipCode());
            for (Occasion occasion : occasions) {
                Address address = occasion.getAddress();
                Double distance = (address != null) ? geoUtil.findDistanceInMiles(address.getGeoLocation(), zipPoint) : null;
                String imageGuid = occasion.getLayout().getImageGuid();
                if (imageGuid != null) {
                    MediaMetaData image = mediaDao.loadMediaMetaDataByGuid(imageGuid);
                    occasion.getLayout().setTransiantImage(image);
                }
                occasion.setAttendees(null);
                SearchResult<Occasion> searchResult = new SearchResult<Occasion>();
                searchResult.setEntity(occasion);
                searchResult.setDistance(distance);
                int totalAttendees = occasionDao.getTotalAttendees(occasion.getGuid(), AttendingStatus.ATTENDING);
                occasion.setTotalAttendees(totalAttendees);
                Attendee attendee = occasionDao.loadAttendeeByGuidAndOccasionGuid(occasion.getGuid(), userGuid);
                if (attendee != null) {
                    searchResult.setAttendingStatus(attendee.getAttendingStatus());
                } else {
                    searchResult.setAttendingStatus(AttendingStatus.NOT_INVITED);
                }
                results.add(searchResult);
            }

            eventExploreResultsCache.put(er, results);
        }

        return buildPaginationResults(results, er.getStartIndex(), er.getMaxResults());
    }


    @Override
    @Transactional(readOnly = false)
    public void updateAttendence(OccasionRequest request) {
        OccasionRsvpEvent event = new OccasionRsvpEvent(request);
        getEventMulticaster().process(event);
    }

    @Override
    @Transactional(readOnly = false)
    public void acceptInvitationRequest(OccasionRequest request) {
        Attendee attendee = occasionDao.loadAttendeeByGuidAndOccasionGuid(request.getOccasionGuid(), request.getAttendeeUserGuid(), request.getOwnerGuid());
        if (attendee != null) {
            attendee.setAcknowledged(false);
            attendee.setAttendingStatus(AttendingStatus.NOT_RESPONDED);
            occasionDao.update(attendee);
        }
    }

    @Override
    public void rejectInvitationRequest(OccasionRequest request) {
        Attendee attendee = occasionDao.loadAttendeeByGuidAndOccasionGuid(request.getOccasionGuid(), request.getAttendeeUserGuid(), request.getOwnerGuid());
        if (attendee != null) {
            attendee.setAcknowledged(true);
            attendee.setAttendingStatus(AttendingStatus.REJECTED_INVITATION_REQUEST);
            occasionDao.update(attendee);
        }
    }

    @Override
    public List<Occasion> loadOccasionsForScene(OccasionRequest request) {
        LoadOccasionForSceneEvent event = new LoadOccasionForSceneEvent(request);
        //noinspection unchecked
        return PaginationUtil.subList((List<Occasion>) getEventMulticaster().process(event), request.getBounds().getStartIndex(), request.getBounds().getMaxResults());
    }

    public IMediaDao getMediaDao() {
        return mediaDao;
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    protected String determineFileName(String guid, String location) {
        String suffix = null;
        if (isNotBlank(location)) {
            int lastPeriod = location.lastIndexOf(".");
            if (lastPeriod > -1) {
                suffix = location.substring(lastPeriod);
            }
        }
        if (suffix != null) {
            return guid + suffix;
        } else {
            return guid;
        }
    }
}
