package com.blackbox.server.activity;

import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.social.Relationship;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.server.social.INetworkDao;
import static com.blackbox.server.activity.ActivityUtil.cloneActivity;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaRecipient;
import com.blackbox.activity.IRecipient;
import static com.google.common.collect.Lists.newArrayList;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

/**
 *
 *
 */
public abstract class BaseSaveToActivityStreams {
    private int batchSize = 200;

    @Resource(name = "networkDao")
    private INetworkDao networkDao;

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    public INetworkDao getNetworkDao() {
        return networkDao;
    }

    public void setNetworkDao(INetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    protected void saveToFollowersOfMe(MediaMetaData metaData) {
        final String guid = metaData.getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByToEnityAndWeights(guid, Relationship.RelationStatus.FOLLOW.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            MediaMetaData clonedMetaData = cloneActivity(metaData, relationship);
            ArrayList<IRecipient> iRecipients = newArrayList();
            iRecipients.add(new MediaRecipient(relationship.getFromEntity(), clonedMetaData));
            clonedMetaData.setRecipients(iRecipients);
            activityStreamDao.saveRecipient(clonedMetaData, NetworkTypeEnum.FOLLOWERS);
        }
    }

    protected void saveToFollowersOfMe(Message message) {
        final String guid = message.getArtifactMetaData().getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByToEnityAndWeights(guid, Relationship.RelationStatus.FOLLOW.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            Message clonedMessage = cloneActivity(message, relationship);
            ArrayList<IRecipient> iRecipients = newArrayList();
            iRecipients.add(new MessageRecipient(relationship.getFromEntity()));
            clonedMessage.setRecipients(iRecipients);
            activityStreamDao.saveRecipient(clonedMessage, NetworkTypeEnum.FOLLOWERS);
        }
    }

    protected void saveToMyFriendsAsFollowing(MediaMetaData metaData) {
        final String guid = metaData.getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByFromEnityAndWeights(guid, Relationship.RelationStatus.FRIEND.getWeight(), Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            MediaMetaData clonedMetaData = cloneActivity(metaData, relationship);
            activityStreamDao.saveRecipient(clonedMetaData, NetworkTypeEnum.FOLLOWERS);
        }
    }
    
    protected void saveToMyFriendsAsFollowing(Message message) {
        final String guid = message.getArtifactMetaData().getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByFromEnityAndWeights(guid, Relationship.RelationStatus.FRIEND.getWeight(), Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            Message clonedMessage = cloneActivity(message, relationship);
            activityStreamDao.saveRecipient(clonedMessage, NetworkTypeEnum.FOLLOWERS);
        }
    }

    protected void saveToMyFriends(Message message) {
        final String guid = message.getArtifactMetaData().getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByFromEnityAndWeights(guid, Relationship.RelationStatus.FRIEND.getWeight(), Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            Message clonedMessage = ActivityUtil.cloneActivity(message, relationship);
            activityStreamDao.saveRecipient(clonedMessage, NetworkTypeEnum.FRIENDS);
        }
    }

    protected void saveToMyFriends(MediaMetaData metaData) {
        final String guid = metaData.getArtifactOwner().getOwnerIdentifier();
        final List<Relationship> relationshipList = networkDao.loadRelationshipsByFromEnityAndWeights(guid, Relationship.RelationStatus.FRIEND.getWeight(), Relationship.RelationStatus.IN_RELATIONSHIP.getWeight());
        for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
            Relationship relationship = relationshipList.get(i);
            MediaMetaData clonedMetaData = cloneActivity(metaData, relationship);
            activityStreamDao.saveRecipient(clonedMetaData, NetworkTypeEnum.FRIENDS);
        }
    }
}
