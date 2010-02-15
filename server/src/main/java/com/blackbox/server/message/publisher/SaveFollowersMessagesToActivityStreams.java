/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.message.publisher;

import com.blackbox.message.Message;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.server.social.INetworkDao;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import static com.blackbox.server.activity.ActivityUtil.isParent;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.server.activity.BaseSaveToActivityStreams;
import static com.google.common.collect.Lists.newArrayList;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveFollowersMessagesToActivityStreams extends BaseSaveToActivityStreams {
    final private static Logger logger = LoggerFactory.getLogger(SaveFollowersMessagesToActivityStreams.class);

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                if (isParent(message)) {
                    if (checkNetworkDepth(message, NetworkTypeEnum.ALL_MEMBERS)||
                            checkNetworkDepth(message, NetworkTypeEnum.WORLD)) {
                        saveToFollowersOfMe(message);
                    } else if (checkNetworkDepth(message, NetworkTypeEnum.FRIENDS)) {
                        saveToMyFriendsAsFollowing(message);
                    }
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}