/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.foundation.social.NetworkTypeEnum;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveSenderMessageToActivityStream {
    final private static Logger logger = LoggerFactory.getLogger(SaveSenderMessageToActivityStream.class);

    private IActivityStreamDao activityStreamDao;

    @Resource(name = "activityStreamDao")
    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                if (!NetworkTypeEnum.SUPER_DIRECT.equals(message.getRecipientDepth())) {
                    // This is simply a defensive measure to avoid a two session problem that seems to occur a lot
//                    message.setAccessControlList(cloneAccessControlList(message.getAccessControlList()));
                    activityStreamDao.saveSender(message, message.getSender());
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}