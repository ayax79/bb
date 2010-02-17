/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import com.blackbox.foundation.BBPersistentObject;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "role")
@InstrumentedClass
public class Role extends BBPersistentObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        //noinspection RedundantIfStatement
        if (name != null ? !name.equals(role.name) : role.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public static Role cloneRole(Role r) {
        Role r2 = new Role();
        r2.setName(r.name);
        return r2;
    }
}
