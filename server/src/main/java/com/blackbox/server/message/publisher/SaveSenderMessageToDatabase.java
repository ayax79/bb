/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.message.publisher;

import com.blackbox.message.Message;
import static com.blackbox.security.AccessControlList.cloneAccessControlList;
import com.blackbox.server.message.IMessageDao;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveSenderMessageToDatabase {
    final private static Logger logger = LoggerFactory.getLogger(SaveSenderMessageToDatabase.class);
    
    @Resource(name = "messageDao")
    private IMessageDao messageDao;

    public IMessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                Message message = camelMessage.getBody(Message.class);
                // This is simply a defensive measure to avoid a two session problem that seems to occur a lot
//                message.setAccessControlList(cloneAccessControlList(message.getAccessControlList()));

                messageDao.save(message);
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}
