package com.blackbox.foundation.occasion;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OccasionRequest implements Serializable {

    protected Status status = Status.ENABLED;
    protected List<User.UserType> ownerTypes;
    protected List<OccasionType> occasionTypes;
    protected Bounds bounds;
    protected String url;
    private String occasionGuid;
    private String attendeeUserGuid;
    private AttendingStatus attendingStatus;

    private static final long serialVersionUID = -6440929676490394735L;
    private String ownerGuid;

    public OccasionRequest() {
        ownerTypes = new ArrayList<User.UserType>();
        occasionTypes = new ArrayList<OccasionType>();
        bounds = new Bounds();
    }

    public String getOccasionGuid() {
        return occasionGuid;
    }

    public void setOccasionGuid(String occasionGuid) {
        this.occasionGuid = occasionGuid;
    }

    public String getAttendeeUserGuid() {
        return attendeeUserGuid;
    }

    public void setAttendeeUserGuid(String attendeeUserGuid) {
        this.attendeeUserGuid = attendeeUserGuid;
    }

    public AttendingStatus getAttendingStatus() {
        return attendingStatus;
    }

    public void setAttendingStatus(AttendingStatus attendingStatus) {
        this.attendingStatus = attendingStatus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<User.UserType> getOwnerTypes() {
        return ownerTypes;
    }

    public void setOwnerTypes(List<User.UserType> ownerTypes) {
        this.ownerTypes = ownerTypes;
    }

    public List<OccasionType> getOccasionTypes() {
        return occasionTypes;
    }

    public void setOccasionTypes(List<OccasionType> occasionTypes) {
        this.occasionTypes = occasionTypes;
    }

    /**
     * @return a non-null bounds object. Should bounds have not been set, this method will return Bounds.boundLess()
     */
    public Bounds getBounds() {
        return bounds == null ? Bounds.boundLess() : bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOwnerGuid(String ownerGuid) {
        this.ownerGuid = ownerGuid;
    }

    public String getOwnerGuid() {
        return ownerGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OccasionRequest that = (OccasionRequest) o;

        if (attendeeUserGuid != null ? !attendeeUserGuid.equals(that.attendeeUserGuid) : that.attendeeUserGuid != null)
            return false;
        if (attendingStatus != that.attendingStatus) return false;
        if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
        if (occasionGuid != null ? !occasionGuid.equals(that.occasionGuid) : that.occasionGuid != null) return false;
        if (occasionTypes != null ? !occasionTypes.equals(that.occasionTypes) : that.occasionTypes != null)
            return false;
        if (ownerTypes != null ? !ownerTypes.equals(that.ownerTypes) : that.ownerTypes != null) return false;
        if (status != that.status) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (ownerGuid != null ? !ownerGuid.equals(that.url) : that.ownerGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (ownerTypes != null ? ownerTypes.hashCode() : 0);
        result = 31 * result + (occasionTypes != null ? occasionTypes.hashCode() : 0);
        result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (occasionGuid != null ? occasionGuid.hashCode() : 0);
        result = 31 * result + (attendeeUserGuid != null ? attendeeUserGuid.hashCode() : 0);
        result = 31 * result + (attendingStatus != null ? attendingStatus.hashCode() : 0);
        result = 31 * result + (ownerGuid != null ? ownerGuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OccasionRequest{" +
                "status=" + status +
                ", ownerTypes=" + ownerTypes +
                ", occasionTypes=" + occasionTypes +
                ", bounds=" + bounds +
                ", url='" + url + '\'' +
                ", occasionGuid='" + occasionGuid + '\'' +
                ", attendeeUserGuid='" + attendeeUserGuid + '\'' +
                ", attendingStatus=" + attendingStatus +
                ", ownerGuid=" + ownerGuid +
                '}';
    }

}

