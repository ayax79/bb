/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.social.NetworkTypeEnum;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import static com.blackbox.server.activity.ActivityUtil.isParent;
import com.blackbox.server.activity.BaseSaveToActivityStreams;
import static com.google.common.collect.Lists.newArrayList;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveFriendsMessagesToActivityStreams extends BaseSaveToActivityStreams {
    final private static Logger logger = LoggerFactory.getLogger(SaveFriendsMessagesToActivityStreams.class);

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                if (isParent(message)) {
                    if (checkNetworkDepth(message, NetworkTypeEnum.FRIENDS)) {
                        saveToMyFriends(message);
                    }
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }

}