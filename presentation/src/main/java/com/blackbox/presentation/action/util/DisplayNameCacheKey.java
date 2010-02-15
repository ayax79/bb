package com.blackbox.presentation.action.util;

import java.io.Serializable;

/**
 * @author andy
 */
public class DisplayNameCacheKey implements Serializable {
    private String viewerGuid;
    private String targetGuid;

    public DisplayNameCacheKey() {
    }

    public DisplayNameCacheKey(String viewerGuid, String targetGuid) {
        this.viewerGuid = viewerGuid;
        this.targetGuid = targetGuid;
    }

    public String getViewerGuid() {
        return viewerGuid;
    }

    public void setViewerGuid(String viewerGuid) {
        this.viewerGuid = viewerGuid;
    }

    public String getTargetGuid() {
        return targetGuid;
    }

    public void setTargetGuid(String targetGuid) {
        this.targetGuid = targetGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayNameCacheKey that = (DisplayNameCacheKey) o;

        if (targetGuid != null ? !targetGuid.equals(that.targetGuid) : that.targetGuid != null) return false;
        if (viewerGuid != null ? !viewerGuid.equals(that.viewerGuid) : that.viewerGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = viewerGuid != null ? viewerGuid.hashCode() : 0;
        result = 31 * result + (targetGuid != null ? targetGuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DisplayNameCacheKey{" +
                "viewerGuid='" + viewerGuid + '\'' +
                ", targetGuid='" + targetGuid + '\'' +
                '}';
    }
}
