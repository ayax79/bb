/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.entity;

import com.blackbox.EntityType;
import com.blackbox.IEntityManager;
import com.blackbox.BBPersistentObject;
import com.blackbox.EntityReference;
import com.blackbox.occasion.IOccasionManager;
import com.blackbox.message.IMessageManager;
import com.blackbox.media.IMediaManager;
import com.blackbox.user.IUserManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@Service("entityManager")
public class EntityManager implements IEntityManager
{

    @Resource
    IUserManager userManager;

    @Resource
    IMediaManager mediaManager;

    @Resource
    IMessageManager messageManager;

    @Resource
    IOccasionManager occasionManager;


    @Transactional(readOnly = true)
    public <T extends BBPersistentObject> T loadEntity(String entityGuid, EntityType entityType)
    {

        if (entityType == EntityType.USER) {
            return (T) userManager.loadUserByGuid(entityGuid);
        }
        else if (entityType == EntityType.MESSAGE) {
            return (T) messageManager.loadMessageByGuid(entityGuid);
        }
        else if (entityType == EntityType.MEDIA) {
            return (T) mediaManager.loadMediaMetaDataByGuid(entityGuid);
        }
        else if (entityType == EntityType.OCCASION) {
            return (T) occasionManager.loadByGuid(entityGuid);
        }
        throw new UnsupportedOperationException("Unhandled media type" + entityType);

    }

    public <T extends BBPersistentObject> T loadEntity(EntityReference ef) {
        return (T) loadEntity(ef.getGuid(), ef.getOwnerType());
    }

}
