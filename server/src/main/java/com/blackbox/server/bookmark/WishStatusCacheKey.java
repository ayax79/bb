package com.blackbox.server.bookmark;

import java.io.Serializable;

/**
 * @author A.J. Wright
 */
public class WishStatusCacheKey implements Serializable {

    private String fromEntity;
    private String toEntity;

    public WishStatusCacheKey() {
    }

    public WishStatusCacheKey(String fromEntity, String toEntity) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
    }

    public String getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
    }

    public String getToEntity() {
        return toEntity;
    }

    public void setToEntity(String toEntity) {
        this.toEntity = toEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishStatusCacheKey that = (WishStatusCacheKey) o;

        if (fromEntity != null ? !fromEntity.equals(that.fromEntity) : that.fromEntity != null) return false;
        //noinspection RedundantIfStatement
        if (toEntity != null ? !toEntity.equals(that.toEntity) : that.toEntity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromEntity != null ? fromEntity.hashCode() : 0;
        result = 31 * result + (toEntity != null ? toEntity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WishStatusCacheKey{" +
                "fromEntity='" + fromEntity + '\'' +
                ", toEntity='" + toEntity + '\'' +
                '}';
    }
}
