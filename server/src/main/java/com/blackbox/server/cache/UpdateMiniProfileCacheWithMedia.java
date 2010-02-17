/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.cache;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.user.MiniProfile;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.IFileArtifact;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;

/**
 * Caches {@link com.blackbox.foundation.media.MediaMetaData} in the userMessageCache.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public class UpdateMiniProfileCacheWithMedia extends BaseMiniProfileCache {
    final private static Logger logger = LoggerFactory.getLogger(UpdateMiniProfileCacheWithMedia.class);

    public void cache(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                IFileArtifact artifact = camelMessage.getBody(IFileArtifact.class);
                MediaMetaData metaData = (MediaMetaData) artifact.getArtifactMetaData();
                if (!metaData.isLibrary() && !metaData.isLoosePic()) {
                    updateMiniProfile(metaData);
                }
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        }
    }

}