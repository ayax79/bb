package com.blackbox.foundation;

import org.compass.annotations.SearchableProperty;
import org.yestech.publish.objectmodel.IArtifactOwner;
import org.compass.annotations.Searchable;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A lightweight reference to an {@link IEntity} object. This class stores the two minimal things it takes
 * to acquire any given {@link IEntity} objects; its guid and its entity type.
 * <p/>
 * {@link BaseEntity} provides a helper method {@link BaseEntity#toEntityReference()} to quickly create
 * EntityRefernce objects.
 */
@XmlRootElement
@Searchable(root = false)
public class EntityReference implements Serializable, IEntity, IArtifactOwner<EntityType, String> {

    @SearchableProperty
    private EntityType ownerType;
    @SearchableProperty
    private String guid;
    private static final long serialVersionUID = 5538082297970813783L;

    public EntityReference() {
    }

    public EntityReference(EntityType ownerType, String guid) {
        this.ownerType = ownerType;
        this.guid = guid;
    }

    /**
     * An alias for {@link #getGuid()} that conforms to the {@link org.yestech.publish.objectmodel.IArtifactOwner} interface.
     *
     * @return Returns the value of {@link #getGuid()}
     */
    @Override
    public String getOwnerIdentifier() {
        return getGuid();
    }

    /**
     * An alias for {@link #setGuid(String)} that conforms to the {@link org.yestech.publish.objectmodel.IArtifactOwner} interface.
     *
     * @param ownerIdentifier The guid to set.
     */
    public void setOwnerIdentifier(String ownerIdentifier) {
        setGuid(ownerIdentifier);
    }

    /**
     * The type of Entity this reference is for. See {@link EntityType} for possible types.
     *
     * @return type of Entity this reference is for.
     */
    public EntityType getOwnerType() {
        return ownerType;
    }

    @Override
    public EntityReference toEntityReference() {
        return this;
    }

    /**
     * Sets type of Entity this reference is for.
     *
     * @param ownerType type of Entity this reference is for.
     */
    public void setOwnerType(EntityType ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * Returns the generated unique identifier for the entity.
     *
     * @return the generated unique identifier for the entity.
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the generated unique identifier for the entity.
     *
     * @param guid the generated unique identifier for the entity.
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public static EntityReference createEntityReference(IEntity entity) {
        return createEntityReference(entity.getGuid(), entity.getOwnerType());
    }

    public static EntityReference createEntityReference(String guid) {
        EntityReference owner = new EntityReference();
        owner.setOwnerIdentifier(guid);
        return owner;
    }

    public static EntityReference createEntityReference(String ownerGuid, EntityType ownerType) {
        EntityReference owner = new EntityReference();
        owner.setOwnerType(ownerType);
        owner.setOwnerIdentifier(ownerGuid);
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityReference)) return false;

        EntityReference that = (EntityReference) o;

        if (ownerType != that.ownerType) return false;
        return !(guid != null ? !guid.equals(that.guid) : that.guid != null);

    }

    @Override
    public int hashCode() {
        int result = ownerType != null ? ownerType.hashCode() : 0;
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EntityReference{" +
                "ownerType=" + ownerType +
                ", guid='" + guid + '\'' +
                '}';
    }

    public static EntityReference createEntityReference() {
        return new EntityReference();
    }
}
