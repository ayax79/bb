/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.user;

import static com.blackbox.foundation.EntityType.USER;

import com.blackbox.foundation.*;
import com.blackbox.foundation.security.IAsset;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "user")
@EntityTypeAnnotation(USER)
@InstrumentedClass
public interface IUser extends Serializable, IEntity, IAsset, IDated {
    String getName();

    void setName(String realName);

    String getEmail();

    void setEmail(String email);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getGuid();

    void setGuid(String guid);

    void setStatus(Status status);

    Status getStatus();

    EntityReference toEntityReference();
}
