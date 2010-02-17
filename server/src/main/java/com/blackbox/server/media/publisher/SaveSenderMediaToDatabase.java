/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.media.publisher;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.IMediaDao;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.IFileArtifact;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveSenderMediaToDatabase {
    final private static Logger logger = LoggerFactory.getLogger(SaveSenderMediaToDatabase.class);
    private IMediaDao mediaDao;

    @Resource(name = "mediaDao")
    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                IFileArtifact artifact = camelMessage.getBody(IFileArtifact.class);
                MediaMetaData metaData = (MediaMetaData) artifact.getArtifactMetaData();
                if (logger.isDebugEnabled()) {
                    logger.debug("artifact metadata: " + artifact.getArtifactMetaData());
                }
                if (!metaData.isLibrary()) {
                    mediaDao.save(metaData);
                }

            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}
