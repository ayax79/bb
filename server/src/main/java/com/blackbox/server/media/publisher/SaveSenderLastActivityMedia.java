/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.media.publisher;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.activity.IActivityStreamDao;
import static com.blackbox.server.util.MediaPublishUtil.isStreamMedia;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.IFileArtifact;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveSenderLastActivityMedia {
    final private static Logger logger = LoggerFactory.getLogger(SaveSenderLastActivityMedia.class);
    private IActivityStreamDao activityStreamDao;

    @Resource(name = "activityStreamDao")
    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
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
                if (isStreamMedia(metaData)) {
                    activityStreamDao.setLatestActivity(metaData);
                }

            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }
    }
}