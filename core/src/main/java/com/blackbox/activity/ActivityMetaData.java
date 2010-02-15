package com.blackbox.activity;

import java.io.Serializable;

/**
 *
 *
 */
public class ActivityMetaData implements Serializable {
    private int globalParentLastBucketIdentifier = -1;
    private static final long serialVersionUID = -4539059879430233786L;
    private IActivity lastestActivity;
    private int globalParentLastBucketSize = -1;
    private int globalChildLastBucketIdentifier = -1;
    private int globalChildLastBucketSize = -1;

    public int getGlobalParentLastBucketSize() {
        return globalParentLastBucketSize;
    }

    public void setGlobalParentLastBucketSize(int globalParentLastBucketSize) {
        this.globalParentLastBucketSize = globalParentLastBucketSize;
    }

    public void setGlobalParentLastBucketIdentifier(int globalParentLastBucketIdentifier) {
        this.globalParentLastBucketIdentifier = globalParentLastBucketIdentifier;
    }

    public int getGlobalParentLastBucketIdentifier() {
        return globalParentLastBucketIdentifier;
    }

    public IActivity getLastestActivity() {
        return lastestActivity;
    }

    public void setLastestActivity(IActivity lastestActivity) {
        this.lastestActivity = lastestActivity;
    }

    public int getGlobalChildLastBucketIdentifier() {
        return globalChildLastBucketIdentifier;
    }

    public void setGlobalChildLastBucketIdentifier(int globalChildLastBucketIdentifier) {
        this.globalChildLastBucketIdentifier = globalChildLastBucketIdentifier;
    }

    public int getGlobalChildLastBucketSize() {
        return globalChildLastBucketSize;
    }

    public void setGlobalChildLastBucketSize(int globalChildLastBucketSize) {
        this.globalChildLastBucketSize = globalChildLastBucketSize;
    }
}
