package com.blackbox.occasion;

import com.blackbox.BBPersistentObject;
import static com.google.common.collect.Lists.newArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class Attendee extends BBPersistentObject implements Serializable {
    private static final long serialVersionUID = 4101122430631690844L;

    private String email;
    private String bbxUserGuid = "";    // only bbox user will have guid set
    private String bbxUserName = ""; //only bbox user will have name set
    //if it is a private event, email/password is needed to see it, if it is invite only event, email password is needed to reply
    private String password;

    private AttendingStatus attendingStatus = AttendingStatus.NOT_RESPONDED;
    private AttendeeSource attendeeSource;
    private List<Occasion> occasions = newArrayList();
    private boolean acknowledged;

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public AttendingStatus getAttendingStatus() {
        return attendingStatus;
    }

    public void setAttendingStatus(AttendingStatus attendingStatus) {
        this.attendingStatus = attendingStatus;
    }

    public AttendeeSource getAttendeeSource() {
        return attendeeSource;
    }

    public void setAttendeeSource(AttendeeSource attendeeSource) {
        this.attendeeSource = attendeeSource;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBbxUserName() {
        return bbxUserName;
    }

    public void setBbxUserName(String name) {
        this.bbxUserName = name;
    }

    public String getBbxUserGuid() {
        return bbxUserGuid;
    }

    public void setBbxUserGuid(String bbxUserGuid) {
        this.bbxUserGuid = bbxUserGuid;
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<Occasion> occasions) {
        this.occasions = occasions;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "email='" + email + '\'' +
                ", acknowledged='" + acknowledged + '\'' +
                ", bbxUserGuid='" + bbxUserGuid + '\'' +
                ", bbxUserName='" + bbxUserName + '\'' +
                ", password='" + password + '\'' +
                ", attendingStatus=" + attendingStatus +
                ", attendeeSource=" + attendeeSource +
//                ", occasions=" + occasions +
                '}';
    }
}
