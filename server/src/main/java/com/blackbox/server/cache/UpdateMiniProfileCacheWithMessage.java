/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.cache;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.social.NetworkTypeEnum;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class UpdateMiniProfileCacheWithMessage extends BaseMiniProfileCache {
    final private static Logger logger = LoggerFactory.getLogger(UpdateMiniProfileCacheWithMessage.class);

    public void cache(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                if (!NetworkTypeEnum.SUPER_DIRECT.equals(message.getRecipientDepth())) {
                    updateMiniProfile(message);
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        }
    }
}