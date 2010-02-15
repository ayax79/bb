/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.activity;

import com.blackbox.media.MediaMetaData;
import com.blackbox.activity.ActivityReference;
import com.blackbox.activity.ActivityTypeEnum;
import com.blackbox.message.Message;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.media.IMediaDao;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public abstract class BaseActivityPublisher {

    @Resource(name = "messageDao")
    private IMessageDao messageDao;

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;


    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }


}
