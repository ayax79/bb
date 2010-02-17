/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.media.publisher;

import com.blackbox.foundation.media.MediaMetaData;
import static com.blackbox.server.activity.ActivityUtil.*;
import static com.blackbox.server.activity.ActivityUtil.checkNetworkDepth;
import com.blackbox.server.activity.BaseSaveToActivityStreams;
import static com.blackbox.server.util.MediaPublishUtil.isStreamMedia;
import com.blackbox.foundation.social.NetworkTypeEnum;
import static com.google.common.collect.Lists.newArrayList;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.IFileArtifact;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveFollowersMediasToActivityStreams extends BaseSaveToActivityStreams {
    final private static Logger logger = LoggerFactory.getLogger(SaveFollowersMediasToActivityStreams.class);
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
                    if (isParent(metaData)) {
                        if (checkNetworkDepth(metaData, NetworkTypeEnum.WORLD) ||
                        checkNetworkDepth(metaData, NetworkTypeEnum.ALL_MEMBERS)) {
                            saveToFollowersOfMe(metaData);
                        } else if (checkNetworkDepth(metaData, NetworkTypeEnum.FRIENDS)) {
                            saveToMyFriendsAsFollowing(metaData);
                        }
                    }
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }

        }

    }
}