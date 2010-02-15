package com.blackbox.presentation.action.util;

import java.io.Serializable;

/**
 * @author A.J. Wright
 */
public class AvatarCacheKey implements Serializable {

    private String viewerGuid;
    private String userGuid;
    private static final long serialVersionUID = 8863717223527672937L;

    public AvatarCacheKey(String viewerGuid, String userGuid) {
        this.viewerGuid = viewerGuid;
        this.userGuid = userGuid;
    }

    public String getViewerGuid() {
        return viewerGuid;
    }

    public void setViewerGuid(String viewerGuid) {
        this.viewerGuid = viewerGuid;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvatarCacheKey that = (AvatarCacheKey) o;

        if (userGuid != null ? !userGuid.equals(that.userGuid) : that.userGuid != null) return false;
        //noinspection RedundantIfStatement
        if (viewerGuid != null ? !viewerGuid.equals(that.viewerGuid) : that.viewerGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = viewerGuid != null ? viewerGuid.hashCode() : 0;
        result = 31 * result + (userGuid != null ? userGuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AvatarCacheKey{" +
                "viewerGuid='" + viewerGuid + '\'' +
                ", userGuid='" + userGuid + '\'' +
                '}';
    }
}
