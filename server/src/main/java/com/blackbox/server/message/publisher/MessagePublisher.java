/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import com.blackbox.server.activity.BaseActivityPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.ProducerArtifactType;
import org.yestech.publish.publisher.IPublisher;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ProducerArtifactType(type = {ArtifactType.TEXT})
public class MessagePublisher extends BaseActivityPublisher implements IPublisher<Message> {

    final private static Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    @Override
    public void publish(Message message) {
        updateRecipient(message);
//        applyPermissions(message);
    }

    private void updateRecipient(Message message) {
    }

    private void applyPermissions(Message message) {
        //need to get the permissions from the users profile...

//        final SocialPermission socialPermission = SecurityFactory.createPermission(PermissionTypeEnum.SOCIAL);
//        final NetworkTypeEnum networkDepth = message.getRecipientDepth();
//        socialPermission.setDepth(networkDepth.getDepth());
//        socialPermission.setRelationshipType(networkDepth.toString());
//
//        HashSet<SocialPermission> socialPermissions = new HashSet<SocialPermission>();
//        AccessControlList accessControlList = message.getAccessControlList();
//        PersistenceUtil.setDates(socialPermission);
//        socialPermissions.add(socialPermission);
//        accessControlList.setSocialPermissions(socialPermissions);
//        PersistenceUtil.setDates(accessControlList);
//        Utils.applyGuid(accessControlList);


//        message.getAccessControlList().getSocialPermissions().add(socialPermission);
    }
}
