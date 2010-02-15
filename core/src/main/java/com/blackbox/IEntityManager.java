/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox;

import org.springframework.transaction.annotation.Transactional;

/**
 * Manager that will allow you to get an {@link com.blackbox.IEntity} implementation of
 * any entity. This is useful for doing things like creating relationships where you main not know the implementation kind.
 * Note that the objects you get back can not be cast into their full implementation.
 *
 * @author A.J. Wright
 */
public interface IEntityManager {

    @Transactional(readOnly = true)
    <T extends BBPersistentObject> T loadEntity(String entityGuid, EntityType entityType);

    <T extends BBPersistentObject> T loadEntity(EntityReference ef);
}
