/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation;

import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.user.User;
import org.springframework.util.Assert;
import org.terracotta.modules.annotations.InstrumentedClass;
import org.yestech.publish.objectmodel.IArtifactOwner;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@InstrumentedClass
@XmlSeeAlso({User.class, Occasion.class, MediaLibrary.class, MediaMetaData.class, Message.class})
public abstract class BaseEntity extends BBPersistentObject implements IEntity, IArtifactOwner<EntityType, String>, IStatusable, Serializable {

    private EntityType ownerType;
    private String name;
    private Status status;

    protected BaseEntity(EntityType ownerType) {
        Assert.notNull(ownerType, "ownerType");
        this.ownerType = ownerType;
    }

    @Override
    public String getOwnerIdentifier() {
        return getGuid();
    }

    public void setOwnerIdentifier(String identifier) {
        setGuid(identifier);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public EntityType getOwnerType() {
        return ownerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityReference toEntityReference() {
        return new EntityReference(getOwnerType(), guid);
    }

    public EntityReference getEntityReference() {
        return toEntityReference();
    }

    public void setOwnerType(EntityType ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        if (!super.equals(o)) return false;

        BaseEntity that = (BaseEntity) o;
        return !(ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "name='" + name + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
