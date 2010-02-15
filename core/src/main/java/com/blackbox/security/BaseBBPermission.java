/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import com.blackbox.BBPersistentObject;
import org.apache.commons.lang.StringUtils;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public abstract class BaseBBPermission extends BBPersistentObject {
    protected PermissionTypeEnum type;
    protected String key;
    protected String artifactType;

    public BaseBBPermission() {
        super();
    }

    protected abstract String generateKey();

    public String getKey() {
        if (StringUtils.isEmpty(key)) {
            key = generateKey();
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
    }

    public PermissionTypeEnum getType() {
        return type;
    }

    public void setType(PermissionTypeEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseBBPermission)) return false;
        if (!super.equals(o)) return false;

        BaseBBPermission that = (BaseBBPermission) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        //noinspection RedundantIfStatement
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

}
