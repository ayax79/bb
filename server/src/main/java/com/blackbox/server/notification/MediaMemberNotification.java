/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.notification;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class MediaMemberNotification extends BaseBlackboxNotification {
    final private static Logger logger = LoggerFactory.getLogger(MediaMemberNotification.class);
    
    @Override
    public void notify(Exchange exchange) {
        try {
//            System.out.println("called...");
        } catch (Exception t) {
            exchange.setException(t);
            logger.error(t.getMessage(), t);
        }

        //To change body of implemented methods use File | Settings | File Templates.
//        final String guid = message.getArtifactMetaData().getArtifactOwner().getOwnerIdentifier().toString();
//        final RelationshipNetwork network = NetworkUtil.loadNetwork(networkDao, guid, message.getRecipientDepth());
//        final List<Relationship> relationshipList = network.getRelationships();
//        //execute db save
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<Message> clonedMessages = com.google.common.collect.Lists.newArrayList();
//                for (int i = 0, b = 1; i < relationshipList.size(); i++, b++) {
//                    Relationship relationship = relationshipList.get(i);
//                    clonedMessages.add(cloneMessage(message, relationship));
//                    if (b % batchSize == 0) {
//                        messageDao.save(clonedMessages);
//                        clonedMessages.clear();
//                    }
//                }
//                messageDao.save(clonedMessages);
//                clonedMessages.clear();
//            }
//        });
//        //execute queue save
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                for (Relationship relationship : relationshipList) {
//                    processMessage(cloneMessage(message, relationship));
//                }
//            }
//        });
    }
}
