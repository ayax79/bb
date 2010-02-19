/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.*;
import com.blackbox.foundation.Status;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.activity.ActivityFactory;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.exception.UserAlreadyExistsException;
import com.blackbox.foundation.geo.GeoPoint;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.foundation.security.Privacy;
import com.blackbox.server.billing.IBillingDao;
import com.blackbox.server.exception.NotFoundException;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.security.ISecurityDao;
import com.blackbox.server.social.IVouchDao;
import com.blackbox.server.user.event.LoadUserStatsEvent;
import com.blackbox.server.user.event.MarkProfileViewedEvent;
import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.server.user.event.VerifyUserEvent;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.*;
import com.blackbox.foundation.user.ViewedBy.ViewedByType;
import com.blackbox.foundation.util.Affirm;
import com.blackbox.foundation.util.GeoUtil;
import com.google.common.base.Function;
import org.apache.commons.collections15.Closure;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.blackbox.foundation.EntityType.USER;
import static com.blackbox.foundation.Utils.transform;
import static com.blackbox.foundation.user.User.UserType.*;
import static com.blackbox.foundation.util.PaginationUtil.buildPaginationResults;
import static com.google.common.collect.Lists.newArrayList;

/**
 *
 *
 */
@SuppressWarnings("unchecked")
@Service("userManager")
public class UserManager extends BaseServiceContainer implements IUserManager {

    @Resource(name = "mediaManager")
    IMediaManager mediaManager;

    @Resource(name = "userDao")
    IUserDao userDao;

    @Resource(name = "viewedByDao")
    IViewedByDao viewedByDao;

    @Resource(name = "mediaDao")
    IMediaDao mediaDao;

    @Resource(name = "securityDao")
    ISecurityDao securityDao;

    @Resource(name = "socialManager")
    ISocialManager socialManager;

    @Resource(name = "userCache")
    ICacheManager<String, String> userCache;

    @Resource(name = "userGuidCache")
    ICacheManager<String, User> userGuidCache;

    @Resource(name = "userRefreshCache")
    ICacheManager<String, String> userRefreshCache;

    @Resource
    IVouchDao vouchDao;

    @Resource
    IAffiliateMappingDao affiliateMappingDao;

    @Resource
    IBookmarkManager bookmarkManager;

    @Resource(name = "wordFrequencyCache")
    ICacheManager<String, List<WordFrequency>> wordFrequencyCache;

    @Resource(name = "viewedByCache")
    ICacheManager<String, List<ViewedBy>> viewedByCache;

    @Resource(name = "userExploreResultsCache")
    ICacheManager<ExploreRequest, List<SearchResult<User>>> userExploreResultsCache;

    @Resource(name = "userLastExploreRequestCache")
    ICacheManager<String, ExploreRequest> userLastExploreRequestCache;

    @Resource
    IPromoCodeDao promoCodeDao;

    @Resource
    IExternalCredentialsDao externalCredentialsDao;

    @Resource(name = "privacyCache")
    ICacheManager<String, Privacy> privacyCache;

    @Resource
    IProfileDao profileDao;

    @Resource(name = "miniProfileCache")
    ICacheManager<String, MiniProfile> miniProfileCache;

    @Resource(name = "activityManager")
    IActivityManager activityManager;

    @Resource(name = "billingDao")
    IBillingDao billingDao;

    private GeoUtil geoUtil = new GeoUtil();

    @Override
    public Profile loadProfileByUserGuid(String userGuid) {
        Profile p = profileDao.loadProfileByUserGuid(userGuid);

        if (p != null) {
            MediaMetaData metaData = mediaDao.loadProfileMediaMetaDataByOwner(new EntityReference(USER, userGuid));
            if (metaData != null) p.setProfileImgUrl(metaData.getLocation());
            MediaMetaData metaData1 = mediaDao.loadAvatarMediaMetaDataByOwner(new EntityReference(USER, userGuid));
            if (metaData1 != null) p.setAvatarUrl(metaData1.getLocation());
        }

        return p;
    }


    @Override
    public User createUser(User user) {
        Affirm.that(userDao.isUsernameAvailable(user.getUsername()), MessageFormat.format("That user name has been take: ", user.getUsername()), UserAlreadyExistsException.class);
        return userDao.save(user);
    }

    @Override
    public User save(User user) {
        user = userDao.save(user);
        setSessionRefreshNeeded(user.getGuid());
        privacyCache.flush(user.getGuid());
        userGuidCache.put(user.getGuid(), user);
        return user;
    }

    @Override
    public User loadUserByEmail(String email) {
        return userDao.loadUserByEmail(email);
    }


    @Override
    public VerificationResult verifyUser(String userGuid, String key) {
        Boolean r = (Boolean) getEventMulticaster().process(new VerifyUserEvent(userGuid, key));
        if (r == null) {
            r = false;
        }
        return new VerificationResult(r);
    }

    @Override
    public User loadUserByUsername(String username) {
        return userDao.loadUserByUsername(username);
    }

    @Override
    public void markProfileViewed(String userGuid) {
        getEventMulticaster().process(new MarkProfileViewedEvent(userGuid));
    }

    @Override
    public void saveViewedBy(String viewingGuid, String destGuid, ViewedByType viewedByType) {
        User user = userDao.loadUserByGuid(viewingGuid);
        if (user != null) {
            ViewedBy viewedBy = new ViewedBy();
            viewedBy.setUser(user);
            viewedBy.setDestGuid(destGuid);
            viewedBy.setViewedByType(viewedByType);
            viewedByDao.insert(viewedBy);
        }
    }

    @Override
    public List<ViewedBy> loadViewersByDestGuid(String destGuid) {

        List<ViewedBy> viewerList = viewedByCache.get(destGuid);
        if (viewerList == null) {
            List<ViewedBy> list = viewedByDao.loadViewersByDestGuid(destGuid);
            viewerList = new ArrayList<ViewedBy>(list.size());
            List<String> addedIds = new ArrayList<String>(list.size());
            for (ViewedBy viewedBy : list) {
                String viewByGuid = viewedBy.getUser().getGuid();
                if (!viewByGuid.equals(destGuid) && !addedIds.contains(viewByGuid)) {
                    addedIds.add(viewedBy.getUser().getGuid());
                    viewerList.add(viewedBy);
                    viewedBy.setUser(loadUserByGuid(viewByGuid));
                }

            }

            viewedByCache.put(destGuid, viewerList);
        }
        return viewerList;
    }

    @Override
    public Integer loadViewNumByDestGuid(String destGuid) {
        return viewedByDao.loadViewNumByDestGuid(destGuid);
    }

    @Override
    public UserStats loadUserStats(String userGuid) {
        return (UserStats) getEventMulticaster().process(new LoadUserStatsEvent(userGuid));
    }

    @Transactional
    @Override
    public void register(Registration registration) {
        User user = registration.getUser();
        if (user.getGuid() == null) {
            Utils.applyGuid(user);
        }

        // make sure we get the correct version, etc that we don't want to save
        User oldRecord = userDao.loadUserByGuid(user.getGuid());
        assert oldRecord != null;
        user.setVersion(oldRecord.getVersion());
        user.setPassword(oldRecord.getPassword());
        user.setStatus(Status.ENABLED); // make them enabled now
        user = userDao.save(user);

        if (registration.getPromoCodeGuid() != null) {
            BasePromoCode promoCode = promoCodeDao.loadPromoCodeByGuid(registration.getPromoCodeGuid());
            if (promoCode != null && BasePromoCode.PromoCodeType.SINGLE_USE.equals(promoCode.getType())) {
                promoCodeDao.delete(promoCode);
            }
        }

        // delete the captured email since we nolonger need it
        userDao.deleteCapturedEmail(user.getEmail());

        if (registration.getAffiliateId() != null) {
            affiliate(registration.getAffiliateId(), user.getGuid());
        }

        final RegisterUserEvent registerEvent = new RegisterUserEvent(user, registration.getLeechEmails());
        if (registration.getPromoCodeGuid() != null)
            registerEvent.setPromoCodeGuid(registration.getPromoCodeGuid());

        getEventMulticaster().process(registerEvent);
    }

    @Override
    public MiniProfile loadMiniProfile(String identifier) {
        MiniProfile miniProfile = miniProfileCache.get(identifier);
        if (miniProfile == null || !miniProfile.isInitialized()) {
            //load by username
            MiniProfile newProfile = userDao.loadMiniProfileByUsername(identifier);
            if (newProfile == null) {
                newProfile = userDao.loadMiniProfileByUserGuid(identifier);
            }
            if (miniProfile != null && miniProfile.getLastOnline() != null) {
                newProfile.setLastOnline(miniProfile.getLastOnline());
            }
            miniProfile = newProfile;
            miniProfile.setInitialized(true);
            miniProfileCache.put(identifier, miniProfile);
        }
        //pull from activity stream
        IActivity activity = activityManager.loadLastActivity(EntityReference.createEntityReference(miniProfile.getGuid()));
        if (activity != null) {
            miniProfile.setActivity(ActivityFactory.clone(activity));
        }
        return miniProfile;
    }

    @Override
    public User loadSessionCacheUserByGuid(String guid) {
        return loadUserByGuid(guid);
    }

    @Override
    public User loadUserByGuid(String guid) {
        User user = userGuidCache.get(guid);
        if (user == null) {
            user = userDao.loadUserByGuid(guid);
            if (user != null) {
                userGuidCache.put(guid, user);
            }
        }
        return user;
    }

    @Override
    public User loadUserByGuidForDisplayName(String guid) {
        return loadUserByGuid(guid);
    }

    @Override
    public void forgotPassword(String email) {
        throw new UnsupportedOperationException();
    }


    @Override
    public List<WordFrequency> highFrequencyWords(String userGuid) {
        List<WordFrequency> list = wordFrequencyCache.get(userGuid);
        if (list == null) {
            list = userDao.highFrequencyWords(userGuid);
            // don't put things in the cache until we have atleast 5 words
            if (list.size() >= 5) {
                wordFrequencyCache.put(userGuid, list);
            }
        }
        return list;
    }

    @Override
    public void addOnlineUser(String userGuid) {
        userCache.put(userGuid, userGuid);
    }

    @Override
    public void removeOnlineUser(String guid) {
        userCache.flush(guid);
    }

    @Override
    public int getOnlineUserCount() {
        return userCache.keys().size();
    }

    @Override
    public BooleanResponse isUserOnline(String guid) {
        return new BooleanResponse(userCache.contains(guid));
    }

    @Override
    public void setSessionRefreshNeeded(String guid) {
        // don't mark the user's session for refresh if there isn't one
        if (userCache.contains(guid)) {
            userRefreshCache.put(guid, guid);
        }
    }

    @Override
    public BooleanResponse isSessionRefreshNeeded(String guid) {
        boolean refresh = userRefreshCache.contains(guid);
        if (refresh) {
            userRefreshCache.flush(guid);
        }
        return new BooleanResponse(refresh);
    }

    @Override
    public PaginationResults<SearchResult<User>> explore(ExploreRequest er) {
        List<SearchResult<User>> results = null;
        boolean cacheResults = er.isCacheResults();
        if (cacheResults) {
            userLastExploreRequestCache.put(er.getUserGuid(), er);
            results = userExploreResultsCache.get(er);
        }

        GeoPoint zipPoint = null;
        if (er.hasZipCode()) {
            zipPoint = geoUtil.fetchGeoInfoForZipCode(er.getZipCode());
        }

        if (results == null || results.isEmpty()) {
            List<User> exploredUsers = userDao.explore(er);
            results = new ArrayList<SearchResult<User>>(exploredUsers.size());

            for (User exploredUser : exploredUsers) {
                SearchResult searchResult = new SearchResult();
                searchResult.setEntity(exploredUser);
                results.add(searchResult);
            }
            if (cacheResults) {
                userExploreResultsCache.put(er, results);
            }
        }
        return buildPaginationResults(results, er.getStartIndex(), er.getMaxResults(), new UserManager.SearchResultPopulatorClosure(er.getUserGuid(), zipPoint));
    }

    private class SearchResultPopulatorClosure implements Closure<SearchResult<User>> {

        private String currentUserGuid;
        private GeoPoint zipPoint;

        private SearchResultPopulatorClosure(String currentUserGuid, GeoPoint zipPoint) {
            this.currentUserGuid = currentUserGuid;
            this.zipPoint = zipPoint;
        }

        @Override
        public void execute(SearchResult<User> searchResult) {
            if (searchResult.getLatest() == null) {
                searchResult.setLatest(activityManager.loadLastActivity(searchResult.getEntity().toEntityReference()));
            }
            if (searchResult.getWishStatus() == null) {
                searchResult.setWishStatus(bookmarkManager.loadWishStatus(currentUserGuid, searchResult.getEntity().getGuid()));
            }
            if (searchResult.getNetwork() == null) {
                searchResult.setNetwork(socialManager.loadRelationshipNetwork(searchResult.getEntity().getGuid()));
            }
            if (searchResult.getDistance() == null) {
                Double distance = calculateDistanceIfRequestWasByDistance(zipPoint, searchResult.getEntity());
                searchResult.setDistance(distance);
            }
            if (searchResult.getVouchCount() == null) {
                searchResult.setVouchCount(vouchDao.countVouchesForEntity(searchResult.getEntity().getGuid()));
            }
        }
    }


    private Double calculateDistanceIfRequestWasByDistance(GeoPoint geoPoint, User exploredUser) {
        Address location = exploredUser.getProfile().getLocation();
        return geoPoint != null && location != null ? geoUtil.findDistanceInMiles(location.getGeoLocation(), geoPoint) : null;
    }

    @Override
    public void affiliate(String affiliateIdentifier, String userGuid) {
        User affiliate = userDao.loadUserByGuid(affiliateIdentifier);
        if (affiliate == null) {
            affiliate = userDao.loadUserByUsername(affiliateIdentifier);
        }

        User user = userDao.loadUserByGuid(userGuid);

        if (affiliate == null) {
            throw new NotFoundException("Could not find affiliate with identifier : " + affiliateIdentifier);
        }

        User.UserType type = affiliate.getType();
        if (type != AFFILIATE && type != FOUNDER && type != VENDOR && type != PROMOTER && type != BLACKBOX_ADMIN && type != BLACKBOX_EMPLOYEE) {
            throw new IllegalArgumentException("Can affiliate registrtion with type: " + type);
        }

        if (user == null) {
            throw new NotFoundException("Could not find user with identifier : " + userGuid);
        }

        AffiliateMapping mapping = affiliateMappingDao.loadByAffiliatesGuid(affiliate.getGuid());
        if (mapping == null) {
            mapping = new AffiliateMapping();
        }

        mapping.setAffiliate(affiliate);

        Collection<User> users = mapping.getUsers();
        if (users == null) {
            mapping.setUsers(newArrayList(user));
        } else {
            mapping.getUsers().add(user);
        }

        affiliateMappingDao.save(mapping);
    }

    @Override
    public UserPersona loadUserPersona(final String guid, final String callerGuid) {

        UserPersona userPersona = new UserPersona();
        RelationshipNetwork network = socialManager.loadRelationshipNetwork(guid);
        Function<String, Relationship> relationshipFunction = new Function<String, Relationship>() {
            @Override
            public Relationship apply(String from) {
                return socialManager.loadRelationshipByEntities(guid, from);
            }
        };

        userPersona.setFriends(transform(network.getFriends(), relationshipFunction));
        userPersona.setFollowing(transform(network.getFollowing(), relationshipFunction));
        userPersona.setRelationships(transform(network.getInRelationship(), relationshipFunction));
        List<Relationship> followedBy = socialManager.loadFollowedBy(guid);
        userPersona.setFollowedBy(followedBy);
        List<ViewedBy> viewedBy = loadViewersByDestGuid(guid);
        userPersona.setViewedBy(viewedBy);
        if (guid.equals(callerGuid)) {
            // Then we should show the pending relationships too
            Collection<Relationship> relationships = userPersona.getRelationships();
            Collection<String> pending = network.getWithRelationStatus(Relationship.RelationStatus.IN_RELATIONSHIP_PENDING);
            // had to copy values to a new arraylist since google's abstract list doesn't support addAll
            relationships = new ArrayList<Relationship>(relationships);
            relationships.addAll(transform(pending, relationshipFunction));
            userPersona.setRelationships(relationships);
        }

        for (Relationship r : userPersona.getRelationships()) {
            r.setToEntityObject(userDao.loadUserByGuid(r.getToEntity().getGuid()));
        }

        // needs to be cached
        MediaMetaData data = mediaManager.loadProfileMetaMediaData(new EntityReference(EntityType.USER, guid));
        if (data != null) {
            userPersona.setProfileImageUrl(data.getLocation());
        }
        userPersona.setWishStatus(bookmarkManager.loadWishStatus(callerGuid, guid));

        return userPersona;
    }

    @Override
    public ExploreRequest lastSearch(String guid) {
        return userLastExploreRequestCache.get(guid);
    }

    @Override
    public List<ExternalCredentials> loadExternalCredentialsForUser(String userGuid) {
        return externalCredentialsDao.loadByOwner(userGuid);
    }

    @Override
    public ExternalCredentials saveExternalCredential(ExternalCredentials ec) {
        return externalCredentialsDao.save(ec);
    }

    @Override
    public ExternalCredentials loadExternalCredentialsForUserAndCredType(String userGuid, ExternalCredentials.CredentialType type) {
        return externalCredentialsDao.loadByOwnerAndCredType(userGuid, type);
    }

    @Override
    public void captureEmail(String email) {
        userDao.save(new EmailCapture(email));
    }

    @Override
    public BasePromoCode loadPromoCodeByCode(String promoCode) {
        return promoCodeDao.loadPromoCodeByCode(promoCode);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userDao.isUsernameAvailable(username);

    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userDao.isEmailAvailable(email);
    }

    @Transactional
    public SingleUsePromoCode createSingleUsePromoCode(SingleUsePromoCode code) {
        assert code != null;
        assert code.getEmail() != null;
        assert code.getMaster() != null;
        assert code.getMaster().getGuid() != null;

        // lets make sure master is up to date, and this will also allow for just
        // the guid to be passed into the user
        User user = userDao.loadUserByGuid(code.getMaster().getGuid());
        code.setMaster(user);
        promoCodeDao.generateUniquePromoCode(code);
        promoCodeDao.save(code);
        return code;
    }

    @Transactional
    public MultiUserPromoCode createMultiUserPromoCode(MultiUserPromoCode code) {
        assert code != null;
        assert code.getMaster() != null;
        assert code.getMaster().getGuid() != null;

        // lets make sure master is up to date, and this will also allow for just
        // the guid to be passed into the user
        User user = userDao.loadUserByGuid(code.getMaster().getGuid());
        code.setMaster(user);
        promoCodeDao.generateUniquePromoCode(code);
        promoCodeDao.save(code);
        return code;
    }


    @Override
    public Privacy loadPrivacy(String guid) {
        Privacy privacy = privacyCache.get(guid);
        if (privacy == null) {
            privacy = securityDao.loadPrivacy(guid);
            privacyCache.put(guid, privacy);
        }
        return privacy;
    }
}
