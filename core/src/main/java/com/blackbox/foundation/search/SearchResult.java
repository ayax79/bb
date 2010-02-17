package com.blackbox.foundation.search;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.bookmark.WishStatus;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.occasion.AttendingStatus;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.social.RelationshipNetwork;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlSeeAlso({MediaMetaData.class, Message.class, Occasion.class})
public class SearchResult<T extends BaseEntity> implements Serializable {
    private static final long serialVersionUID = -2057020255770739826L;

    private T entity;
    private IActivity latest;
    private RelationshipNetwork network;
    private Integer vouchCount;
    private WishStatus wishStatus;
    private Double distance = null;
    private AttendingStatus attendingStatus;

    public SearchResult() {
    }

    public SearchResult(T entity, IActivity latest, RelationshipNetwork network, Integer vouchCount, WishStatus wishStatus, Double distance) {
        this.entity = entity;
        this.latest = latest;
        this.network = network;
        this.vouchCount = vouchCount;
        this.wishStatus = wishStatus;
        this.distance = distance;
    }

    public AttendingStatus getAttendingStatus() {
        return attendingStatus;
    }

    public void setAttendingStatus(AttendingStatus attendingStatus) {
        this.attendingStatus = attendingStatus;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public IActivity getLatest() {
        return latest;
    }

    public void setLatest(IActivity latest) {
        this.latest = latest;
    }

    public RelationshipNetwork getNetwork() {
        return network;
    }

    public void setNetwork(RelationshipNetwork network) {
        this.network = network;
    }

    public Integer getVouchCount() {
        return vouchCount;
    }

    public void setVouchCount(Integer vouchCount) {
        this.vouchCount = vouchCount;
    }

    public WishStatus getWishStatus() {
        return wishStatus;
    }

    public void setWishStatus(WishStatus wishStatus) {
        this.wishStatus = wishStatus;
    }

    /**
     * @return distance from the zip in the search request, or null if not available
     */
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (vouchCount != that.vouchCount) return false;
        if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;
        if (latest != null ? !latest.equals(that.latest) : that.latest != null) return false;
        if (network != null ? !network.equals(that.network) : that.network != null) return false;
        if (wishStatus != that.wishStatus) return false;
        if (distance != null ? !distance.equals(that.distance) : that.distance != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (latest != null ? latest.hashCode() : 0);
        result = 31 * result + (network != null ? network.hashCode() : 0);
        result = 31 * result + (int) (vouchCount ^ (vouchCount >>> 32));
        result = 31 * result + (wishStatus != null ? wishStatus.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
