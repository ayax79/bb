/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.notification;

import com.blackbox.message.Message;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.user.IUser;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.notify.client.INotificationBuilder;
import org.yestech.notify.client.NotificationBuilder;
import org.yestech.notify.factory.ObjectFactory;
import org.yestech.notify.objectmodel.INotification;
import org.yestech.notify.objectmodel.IRecipient;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class MessageMemberNotification extends BaseBlackboxNotification {
    final private static Logger logger = LoggerFactory.getLogger(MessageMemberNotification.class);

    public void notify(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                if (checkNetworkDepth(message, NetworkTypeEnum.SELF)) {
                    sendSelf(message);
                } else if (checkNetworkDepth(message, NetworkTypeEnum.DIRECT)) {
                    sendDirect(message);
                } else if (checkNetworkDepth(message, NetworkTypeEnum.ALL_MEMBERS)) {
                    sendAllMembers(message);
                } else if (checkNetworkDepth(message, NetworkTypeEnum.WORLD)) {
                    sendWorld(message);
                } else {
                    sendNetworked(message);
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        }
    }

    private void notifyMember(Message message) {
        IRecipient recipient = ObjectFactory.createRecipient();
        IUser user = (IUser) message.getArtifactMetaData().getRecipients();
        recipient.setEmailAddress(user.getEmail());
        recipient.setDisplayName(user.getName());
        INotificationBuilder builder = NotificationBuilder.getBuilder(getTemplateLanguage());
        builder.setMessageType(getMessageType()).setSubject(getSubject()).setSender(getSender());
        builder.addRecipient(recipient);
        HashMap<String, Serializable> templateData = initializeTemplateData(user);
        builder.setTemplateData(templateData);
        INotification notification = builder.createNotification();
        if (logger.isInfoEnabled()) {
            logger.info(notification.toString());
        }
        getNotificationBridge().sendNotification(notification);
    }

    private void sendNetworked(Message message) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void sendWorld(Message message) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void sendAllMembers(Message message) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void sendDirect(Message message) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void sendSelf(Message message) {
        //To change body of created methods use File | Settings | File Templates.
    }

//    private void processMessage(Message message) {
//        if (MessageUtil.isNotify(userMessageCache, message)) {
//            notifyMember(message);
//        } else {
//            MessageUtil.enQueueMessage(userMessageCache, message);
//        }
//    }


}
