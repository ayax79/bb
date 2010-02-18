package com.blackbox.server.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.common.ActivityThreadParentOwnerTypePredicate;
import com.blackbox.foundation.common.TwoBounds;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.util.Affirm;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.util.CollectionHelper;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.util.PersistenceUtil;
import org.compass.core.CompassOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

import static com.blackbox.foundation.activity.AscendingActivityComparator.getAscendingActivityComparator;
import static com.blackbox.foundation.activity.DescendingActivityThreadComparator.getDescendingActivityThreadComparator;
import static com.blackbox.foundation.social.NetworkTypeEnum.*;
import static com.blackbox.foundation.util.PaginationUtil.subList;
import static com.blackbox.server.activity.ActivityUtil.createActivityThreadList;
import static com.blackbox.server.activity.ActivityUtil.isComment;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newTreeSet;
import static java.util.Collections.sort;

/**
 *
 *
 */
@Repository("activityStreamDao")
@SuppressWarnings("unchecked")
public class IbatisActivityStreamDao implements IActivityStreamDao {
    final private static Logger logger = LoggerFactory.getLogger(IbatisActivityStreamDao.class);

    @Resource(name = "messageDao")
    IMessageDao messageDao;

    @Resource(name = "mediaDao")
    IMediaDao mediaDao;

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;

    @Resource(name = "compassTemplate")
    CompassOperations compassTemplate;

    @Resource(name = "activityThreadChildrenResultCache")
    private ICacheManager<String, Collection<IActivity>> activityThreadChildrenResultCache;

    @Override
    public IActivity getLatestActivity(EntityReference entity) {
        return null;   /* todo: why is this method here? */
    }

    @Override
    public void setLatestActivity(IActivity activity) {
        /* todo: why is this method here? */
    }

    private void flushChildren(String parentGuid) {
        activityThreadChildrenResultCache.flush(parentGuid);
    }

    /**
     * Only loads the activities the entity published.
     *
     * @param entity
     * @param filterType
     * @param bounds
     * @return
     */
    @Override
    public Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds) {
        return loadPublishedActivities(entity, filterType, bounds, ALL_MEMBERS, FRIENDS, WORLD);
    }

    public Collection<IActivityThread> loadPublishedActivities(EntityReference entity, AssociatedActivityFilterType filterType, Bounds bounds, NetworkTypeEnum... types) {
        String guid = entity.getGuid();
        List<Message> list0 = messageDao.loadAllAssociatedMessages(guid, types);
        List<MediaMetaData> list1 = mediaDao.loadAllAssociatedMedia(guid, types);
        List<IActivity> union = new ArrayList<IActivity>(list0.size() + list1.size());
        filterActivity(filterType, guid, list0, union);
        filterActivity(filterType, guid, list1, union);
        sort(union, getAscendingActivityComparator());
        List<IActivityThread> threads = createActivityThreadList(union);
        sort(threads, getDescendingActivityThreadComparator());
        int startIndex = bounds.getStartIndex();
        int maxResults = bounds.getMaxResults();
        return subList(threads, startIndex, maxResults);
    }

    private <T extends IActivity> void filterActivity(AssociatedActivityFilterType filterType, String guid, List<T> fromList, List<IActivity> toList) {
        for (IActivity activity : fromList) {
            // if we are filtering by owner don't add things we aren't the owner for
            // vice versa for participant
            if (filterType == AssociatedActivityFilterType.OWNER &&
                    !activity.getSender().getGuid().equals(guid)) {
                continue;
            } else if (filterType == AssociatedActivityFilterType.PARTICIPANT &&
                    activity.getSender().getGuid().equals(guid)) {
                continue;
            }
            toList.add(activity);
        }
    }

    @Override
    public void deleteSender(IActivity activity) {
        if (activity != null) {
            flushChildren(activity.getGuid());
        }
    }

    /**
     * Deleted the full group of child activities
     *
     * @param activity
     * @param type     Type of relationship
     */
    @Override
    public void deleteAssociated(IActivity activity, NetworkTypeEnum type) {
        if (activity != null) {
            flushChildren(activity.getGuid());
        }
    }

    @Override
    public void deletePrimary(IActivity activity, NetworkTypeEnum type, boolean parent) {
        if (activity != null) {
            if (parent) {
                flushChildren(activity.getGuid());
            } else {
                flushChildren(activity.getParentActivity().getGuid());
            }
        }
    }

    @Override
    public Collection<IActivityThread> loadActivities(EntityReference entity, Collection<NetworkTypeEnum> types, TwoBounds bounds) {
        Affirm.isNotNull(entity, "entity", IllegalArgumentException.class);
        Affirm.isNotNull(bounds, "bounds", IllegalArgumentException.class);

        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("called for {0} ", entity));
        }
        Collection<IActivityThread> stream = newArrayList();
        if (FilterHelper.isEveryOneFilter(types)) {
            stream = loadEveryOne(entity.getGuid(), bounds);
        } else if (FilterHelper.isFriendFilter(types)) {
            stream = loadFriends(entity.getGuid(), bounds);
        } else if (FilterHelper.isFollowingFilter(types)) {
            stream = loadFollowing(entity.getGuid(), bounds);
        }
        associateStreamsThreadsWithNetworkTypesRequested(types, stream);
        return stream;
    }

    private void associateStreamsThreadsWithNetworkTypesRequested(Collection<NetworkTypeEnum> types, Collection<IActivityThread> stream) {
        for (IActivityThread thread : stream) {
            thread.setNetworkTypes(types);
        }
    }

    private <T extends IActivity> void buildThreadParts(List<T> parentActivities, int maxResults, List<IActivityThread> threads) {
        for (int i = 0, c = 0; i < parentActivities.size() && c < maxResults; i++, c++) {
            T activity = parentActivities.get(i);
            ActivityThread thread = new ActivityThread(activity);
            loadComments(thread);
            threads.add(thread);
        }
    }

    private void loadComments(IActivityThread thread) {
        IActivity parentActivity = thread.getParent();
        final String parentActivityGuid = parentActivity.getGuid();
        Collection<IActivity> children = activityThreadChildrenResultCache.get(parentActivityGuid);
        if (children == null) {
            children = thread.getChildren();
            addChildrenMessages(parentActivityGuid, children);
            addChildrenMediaMetaData(parentActivityGuid, children);
            activityThreadChildrenResultCache.put(parentActivityGuid, children);
        }
        thread.setChildren(children);
    }

    private void addChildrenMediaMetaData(final String parentActivityGuid, Collection<IActivity> children) {
        List<MediaMetaData> childrenMediaMetaData = template.selectList("Activity.loadChildMedia", parentActivityGuid);
        children.addAll(childrenMediaMetaData);
    }

    private void addChildrenMessages(final String parentActivityGuid, Collection<IActivity> children) {
        List<Message> childrenMessages = template.selectList("Activity.loadChildMessage", parentActivityGuid);
        children.addAll(childrenMessages);
    }

    private Collection<IActivityThread> loadEveryOne(final String guid, final TwoBounds bounds) {
        Map<String, Object> parameters = setEveryOneParameters(guid);
        return doLoading(bounds, parameters, "Activity.loadEverythingParentMedia", "Activity.loadEverythingParentMessage");
    }

    private Collection<IActivityThread> loadFollowing(final String guid, final TwoBounds bounds) {
        Map<String, Object> parameters = setFollowingParameters(guid);
        return doLoading(bounds, parameters, "Activity.loadMediaFollowing", "Activity.loadMessageFollowing");
    }

    private Collection<IActivityThread> loadFriends(final String guid, final TwoBounds bounds) {
        Map<String, Object> parameters = setFriendsParameters(guid);
        return doLoading(bounds, parameters, "Activity.loadFriendMedias", "Activity.loadFriendMessages");
    }

    private Collection<IActivityThread> doLoading(TwoBounds bounds, Map<String, Object> parameters, String mediaQueryTemplateName, String messageQueryTemplateName) {
        parameters.put("bounds", bounds.getMediaMetaDataBounds());
        List<MediaMetaData> parentMedia = template.selectList(mediaQueryTemplateName, parameters);

        parameters.remove("bounds");
        parameters.put("bounds", bounds.getMessageBounds());
        List<Message> parentMessage = template.selectList(messageQueryTemplateName, parameters);

        List<IActivityThread> threads = newArrayList();
        buildThreadParts(parentMedia, bounds.getMaxResults(), threads);
        buildThreadParts(parentMessage, bounds.getMaxResults(), threads);

        Collections.sort(threads);
        return updateBoundsSides(bounds, subList(threads, 0, bounds.getMaxResults()));
    }

    private List<IActivityThread> updateBoundsSides(TwoBounds bounds, List<IActivityThread> results) {
        // count message and media metadata results and update bounds
        int mediaActivities = CollectionHelper.countMatches(results, new ActivityThreadParentOwnerTypePredicate(ActivityTypeEnum.MEDIA));
        int messageActivities = CollectionHelper.countMatches(results, new ActivityThreadParentOwnerTypePredicate(ActivityTypeEnum.MESSAGE));
        bounds.getMediaMetaDataBounds().next(mediaActivities - bounds.getMediaMetaDataBounds().getMaxResults());
        bounds.getMessageBounds().next(messageActivities - bounds.getMessageBounds().getMaxResults());
        return results;
    }

    private Map<String, Object> setFollowingParameters(String guid) {
        Map<String, Object> parameters = newHashMap();
        parameters.put("guid", guid);
        parameters.put("followWeight", Relationship.RelationStatus.FOLLOW.getWeight());
        parameters.put("fromGuid", guid);
        parameters.put("world", NetworkTypeEnum.WORLD.getDatabaseIdentifier());
        parameters.put("allMembers", NetworkTypeEnum.ALL_MEMBERS.getDatabaseIdentifier());
        return parameters;
    }

    private Map<String, Object> setFriendsParameters(String guid) {
        Map<String, Object> parameters = newHashMap();
        parameters.put("friendWeight", Relationship.RelationStatus.FRIEND.getWeight());
        parameters.put("inRelationshipWeight", Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        parameters.put("fromGuid", guid);
        parameters.put("friend", NetworkTypeEnum.FRIENDS.getDatabaseIdentifier());
        return parameters;
    }

    private Map<String, Object> setEveryOneParameters(String guid) {
        Map<String, Object> parameters = newHashMap();
        parameters.put("followWeight", Relationship.RelationStatus.FOLLOW.getWeight());
        parameters.put("friendWeight", Relationship.RelationStatus.FRIEND.getWeight());
        parameters.put("inRelationshipWeight", Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        parameters.put("fromGuid", guid);
        parameters.put("friend", NetworkTypeEnum.FRIENDS.getDatabaseIdentifier());
        parameters.put("world", NetworkTypeEnum.WORLD.getDatabaseIdentifier());
        parameters.put("allMembers", NetworkTypeEnum.ALL_MEMBERS.getDatabaseIdentifier());
        return parameters;
    }

    @Override
    public void saveRecipient(IActivity activity, NetworkTypeEnum type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveRecipient(List<IActivity> activities, NetworkTypeEnum type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveSender(List<IActivity> activities, EntityReference entity) {
        for (IActivity activity : activities) {
            saveSender(activity, entity);
        }
    }

    @Override
    public void saveSender(IActivity activityToSave, EntityReference entity) {
        if (isComment(activityToSave)) {
            String parentGuid = activityToSave.getParentActivity().getGuid();
            if (activityThreadChildrenResultCache.contains(parentGuid)) {
                flushChildren(parentGuid);
            }
            Collection<IActivity> children = newTreeSet(AscendingActivityAndGuidComparator.getAscendingActivityComparator());
            addChildrenMediaMetaData(parentGuid, children);
            addChildrenMessages(parentGuid, children);
            activityThreadChildrenResultCache.put(parentGuid, children);
        }
    }

    @Override
    public Collection<IActivityThread> loadPublicActivity(final int startIndex, final int maxResults) {
        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("type", parameters);
        parameters.put("bounds", new Bounds(startIndex, maxResults));
        List<MediaMetaData> parentMedia = template.selectList("Activity.loadPublicMedia", parameters);
        List<Message> parentMessage = template.selectList("Activity.loadPublicMessage", parameters);

        List<IActivityThread> threads = newArrayList();
        buildThreadParts(parentMedia, maxResults, threads);
        buildThreadParts(parentMessage, maxResults, threads);

        Collections.sort(threads);
        return subList(threads, startIndex, maxResults);
    }

    public ActivityThread loadActivityThreadByParentGuid(String guid) {
        Map<String, Object> parameters = newHashMap();
        parameters.put("guid", guid);
        parameters.put("recipientDepth", NetworkTypeEnum.WORLD);
        List list = template.selectList("Activity.loadMessagesByParent", parameters);
        if (list.isEmpty()) {
            list = template.selectList("Activity.loadMediasByParent", parameters);
        }


        ActivityThread at = null;
        if (!list.isEmpty()) {
            at = new ActivityThread((IActivity) list.get(0));
            loadComments(at);
        }

        return at;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(Activity activity) {
        PersistenceUtil.insertOrUpdate(activity, template, "Activity.insert", "Activity.update");
        compassTemplate.save(activity);
    }

    @Override
    public Collection<IActivity> loadChildren(String parentGuid) {
        return template.selectList("Activity.loadChildActivities", parentGuid);
    }

    @Override
    public void reindex() {
        List<Activity> list = template.selectList("Activity.loadAll");
        for (Activity activity : list) {
            logger.info("Reindexing Activity : " + activity.getGuid());
            compassTemplate.save(activity);
        }
    }

}


// hsql below....


//    @Language("SQL")
//    final private static String LOAD_CHILD_MESSAGE_SQL = "select " +
//            " m.guid as guid, m.postDate as postDate, m.recipientDepth as depth, " +
//            " m.body as body, m.parentguid as parentGuid, mm.ownerguid as ownerGuid, " +
//            " mm.ownerownertype as ownerType " +
//            " from " +
//            " bb_message m, bb_message_meta_data mm " +
//            " where " +
//            " mm.guid = m.artifactmetadata_guid " +
//            " and m.parentguid = :parentActivityGuid" +
//            " order " +
//            " by m.created asc";


//    @Language("SQL")
//    final private static String LOAD_CHILD_MEDIA_META_DATA_SQL = "select " +
//            " mm.guid as guid, mm.postDate as postDate, mm.recipientDepth as depth, mm.comment as comment, " +
//            " mm.location as location, mm.mimeType as mimeType, mm.thumbnaillocation as thumbnailLocation, " +
//            " mm.ownerguid as ownerGuid, mm.ownerownertype as ownerType, " +
//            " mm.parentguid as parentGuid " +
//            " from " +
//            " bb_media_meta_data mm " +
//            " where " +
//            " mm.parentguid = :parentActivityGuid " +
//            " order by mm.c" +
//            "reated asc";
//
//    @Language("HQL")
//    private static final String FOLLOWING_MESSAGE_QUERY = "select " +
//            " new com.blackbox.foundation.message.Message(m.guid, m.postDate, m.recipientDepth, m.body, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, m.virtualGift ) " +
//            " from Message m, MessageMetaData mm " +
//            " where " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where r.weight = :followWeight and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid " +
//            " ) " +
//            " and m.artifactMetaData.guid = mm.guid " +
//            " and m.recipientDepth in (:allMembers,:world) " +
//            " and m.parentActivity.guid is null " +
//            " and m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " order by m.created desc";
//
//    @Language("HQL")
//    private static final String FOLLOWING_MEDIA_QUERY = "select " +
//            " new com.blackbox.foundation.media.MediaMetaData(mm.guid, mm.postDate, mm.recipientDepth, mm.comment, mm.location," +
//            " mm.mimeType, mm.thumbnailLocation, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, mm.virtualGift ) " +
//            " from MediaMetaData mm " +
//            " where " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where r.weight = :followWeight and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid " +
//            " ) " +
//            " and mm.recipientDepth in (:allMembers,:world) " +
//            " and mm.parentActivity.guid is null " +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.acknowledged = true " +
//            " and mm.virtualGift = false " +
//            " order by mm.created desc";
//
//    @Language("HQL")
//    private static final String FRIEND_MESSAGE_QUERY = "select " +
//            " new com.blackbox.foundation.message.Message(m.guid, m.postDate, m.recipientDepth, m.body, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, m.virtualGift ) " +
//            " from Message m, MessageMetaData mm " +
//            " where " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where (r.weight = :friendWeight or r.weight = :inRelationshipWeight) and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid " +
//            " ) " +
//            " and m.artifactMetaData.guid = mm.guid " +
//            " and m.recipientDepth = :friend " +
//            " and m.parentActivity.guid is null " +
//            " and m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " order by m.created desc";
//
//    @Language("HQL")
//    private static final String FRIEND_MEDIA_QUERY = "select " +
//            " new com.blackbox.foundation.media.MediaMetaData(mm.guid, mm.postDate, mm.recipientDepth, mm.comment, mm.location," +
//            " mm.mimeType, mm.thumbnailLocation, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, mm.virtualGift ) " +
//            " from MediaMetaData mm " +
//            " where " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where (r.weight = :friendWeight or r.weight = :inRelationshipWeight) and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid " +
//            " ) " +
//            " and " +
//            " mm.recipientDepth = :friend and " +
//            " mm.parentActivity.guid is null " +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.acknowledged = true " +
//            " and mm.virtualGift = false" +
//            " order by mm.created desc";
//
//    @Language("HQL")
//    private static final String EVERYTHING_MESSAGE_QUERY = "select " +
//            " new com.blackbox.foundation.message.Message(m.guid, m.postDate, m.recipientDepth, m.body, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, m.virtualGift ) " +
//            " from Message m, MessageMetaData mm " +
//            " where " +
//            " ( " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where (r.weight = :friendWeight or r.weight = :inRelationshipWeight) and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid " +
//            " ) " +
//            " and m.artifactMetaData.guid = mm.guid " +
//            " and m.recipientDepth = :friend " +
//            " and m.parentActivity.guid is null " +
//            " and m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " ) " +
//            " or " +
//            " ( " +
//            " mm.artifactOwner.guid in (select u.guid from Relationship r, User u where r.weight = :followWeight and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " and m.artifactMetaData.guid = mm.guid " +
//            " and m.recipientDepth = :friend " +
//            " and m.parentActivity.guid is null " +
//            " and m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " ) " +
//            " or " +
//            " ( " +
//            " m.artifactMetaData.guid = mm.guid " +
//            " and m.recipientDepth in (:allMembers,:world) " +
//            " and m.parentActivity.guid is null " +
//            " and m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " ) " +
//            " order by m.created desc";
//
//    @Language("HQL")
//    private static final String EVERYTHING_MEDIA_QUERY = "select " +
//            " new com.blackbox.foundation.media.MediaMetaData(mm.guid, mm.postDate, mm.recipientDepth, mm.comment, mm.location," +
//            " mm.mimeType, mm.thumbnailLocation, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, mm.virtualGift ) " +
//            " from MediaMetaData mm " +
//            "where " +
//            "( " +
//            " ( mm.artifactOwner.guid in " +
//            "(select u.guid from Relationship r, User u where (r.weight = :friendWeight or r.weight = :inRelationshipWeight) and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " or " +
//            " mm.artifactOwner.guid = :fromGuid ) " +
//            " and mm.recipientDepth = :friend " +
//            " and mm.parentActivity.guid is null " +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.acknowledged = true " +
//            " and mm.virtualGift = false " +
//            ")" +
//            " or " +
//            " (mm.artifactOwner.guid in " +
//            "  (select u.guid from Relationship r, User u where r.weight = :followWeight and r.fromEntity.guid = :fromGuid and r.toEntity.guid = u.guid) " +
//            " and mm.recipientDepth in (:allMembers,:world)" +
//            " and mm.parentActivity.guid is null" +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.acknowledged = true " +
//            " and mm.virtualGift = false " +
//            ") " +
//            " or " +
//            "(" +
//            " mm.recipientDepth in (:allMembers,:world) " +
//            " and mm.parentActivity.guid is null " +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.acknowledged = true " +
//            " and mm.virtualGift = false " +
//            " ) " +
//            " order by mm.created desc";
//
//    @Language("HQL")
//    private static final String PUBLIC_MESSAGE_QUERY = "select " +
//            " new com.blackbox.foundation.message.Message(m.guid, m.postDate, m.recipientDepth, m.body, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, m.virtualGift ) " +
//            " from Message m, MessageMetaData mm " +
//            " where " +
//            " m.acknowledged = true " +
//            " and m.virtualGift = false " +
//            " and m.parentActivity is null " +
//            " and m.recipientDepth  = ? " +
//            " order by m.postDate desc";
//
//    @Language("HQL")
//    private static final String PUBLIC_MEDIA_QUERY = "select " +
//            " new com.blackbox.foundation.media.MediaMetaData(mm.guid, mm.postDate, mm.recipientDepth, mm.comment, mm.location," +
//            " mm.mimeType, mm.thumbnailLocation, " +
//            " mm.artifactOwner.guid, mm.artifactOwner.ownerType, mm.virtualGift ) " +
//            " from MediaMetaData mm " +
//            "where " +
//            " mm.acknowledged = true " +
//            " and mm.library = false " +
//            " and mm.loosePic = false " +
//            " and mm.virtualGift = false " +
//            " and mm.parentActivity is null " +
//            " and mm.recipientDepth = ?" +
//            " order by mm.postDate desc";