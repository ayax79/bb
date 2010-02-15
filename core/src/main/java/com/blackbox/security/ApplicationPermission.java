/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import org.terracotta.modules.annotations.InstrumentedClass;
import org.yestech.lib.crypto.MessageDigestUtils;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */

@InstrumentedClass
public class ApplicationPermission extends BaseBBPermission {
    private static final long serialVersionUID = 3244953919785964166L;
    private String category;
    private String access;
    private String field;

    public ApplicationPermission() {
        super();
        setType(PermissionTypeEnum.APPLICATION);
    }

    @Override
    protected String generateKey() {
        return MessageDigestUtils.sha1Hash(category + access + field + getType().toString());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationPermission)) return false;
        if (!super.equals(o)) return false;

        ApplicationPermission that = (ApplicationPermission) o;

        if (access != null ? !access.equals(that.access) : that.access != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        //noinspection RedundantIfStatement
        if (field != null ? !field.equals(that.field) : that.field != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (access != null ? access.hashCode() : 0);
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }

    public static ApplicationPermission cloneApplicationPermission(ApplicationPermission ap) {
        ApplicationPermission ap2 = new ApplicationPermission();
        ap2.access = ap.access;
        ap2.artifactType = ap.artifactType;
        ap2.category = ap.category;
        ap2.field = ap.field;
        ap2.key = ap.key;
        ap2.type = ap.type;
        return ap2;
    }
}