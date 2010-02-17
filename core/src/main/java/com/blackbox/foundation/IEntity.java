/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation;

import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IEntity extends Serializable {
    String getGuid();

    EntityType getOwnerType();

    EntityReference toEntityReference();

}
