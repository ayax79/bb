/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.media.publisher;

import com.blackbox.media.MediaMetaData;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import com.blackbox.server.activity.BaseActivityPublisher;
import com.blackbox.social.NetworkTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.IFileArtifact;
import org.yestech.publish.objectmodel.IFileArtifactMetaData;
import org.yestech.publish.objectmodel.ProducerArtifactType;
import org.yestech.publish.publisher.IPublisher;

import java.util.Map;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
@ProducerArtifactType(type = {ArtifactType.IMAGE, ArtifactType.AUDIO, ArtifactType.VIDEO})
public class MultimediaAdapterPublisher extends BaseActivityPublisher implements IPublisher<IFileArtifact> {
    final private static Logger logger = LoggerFactory.getLogger(MultimediaAdapterPublisher.class);

    private Map<ArtifactType, IPublisher> adaptees;

    public void setAdaptees(Map<ArtifactType, IPublisher> adaptees) {
        this.adaptees = adaptees;
    }

    public Map<ArtifactType, IPublisher> getAdaptees() {
        return adaptees;
    }

    @Override
    public void publish(IFileArtifact artifact) {
        final IFileArtifactMetaData metaData = artifact.getArtifactMetaData();

        IPublisher publisher = adaptees.get(metaData.getArtifactType());
        if (publisher == null) {
            throw new RuntimeException("non supported testArtifact type: " + metaData.getArtifactType());
        }
        publisher.publish(artifact);
        if (logger.isDebugEnabled()) {
            logger.debug("location: " + metaData.getLocation());
        }
        MediaMetaData contentMetaData = (MediaMetaData) metaData;

        //updated recipient
        updateRecipient(contentMetaData);
        //updated message with full parent message
//        loadParentActivity(contentMetaData);
//        applyPermissions(contentMetaData);
    }

    private void updateRecipient(MediaMetaData contentMetaData) {
        if (checkNetworkDepth(contentMetaData, NetworkTypeEnum.SELF) && contentMetaData.getRecipients() == null) {
//            contentMetaData.setRecipients(contentMetaData.getSender());
        }
    }

    private void applyPermissions(MediaMetaData metaData) {
        //need to get the permissions from the users profile...

//        HashSet<SocialPermission> socialPermissions = new HashSet<SocialPermission>();
//        HashSet<ApplicationPermission> applicationPermissions = new HashSet<ApplicationPermission>();
//
//        AccessControlList accessControlList = metaData.getAccessControlList();
//        SocialPermission defaultSocialPermission = SecurityFactory.createPermission(PermissionTypeEnum.SOCIAL);
//        PersistenceUtil.setDates(defaultSocialPermission);
//        defaultSocialPermission.setRelationshipType(NetworkTypeEnum.ALL_MEMBERS.name());
//        defaultSocialPermission.setDepth(NetworkTypeEnum.ALL_MEMBERS.getDepth());
//        socialPermissions.add(defaultSocialPermission);
//        accessControlList.setSocialPermissions(socialPermissions);
//
//        ApplicationPermission defaultApplicationPermission = SecurityFactory.createPermission(PermissionTypeEnum.APPLICATION);
//        PersistenceUtil.setDates(defaultApplicationPermission);
//        defaultApplicationPermission.setCategory(ApplicationPermissionCategoryEnum.PROFILE.name());
//        defaultApplicationPermission.setAccess(ApplicationPermissionAccessEnum.READ.name());
//        applicationPermissions.add(defaultApplicationPermission);
//        accessControlList.setApplicationPermissions(applicationPermissions);
//
//        PersistenceUtil.setDates(accessControlList);
//        Utils.applyGuid(accessControlList);
    }
}
