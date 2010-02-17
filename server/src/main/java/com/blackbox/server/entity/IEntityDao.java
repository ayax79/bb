/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.entity;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.EntityReference;

/**
 *
 *
 */
public interface IEntityDao
{
    <T extends BBPersistentObject> T loadByGuidAndEntityType(String entityGuid, EntityType type);

    <T extends BBPersistentObject> T loadByEntityReference(EntityReference ref);

    <T extends BBPersistentObject> T  loadSearchUserByGuid(String guid);
}
