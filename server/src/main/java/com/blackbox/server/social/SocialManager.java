/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.social;

import com.blackbox.EntityReference;
import com.blackbox.EntityType;
import com.blackbox.activity.IActivity;
import com.blackbox.bookmark.Bookmark;
import com.blackbox.media.MediaMetaData;
import com.blackbox.notification.Notification;
import com.blackbox.notification.NotificationGroup;
import com.blackbox.notification.Notifications;
import com.blackbox.occasion.Attendee;
import com.blackbox.occasion.AttendingStatus;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionLayout;
import com.blackbox.server.bookmark.IBookmarkDao;
import com.blackbox.server.exception.NotFoundException;
import com.blackbox.server.gifting.IGiftingDao;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.occasion.IOccasionDao;
import com.blackbox.server.social.event.CreateRelationshipEvent;
import com.blackbox.server.social.event.UpdateRelationshipEvent;
import com.blackbox.server.social.event.VouchEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.social.*;
import com.blackbox.user.IUserManager;
import com.blackbox.user.MiniProfile;
import com.blackbox.user.PaginationResults;
import com.blackbox.user.User;
import com.blackbox.util.Count;
import com.blackbox.util.DateUtil;
import com.blackbox.util.NameInfo;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.lib.util.Pair;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blackbox.Utils.reversePair;
import static com.blackbox.notification.Notification.Type;
import static com.blackbox.server.util.OccasionUtil.cleanAttendee;
import static com.blackbox.social.Connection.ConnectionType;
import static com.blackbox.social.Relationship.RelationStatus;
import static com.blackbox.social.Relationship.RelationStatus.*;
import static com.blackbox.social.UserVouch.VouchDirection;
import static com.blackbox.user.User.UserType;
import static com.blackbox.util.PaginationUtil.buildPaginationResults;

/**
 *
 *
 */
@Service("socialManager")
@SuppressWarnings("unchecked")
public class SocialManager extends BaseServiceContainer implements ISocialManager {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceContainer.class);

    @Resource(name = "miniProfileCache")
    ICacheManager<String, MiniProfile> miniProfileCache;

    @Resource(name = "relationshipCache")
    ICacheManager<String, RelationshipNetwork> relationshipCache;

    @Resource(name = "followedByCache")
    ICacheManager<String, List<Relationship>> followedByCache;

    @Resource(name = "vouchTargetCache")
    ICacheManager<String, List<Vouch>> vouchTargetCache;

    @Resource(name = "vouchVoucherCache")
    ICacheManager<String, List<Vouch>> vouchVoucherCache;

    @Resource(name = "connectionCache")
    ICacheManager<String, List<Connection>> connectionCache;

    @Resource(name = "userVouchCache")
    ICacheManager<String, List<UserVouch>> userVouchCache;

    @Resource(name = "vouchCountCache")
    ICacheManager<VouchCountCacheKey, Integer> vouchCountCache;

    @Resource(name = "vouchDao")
    IVouchDao vouchDao;

    @Resource(name = "ignoreDao")
    IIgnoreDao ignoreDao;

    @Resource(name = "networkDao")
    INetworkDao networkDao;

    @Resource(name = "userManager")
    IUserManager userManager;

    @Resource
    IUserDao userDao;

    @Resource(name = "bookmarkDao")
    IBookmarkDao bookmarkDao;

    @Resource(name = "mediaDao")
    IMediaDao mediaDao;

    @Resource(name = "occasionDao")
    IOccasionDao occasionDao;

    @Resource(name = "giftingDao")
    IGiftingDao giftingDao;


    public ICacheManager<String, MiniProfile> getMiniProfileCache() {
        return miniProfileCache;
    }

    @Override
    @Transactional(readOnly = true)
    public RelationshipNetwork loadRelationshipNetwork(String guid) {

        RelationshipNetwork network = relationshipCache.get(guid);


        if (network == null) {

            Map<String, RelationStatus> relationshipMap = new HashMap<String, RelationStatus>();
            Map<String, NameInfo> nameInfoMap = new HashMap<String, NameInfo>();

            List<Relationship> relationships = networkDao.loadRelationshipsByFromEntityGuid(guid);

            for (Relationship r : relationships) {
                User user = userDao.loadUserByGuid(r.getToEntity().getGuid());
                if (user != null) {
                    RelationStatus rs = RelationStatus.getClosestStatusForWeight(r.getWeight());
                    relationshipMap.put(user.getGuid(), rs);

                    if (rs == FRIEND || rs == RelationStatus.IN_RELATIONSHIP_PENDING || rs == RelationStatus.IN_RELATIONSHIP) {
                        nameInfoMap.put(user.getGuid(), new NameInfo(user.getFirstname() + " " + user.getLastname(), user.getUsername()));
                    } else {
                        nameInfoMap.put(user.getGuid(), new NameInfo(user.getUsername(), user.getUsername()));
                    }
                }
            }

            network = new RelationshipNetwork(relationshipMap, nameInfoMap);
            relationshipCache.put(guid, network);
        }

        return network;
    }

    @Override
    @Transactional
    public void relate(Relationship relationship) {
        Relationship r = networkDao.loadRelationshipByEntities(relationship.getFromEntity().getGuid(),
                relationship.getToEntity().getGuid());

        boolean isNew = true;
        if (r == null) {
            r = relationship;
        } else {
            isNew = false;

            // Dont' override friend requests/friends with following
            if (r.getWeight() > relationship.getWeight() &&
                    relationship.getWeight() < FRIEND.getWeight()) {
                return;
            }

            r.setDescription(relationship.getDescription());
            r.setPreviousWeight(r.getWeight());
            r.setWeight(relationship.getWeight());
            r.setAcknowledged(relationship.isAcknowledged());
        }

        networkDao.save(r);

        // remove any ignores from the current user to the other user
        //noinspection EmptyCatchBlock
        try {
            deleteIgnore(r.getFromEntity().getGuid(), r.getToEntity().getGuid());
        }
        catch (Exception e) {
            // doesn't matter, just checking to see if there are 
        }

        flushRelationshipCache(r.getFromEntity().getGuid(), r.getToEntity().getGuid());
        if (isNew) {
            getEventMulticaster().process(new CreateRelationshipEvent(r));
        }
    }

    public List<Relationship> friendRequests(String userGuid) {
        return networkDao.loadRelationshipPending(userGuid);
    }

    @Transactional
    public void acceptRequest(String acceptorGuid, String requestorGuid) {

        Relationship relationship = networkDao.loadRelationshipByEntities(requestorGuid, acceptorGuid);
        if (relationship != null) {
            if (relationship.getWeight() == FRIEND_PENDING.getWeight()) {
                relationship.setWeight(FRIEND.getWeight());
            } else if (relationship.getWeight() == IN_RELATIONSHIP_PENDING.getWeight()) {
                relationship.setWeight(RelationStatus.IN_RELATIONSHIP.getWeight());
            }
            relationship.setAcknowledged(true);
            networkDao.save(relationship);

            Relationship r = networkDao.loadRelationshipByEntities(acceptorGuid, requestorGuid);
            if (r == null) {
                r = new Relationship();
                r.setFromEntity(relationship.getToEntity());
                r.setToEntity(relationship.getFromEntity());
            }
            r.setDescription(relationship.getDescription());
            r.setWeight(relationship.getWeight());
            networkDao.save(r);

            flushRelationshipCache(acceptorGuid, requestorGuid);
            getEventMulticaster().process(new CreateRelationshipEvent(r));
            getEventMulticaster().process(new UpdateRelationshipEvent(relationship));
        } else {
            throw new NotFoundException(String.format("No friend request from %s to %s could be found", requestorGuid, acceptorGuid));
        }
    }

    public void rejectRequest(String rejectorGuid, String requestorGuid) {
        Relationship relationship = networkDao.loadRelationshipByEntities(requestorGuid, rejectorGuid);
        if (relationship != null) {
            if (relationship.getPreviousWeight() != null) {
                relationship.setAcknowledged(true);
                relationship.setDescription(null);
                relationship.setWeight(relationship.getPreviousWeight());
                networkDao.save(relationship);
                getEventMulticaster().process(new UpdateRelationshipEvent(relationship));
            } else {
                networkDao.delete(relationship);
            }
            flushRelationshipCache(requestorGuid);
        } else {
            throw new NotFoundException(String.format("No friend request from %s to %s could be found", requestorGuid, rejectorGuid));
        }
    }


    @Override
    @Transactional(readOnly = false)
    public void deleteRelationship(String fromGuid, String toGuid) {

        Relationship relationship = networkDao.loadRelationshipByEntities(fromGuid, toGuid);
        if (relationship != null && relationship.getWeight() >= 10) {
            networkDao.deleteRelationship(fromGuid, toGuid);
            networkDao.deleteRelationship(toGuid, fromGuid);
        } else {
            networkDao.deleteRelationship(fromGuid, toGuid);
        }

        flushRelationshipCache(toGuid, fromGuid);
    }


    @Override
    @Transactional(readOnly = true)
    public Relationship loadRelationshipByGuid(String relationshipGuid) {
        return networkDao.loadRelationshipByGuid(relationshipGuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Relationship> loadRelationshipsByToEntityGuid(String toEntityGuid) {
        return networkDao.loadRelationshipsByToEntityGuid(toEntityGuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Relationship loadRelationshipByEntities(String fromGuid, String toGuid) {

        return networkDao.loadRelationshipByEntities(fromGuid, toGuid);
    }

    public List<Relationship> loadFollowedBy(String guid) {
        List<Relationship> followedBy = followedByCache.get(guid);
        if (followedBy == null) {
            followedBy = networkDao.loadRelationshipsByToEntityGuidAndWeightRange(guid, FOLLOW.getWeight(), FRIEND.getWeight());
            followedByCache.put(guid, followedBy);
        }
        return followedBy;
    }

    @Override
    public List<Relationship> loadRelationships(String guid, String callerGuid) {
        if (guid.equals(callerGuid)) {
            return networkDao.loadRelationshipsByFromEnityAndWeights(guid,
                    RelationStatus.IN_RELATIONSHIP.getWeight(), RelationStatus.IN_RELATIONSHIP_PENDING.getWeight());
        } else {
            return networkDao.loadRelationshipsByFromEnityAndWeights(guid,
                    RelationStatus.IN_RELATIONSHIP.getWeight());
        }
    }

    @Override
    public Vouch vouch(VouchRequest request) {
        Vouch vouch = request.getVouch();
        Vouch oldVouch = vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid());
        boolean isPermanent = request.isPermanent();
        User user = userDao.loadUserByGuid(vouch.getVoucher().getGuid());
        User.UserType userType = user.getType();

        if (userType == UserType.LIMITED && isPermanent) {
            // limited users are not allowed to permanently vouch anyone
            throw new VouchException("Limited users cannot create permanent vouches");

        } else if (oldVouch == null) {

            int count = monthlyVouchCount(vouch.getVoucher().getGuid(), isPermanent).getValue();

            if (isPermanent && count > user.getMaxPermanentVouches()) {
                // founders and normal users only get 5 permanent vouches per month
                throw new VouchException("The number of monthly vouches has been exceeded");

            } else if (!isPermanent && count > user.getMaxTemporaryVouches()) {
                throw new VouchException(("The number of month vouches has been exceeded"));
            }

            if (!isPermanent) {
                vouch.setExpirationDate(new DateTime().plusDays(Vouch.TEMPORARY_DAYS));
            } else {
                vouch.setExpirationDate(new DateTime().plusDays(Vouch.PERMANENT_DAYS));
            }

        } else {

            if (Vouch.isPermanent(oldVouch)) {
                throw new VouchException("Cannot revouch permanent vouches");
            }

            // else if the user user is previously vouched by them, it doesn't cost to revouch
            // we will reuse the old vouch and just change the status backed to active
            vouch = oldVouch;
            vouch.setCount(vouch.getCount() + 1);

            if (new DateTime().isBefore(vouch.getExpirationDate().minusDays(Vouch.REVOUCH_WINDOW_BEFORE_EXPIRE))) {
                throw new VouchException("Cannot revouch before 30 days to expire");
            }


            if (vouch.getCount() >= Vouch.TEMPORARY_PERMANENT_CONVERSION_COUNT || isPermanent) {
                vouch.setExpirationDate(new DateTime().plusDays(Vouch.PERMANENT_DAYS));
            } else {
                vouch.setExpirationDate(vouch.getExpirationDate().plusDays(Vouch.TEMPORARY_DAYS));
            }

        }


        Vouch v = vouchDao.save(vouch);
        flushVouchCache(vouch.getTarget().getGuid(), vouch.getVoucher().getGuid());
        getEventMulticaster().process(new VouchEvent(v));

        return v;
    }

    @Override
    public Vouch saveVouch(Vouch vouch) {
        vouch = vouchDao.save(vouch);
        MiniProfile miniProfile = miniProfileCache.get(vouch.getTarget().getGuid());
        if (miniProfile != null) {
            miniProfile.setTotalVouches(miniProfile.getTotalVouches() + 1);
        }
        flushVouchCache(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid());
        return vouch;
    }

    public void deleteVouch(String vouchGuid) {
        Vouch vouch = vouchDao.loadByGuid(vouchGuid);
        if (vouch != null) {
            vouchDao.delete(vouch);
            MiniProfile miniProfile = miniProfileCache.get(vouch.getTarget().getGuid());
            if (miniProfile != null) {
                miniProfile.setTotalVouches(miniProfile.getTotalVouches() - 1);
            }
            flushVouchCache(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid());
        }
    }

    @Override
    public void deleteVouch(String voucherGuid, String targetGuid) {
        Vouch vouch = vouchDao.loadByVoucherAndTarget(voucherGuid, targetGuid);
        if (vouch != null) {
            vouchDao.delete(vouch);
            MiniProfile miniProfile = miniProfileCache.get(targetGuid);
            if (miniProfile != null) {
                miniProfile.setTotalVouches(miniProfile.getTotalVouches() - 1);
            }
            flushVouchCache(voucherGuid, targetGuid);
        }
    }

    @Override
    public List<Vouch> loadVouchByVoucher(String voucherGuid) {
        List<Vouch> vouches = vouchVoucherCache.get(voucherGuid);
        if (vouches == null) {
            List<Vouch> list = vouchDao.loadByVoucher(voucherGuid);
            vouches = list;
            vouchVoucherCache.put(voucherGuid, vouches);
        }
        return vouches;
    }

    @Override
    public List<Vouch> loadOutgoingByVoucher(String voucherGuid) {
        List<Vouch> vouches = vouchVoucherCache.get(voucherGuid);
        if (vouches == null) {
            List<Vouch> list = vouchDao.loadOutgoingByVoucher(voucherGuid);
            vouches = list;
            vouchVoucherCache.put(voucherGuid, vouches);
        }
        return vouches;
    }

    @Override
    public List<Vouch> loadVouchByTarget(String targetGuid) {
        List<Vouch> vouches = vouchTargetCache.get(targetGuid);
        if (vouches == null) {
            vouches = vouchDao.loadByTarget(targetGuid);
            vouchTargetCache.put(targetGuid, vouches);
        }
        return vouches;
    }

    public Count monthlyVouchCount(String guid, boolean permanent) {
        DateTime startDay = DateUtil.firstDayOfMonth();

        VouchCountCacheKey key = new VouchCountCacheKey(guid, permanent, startDay);
        Integer count = vouchCountCache.get(key);
        if (count == null) {
            count = vouchDao.countByUserGuidTypeAndDate(guid, permanent, startDay);
            vouchCountCache.put(key, count);
        }
        return new Count(count);
    }

    @Override
    public Vouch loadVouchByVoucherAndVouchee(String fromGuid, String toGuid) {
        return vouchDao.loadVouchByVoucherAndVouchee(fromGuid, toGuid);
    }

    @Override
    public PaginationResults<UserVouch> loadUserVouches(final String guid, int startIndex, int maxResults) {
        List<UserVouch> vouches = userVouchCache.get(guid);
        boolean fromCache = true;
        if (vouches == null) {

            HashMap<Pair<String, String>, UserVouch> map = new HashMap<Pair<String, String>, UserVouch>();
            List<Vouch> list = vouchDao.loadUserVouches(guid);

            for (Vouch o : list) {
                Pair<String, String> guids = new Pair<String, String>(o.getVoucher().getGuid(), o.getTarget().getGuid());
                UserVouch vouch = map.get(guids);
                if (vouch == null) {
                    vouch = map.get(reversePair(guids));
                }

                String description = o.getDescription();
                boolean reverse = !guid.equals(guids.getFirst());

                if (vouch != null) {
                    vouch.setDirection(VouchDirection.MUTUAL);
                } else {
                    String from = guids.getFirst();
                    String to = guids.getSecond();
                    VouchDirection vouchDirection;
                    vouch = new UserVouch();
                    User fromUser = userDao.loadUserByGuid(from);
                    User toUser = userDao.loadUserByGuid(to);
                    vouch.setFromUser(fromUser);
                    vouch.setToUser(toUser);
                    if (!from.equals(guid)) {
                        vouchDirection = VouchDirection.INCOMING;
                        vouch.setUser(fromUser);
                    } else {
                        vouchDirection = VouchDirection.OUTGOING;
                        vouch.setUser(toUser);
                    }
                    vouch.setDirection(vouchDirection);
                    map.put(guids, vouch);
                }

                if (reverse) {
                    vouch.setFromDescription(description);
                } else {
                    vouch.setToDescription(description);
                }

            }
            vouches = new ArrayList<UserVouch>(map.values());
            userVouchCache.put(guid, vouches);
            fromCache = false;
        }

        PaginationResults<UserVouch> results = buildPaginationResults(vouches, startIndex, maxResults);

        return results;
    }

    @Override
    public List<EntityReference> loadIgnores(String ignorerGuid) {
        return ignoreDao.loadByIgnorerAndType(ignorerGuid, Ignore.IgnoreType.SOFT);
    }

    @Override
    public List<EntityReference> loadBlocks(String ignorerGuid) {
        return ignoreDao.loadByIgnorerAndType(ignorerGuid, Ignore.IgnoreType.HARD);
    }

    @Override
    public List<EntityReference> loadIgnoreByTarget(String targetGuid) {
        return ignoreDao.loadByTarget(targetGuid);
    }

    @Override
    public List<EntityReference> loadBlockedBy(String blockedGuid) {
        return ignoreDao.loadByTargetandType(blockedGuid, Ignore.IgnoreType.HARD);
    }

    @Override
    public void deleteIgnore(String fromGuid, String toGuid) {
        Ignore ignore = ignoreDao.loadByIgnorerAndTarget(fromGuid, toGuid);
        if (ignore == null) {
            throw new NotFoundException(String.format("No ignore could be found for %s:%s", fromGuid, toGuid));
        }

        ignoreDao.delete(ignore);
        userManager.setSessionRefreshNeeded(fromGuid);
        userManager.setSessionRefreshNeeded(toGuid);
    }

    @Override
    public Ignore loadIgnoreByIgnorerAndTarget(String ignorerGuid, String targetGuid) {
        return ignoreDao.loadByIgnorerAndTarget(ignorerGuid, targetGuid);
    }

    @Override
    public List<Relationship> loadReverseRelationships(String targetGuid) {
        return networkDao.loadRelationshipsByToEntityGuid(targetGuid);
    }


    public PaginationResults<Connection> loadConnections(final String guid, ConnectionType type, int startIndex, int maxResults) {

        List<Connection> connections = connectionCache.get(guid);
        boolean fromCache = true;

        if (connections == null) {
            fromCache = false;
            List<Relationship> relationships = networkDao.loadConnections(guid);

            HashMap<Pair<String, String>, Connection> map = new HashMap<Pair<String, String>, Connection>();

            for (Relationship relationship : relationships) {
                Pair<String, String> guids = new Pair<String, String>(relationship.getFromEntity().getGuid(), relationship.getToEntity().getGuid());
                Connection conn = map.get(guids);
                if (conn == null)
                    conn = map.get(reversePair(guids)); // check to see if we stored the value in the opposite direction

                // If the connection was already stored as followed or following that matches the currently relationsihp
                // then save the connection with mutual following
                if (conn != null && (conn.getType() == ConnectionType.FOLLOWED || conn.getType() == ConnectionType.FOLLOWING)) {
                    conn.setType(ConnectionType.MUTUAL_FOLLOWING);
                } else if (conn == null) {
                    String from = guids.getFirst();
                    String to = guids.getSecond();

                    String guidToLoad;
                    ConnectionType t = null;

                    // If the person sending is not the current user (incoming direction)
                    if (!from.equals(guid)) {
                        guidToLoad = from;

                        // The only relationships coming in this direction (incoming) we are care about are following
                        // If the relationship is not following ignore it.
                        if (RelationStatus.getClosestStatusForWeight(relationship.getWeight()) == RelationStatus.FOLLOW) {
                            t = ConnectionType.FOLLOWED;
                        }
                    } else {
                        // For outgoing relationships just determine the connection type
                        // form the relationship weight
                        guidToLoad = to;
                        t = ConnectionType.fromWeight(relationship.getWeight());
                    }

                    // If the relationship was a type we care about, create the connection object
                    // and add it to the map
                    if (t != null) {
                        User user = userDao.loadUserByGuid(guidToLoad);
                        conn = new Connection(user, t);
                        map.put(guids, conn);
                    }
                }
            }
            connections = new ArrayList<Connection>(map.values());
        }

        // FilterHelper based off type passed in, if there was a type and it wasn't just ALL
        if (type != ConnectionType.ALL) {
            ArrayList<Connection> copy = new ArrayList<Connection>(connections.size());
            for (Connection connection : connections) {
                if (connection.getType() == type) {
                    copy.add(connection);
                }
            }
            connections = copy;
        }

        // Hibernate will complain if there was sessions associated with objects and the session was disconnected
        // so we want to make sure we explicitly reattach the connections
        PaginationResults<Connection> results = buildPaginationResults(connections, startIndex, maxResults);

        return results;
    }

    @Transactional
    @Override
    public Ignore ignore(Ignore ignore) {

        Ignore i = ignoreDao.save(ignore);
        if (i.getType() == Ignore.IgnoreType.HARD && ignore.getTarget().getOwnerType() == EntityType.USER) {
            // also deletePrimary the relationship between the target user and ignorer
            // deletePrimary both directions
            deleteRelationship(ignore.getIgnorer().getGuid(), ignore.getTarget().getGuid());
            deleteRelationship(ignore.getTarget().getGuid(), ignore.getIgnorer().getGuid());

            // if the user is requesting to block this user, we need to take effect
            userManager.setSessionRefreshNeeded(ignore.getTarget().getGuid());
            // the ignorer will need to have his session rebuilt too
            userManager.setSessionRefreshNeeded(ignore.getIgnorer().getGuid());
        }
        return i;

    }

    @Override
    public Notification loadNewestNotification(String guid, Notification.Type type) {
        Notifications notifications = new Notifications();
        Notification notification = new Notification();
        if (Type.Follow.equals(type)) {
            loadFollowing(guid, 1, notifications);
            notification = loadNotification(notifications, Type.Follow);
        } else if (Type.Friend.equals(type)) {
            loadFriends(guid, 1, notifications);
            notification = loadNotification(notifications, Type.Friend);
        } else if (Type.Wish.equals(type)) {
            loadWishes(guid, 1, notifications);
            notification = loadNotification(notifications, Type.Wish);
        } else if (Type.Occasion.equals(type)) {
            loadOccasions(guid, 1, notifications);
            notification = loadNotification(notifications, Type.Occasion);
        }
        if (notification != null) {
            notification.setType(type);
        }
        return notification;
    }

    @Override
    public NotificationGroup loadNewestNotificationWithLimit(String guid, Notification.Type type, int limit) {
        Notifications notifications = new Notifications();
        NotificationGroup group = null;
        if (Type.Follow.equals(type)) {
            loadFollowing(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Friend.equals(type)) {
            loadFriends(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Wish.equals(type)) {
            loadWishes(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Relationship.equals(type)) {
            loadWishes(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Gift.equals(type)) {
            loadWishes(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Vouch.equals(type)) {
            loadWishes(guid, limit, notifications);
            group = notifications.getGroup(type);
        } else if (Type.Occasion.equals(type)) {
            loadOccasions(guid, limit, notifications);
            group = notifications.getGroup(type);
        }
        return group;
    }

    @Override
    public Vouch loadVouchByGuid(String guid) {
        return vouchDao.loadByGuid(guid);
    }


    private Notification loadNotification(Notifications notifications, Notification.Type type) {
        final NotificationGroup group = notifications.getGroup(type);
        Notification notification = null;
        if (group != null) {
            final List<Notification> items = group.getItems();
            if (items.size() > 0) {
                notification = items.get(0);
            }
        }
        return notification;
    }

    @Override
    public Notifications loadLimitNotifications(String guid, int limit) {
        Notifications notifications = new Notifications();
        loadNotifications(guid, limit, notifications);
        return notifications;
    }

    @Override
    public Notifications loadNotifications(String guid) {
        Notifications notifications = new Notifications();
        loadNotifications(guid, Integer.MAX_VALUE - 1, notifications);
        return notifications;
    }

    private void loadNotifications(String guid, int limit, Notifications notifications) {
        loadFollowing(guid, limit, notifications);
        loadFriends(guid, limit, notifications);
        loadWishes(guid, limit, notifications);
        loadRelationships(guid, limit, notifications);
        loadGifts(guid, limit, notifications);
        loadVouches(guid, limit, notifications);
        loadOccasions(guid, limit, notifications);
    }

    private void loadOccasions(String guid, int limit, Notifications notifications) {
        List<Occasion> occasions = occasionDao.loadUnAcknowledged(guid, limit);
        if (occasions != null) {
            NotificationGroup occasionGroup = new NotificationGroup();
            occasionGroup.setType(Type.Occasion);
            occasionGroup.setTotal(occasions.size());
            int totalNotifications = 0;
            for (Occasion occasion : occasions) {
                cleanAttendee(occasion);
                Notification notification = new Notification();
                notification.setType(Type.Occasion);
                Attendee attendee = occasionDao.loadAttendeeByGuidAndOccasionGuid(occasion.getGuid(), guid);
                //must be invited....
                if (attendee != null) {
                    notification.setOccasion(occasion);
                    AttendingStatus status = attendee.getAttendingStatus();
                    if (AttendingStatus.REQUESTED_INVITATION.equals(status)) {
                        User user = userDao.loadUserByGuid(attendee.getBbxUserGuid());
                        notification.setUser(user);
                    }
                    notification.setAttendingStatus(status);
                    notification.setGuid(occasion.getGuid());
                    notification.setAttendeeGuid(attendee.getGuid());
                    OccasionLayout layout = occasion.getLayout();
                    String imageGuid = layout.getImageGuid();
                    if (StringUtils.isNotBlank(imageGuid)) {
                        MediaMetaData md = mediaDao.loadMediaMetaDataByGuid(imageGuid);
                        layout.setTransiantImage(md);
                    }
                    occasionGroup.addNotification(notification);
                    ++totalNotifications;
                }
            }
            occasionGroup.setTotalNotifications(occasionDao.loadTotalUnAcknowledged(guid));
            notifications.addGroup(occasionGroup);
        }
    }

    private void loadWishes(String guid, int limit, Notifications notifications) {
        //load wishes
        final List<Bookmark> bookmarkList = bookmarkDao.loadByTargetGuidAndBookmarkType(guid, Bookmark.BookmarkType.WISH);
        if (bookmarkList != null && !bookmarkList.isEmpty()) {
            NotificationGroup wishesGroup = new NotificationGroup();
            wishesGroup.setType(Type.Wish);
            wishesGroup.setTotal(bookmarkList.size());
            int totalNotifications = 0;
            for (Bookmark bookmark : bookmarkList) {

                if (Bookmark.BookmarkType.WISH.equals(bookmark.getType()) && !bookmark.isAcknowledged()) {
                    if (totalNotifications < limit) {

                        Notification notification = new Notification();
                        notification.setType(Type.Wish);
                        final String wishingGuid = bookmark.getOwner().getGuid();
                        notification.setGuid(bookmark.getGuid());
                        populateNotificationEntity(notification, wishingGuid);
                        List<Bookmark> wishing = bookmarkDao.loadByUserAndTargetGuidAndBookmarkType(guid, wishingGuid, Bookmark.BookmarkType.WISH);
                        if (wishing != null && wishing.size() > 0) {
                            notification.setWishing(true);
                        } else {
                            notification.setWishing(false);
                        }
                        wishesGroup.addNotification(notification);
                        ++totalNotifications;
                    }
                }
            }
            wishesGroup.setTotalNotifications(totalNotifications);
            notifications.addGroup(wishesGroup);
        }
    }

    private void loadGifts(String guid, int limit, Notifications notifications) {
        List<IActivity> gifts = giftingDao.loadVirtualGiftsForRecipient(guid, false);
        if (gifts != null && !gifts.isEmpty()) {
            NotificationGroup group = new NotificationGroup();
            group.setType(Type.Gift);
            group.setTotal(giftingDao.countVirtualGiftsForRecipient(guid, true));
            int total = 0;
            for (IActivity gift : gifts) {
                if (total < limit) {
                    Notification notification = new Notification();
                    notification.setType(Type.Gift);

                    notification.setGuid(gift.getGuid());
                    String userGuid = gift.getSender().getGuid();
                    populateNotificationEntity(notification, userGuid);
                    group.addNotification(notification);
                    ++total;
                } else {
                    break;
                }
            }
            group.setTotalNotifications(total);
            notifications.addGroup(group);
        }
    }

    private void loadFriends(String guid, int limit, Notifications notifications) {
        //load friends
        final List<Relationship> friendsPendingList = networkDao.loadRelationshipsByToEnityAndWeights(guid, FRIEND_PENDING.getWeight());
        if (friendsPendingList != null && !friendsPendingList.isEmpty()) {

            NotificationGroup friendsGroup = new NotificationGroup();
            friendsGroup.setType(Type.Friend);
            friendsGroup.setTotal(networkDao.countFriendsOrGreater(guid));
            int totalNotifications = 0;
            for (Relationship relationship : friendsPendingList) {
                if (!relationship.isAcknowledged()) {
                    if (totalNotifications < limit) {
                        Notification notification = new Notification();
                        notification.setType(Notification.Type.Friend);
                        notification.setGuid(relationship.getGuid());
                        final String fromEntityGuid = relationship.getFromEntity().getGuid();
                        boolean valid = populateNotificationEntity(notification, fromEntityGuid);
                        notification.setDisplayName(notification.getUsername());
                        if (valid) friendsGroup.addNotification(notification);
                        ++totalNotifications;
                    }
                }
            }
            friendsGroup.setTotalNotifications(friendsPendingList.size());

            notifications.addGroup(friendsGroup);
        }
    }

    private void loadFollowing(String guid, int limit, Notifications notifications) {
        //load following
        final List<Relationship> followingList = networkDao.loadRelationshipsByToEntityGuidAndWeightRange(guid, FOLLOW, FRIEND);
        if (followingList != null && !followingList.isEmpty()) {
            NotificationGroup followingGroup = new NotificationGroup();
            followingGroup.setType(Type.Follow);
            followingGroup.setTotal(followingList.size());
            int totalNotifications = 0;
            for (Relationship relationship : followingList) {
                if (!relationship.isAcknowledged()) {
                    if (totalNotifications < limit) {
                        Notification notification = new Notification();
                        notification.setType(Type.Follow);
                        notification.setGuid(relationship.getGuid());
                        final String fromEntityGuid = relationship.getFromEntity().getGuid();
                        Relationship following = networkDao.loadRelationshipByEntitiesAndStatus(guid, fromEntityGuid, FOLLOW);
                        if (following != null) {
                            notification.setFollowing(true);
                        } else {
                            notification.setFollowing(false);
                        }
                        populateNotificationEntity(notification, fromEntityGuid);
                        followingGroup.addNotification(notification);
                        ++totalNotifications;
                    }
                }

            }
            followingGroup.setTotalNotifications(totalNotifications);
            notifications.addGroup(followingGroup);
        }
    }

    private void loadRelationships(String guid, int limit, Notifications notifications) {
        //load friends
        final List<Relationship> relationshipPending = networkDao.loadRelationshipsByToEnityAndWeights(guid, IN_RELATIONSHIP_PENDING.getWeight());
        if (relationshipPending != null) {
            NotificationGroup group = new NotificationGroup();
            group.setType(Type.Relationship);
            group.setTotal(relationshipPending.size());
            int totalNotifications = 0;
            for (Relationship relationship : relationshipPending) {
                if (!relationship.isAcknowledged()) {
                    if (totalNotifications < limit) {
                        Notification notification = new Notification();
                        notification.setType(Notification.Type.Relationship);
                        notification.setGuid(relationship.getGuid());
                        final String fromEntityGuid = relationship.getFromEntity().getGuid();
                        boolean valid = populateNotificationEntity(notification, fromEntityGuid);
                        notification.setDisplayName(notification.getUsername());
                        if (valid) group.addNotification(notification);
                    }
                    ++totalNotifications;
                }
            }
            group.setTotalNotifications(totalNotifications);
            notifications.addGroup(group);
        }
    }


    private void loadVouches(String guid, int limit, Notifications notifications) {

        List<Vouch> vouches = vouchDao.loadAllByTarget(guid, false);
        if (vouches != null && !vouches.isEmpty()) {
            NotificationGroup group = new NotificationGroup();
            group.setType(Type.Vouch);
            group.setTotal(vouches.size());
            int totalNotifications = 0;
            for (Vouch vouch : vouches) {
                if (!vouch.isAccepted()) {
                    if (totalNotifications < limit) {
                        Notification notification = new Notification();
                        notification.setType(Type.Vouch);
                        notification.setGuid(vouch.getGuid());
                        populateNotificationEntity(notification, vouch.getVoucher().getGuid());
                        group.addNotification(notification);
                        ++totalNotifications;
                    }
                }
            }
            group.setTotalNotifications(totalNotifications);
            notifications.addGroup(group);
        }
    }

    private boolean populateNotificationEntity(Notification notification, String fromEntityGuid) {
        final User user = userDao.loadUserByGuid(fromEntityGuid);
        if (user != null) {
            notification.setFromEntityGuid(user.getGuid());
            notification.setDisplayName(user.getUsername());
            notification.setUsername(user.getUsername());
            notification.setImage(user.getProfile().getProfileImgUrl());
            notification.setAvatarImage(user.getProfile().getAvatarUrl());
            return true;
        } else {
            logger.warn("Attempted to create a notification from an invalid guid: " + fromEntityGuid);
            return false;
        }
    }

    private void flushRelationshipCache(String... guid) {
        for (String s : guid) {
            relationshipCache.flush(s);
            followedByCache.flush(s);
            connectionCache.flush(s);
            userManager.setSessionRefreshNeeded(s);
        }
    }

    private void flushVouchCache(String... guid) {
        DateTime dt = new DateMidnight().minusMonths(1).toDateTime();
        for (String s : guid) {
            vouchTargetCache.flush(s);
            vouchVoucherCache.flush(s);
            userVouchCache.flush(s);

            vouchCountCache.flush(new VouchCountCacheKey(s, true, dt));
            vouchCountCache.flush(new VouchCountCacheKey(s, false, dt));
        }
    }

    /**
     * Maps {@link com.blackbox.social.Relationship.RelationStatus} to {@link NetworkTypeEnum}
     *
     * @param weight The relationship weight
     * @return The NetworkTypeEnum
     */
    public NetworkTypeEnum toNetworkType(int weight) {
        NetworkTypeEnum type = null;
        Relationship.RelationStatus relationStatus = Relationship.RelationStatus.getClosestStatusForWeight(weight);
        if (Relationship.RelationStatus.FOLLOW.equals(relationStatus)) {
            type = NetworkTypeEnum.FOLLOWING;
        } else if (FRIEND.equals(relationStatus)) {
            type = NetworkTypeEnum.FRIENDS;
        }
        return type;
    }

}
