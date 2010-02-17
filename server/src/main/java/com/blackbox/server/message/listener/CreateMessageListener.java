/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.listener;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.ActivityProfile;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.entity.IEntityDao;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.event.CreateMessageEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * Creates a Lightweight message that can be published.  doesnt type to associate parents.
 */
@ListenedEvents(CreateMessageEvent.class)
public class CreateMessageListener extends BaseBlackboxListener<CreateMessageEvent, Message> {

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Resource(name = "entityDao")
    private IEntityDao entityDao;

    @Override
    public void handle(CreateMessageEvent event, ResultReference<Message> result) {
        Message message = event.getType();
        EntityReference owner = message.getArtifactMetaData().getArtifactOwner();
        BaseEntity baseEntity = entityDao.loadByEntityReference(owner);

        ActivityProfile profile = new ActivityProfile();
        profile.setSenderDisplayName(baseEntity.getName());
        MediaMetaData profileImage = mediaDao.loadProfileMediaMetaDataByOwner(owner);
        if (profileImage != null) {
            profile.setSenderProfileImage(profileImage.getLocation());
        }
        MediaMetaData avatarImage = mediaDao.loadAvatarMediaMetaDataByOwner(owner);
        if (avatarImage != null) {
            profile.setSenderAvatarImage(avatarImage.getLocation());
        }
        message.getArtifactMetaData().setSenderProfile(profile);
        result.setResult(message);
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    public void setEntityDao(IEntityDao entityDao) {
        this.entityDao = entityDao;
    }
}
