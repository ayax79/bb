/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public abstract class Vendor extends BaseEntity implements IEntity {

    protected Vendor(EntityType ownerType) {
        super(ownerType);
    }
}
