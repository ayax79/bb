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
public class SocialPermission extends BaseBBPermission {
    private static final long serialVersionUID = 4703851157748302803L;
    private double trustLevel;
    private int depth;
    private String relationshipType;

    public SocialPermission() {
        setType(PermissionTypeEnum.SOCIAL);
    }

    @Override
    protected String generateKey() {
        return MessageDigestUtils.sha1Hash(relationshipType + depth + trustLevel + getType().toString());
    }

    public double getTrustLevel() {
        return trustLevel;
    }

    public void setTrustLevel(double trustLevel) {
        this.trustLevel = trustLevel;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialPermission)) return false;
        if (!super.equals(o)) return false;

        SocialPermission that = (SocialPermission) o;

        if (depth != that.depth) return false;
        if (Double.compare(that.trustLevel, trustLevel) != 0) return false;
        //noinspection RedundantIfStatement
        if (relationshipType != null ? !relationshipType.equals(that.relationshipType) : that.relationshipType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = trustLevel != +0.0d ? Double.doubleToLongBits(trustLevel) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + depth;
        result = 31 * result + (relationshipType != null ? relationshipType.hashCode() : 0);
        return result;
    }

    public static SocialPermission cloneSocialPermission(SocialPermission sp) {
        SocialPermission sp2 = new SocialPermission();
        sp2.depth = sp.depth;
        sp2.relationshipType = sp.relationshipType;
        sp2.trustLevel = sp.trustLevel;
        sp2.type = sp.type;
        sp2.artifactType = sp.artifactType;
        sp2.key = sp.key;
        return sp2;
    }
}
