package com.blackbox.foundation.occasion;


import com.blackbox.foundation.BBPersistentObject;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@XmlRootElement
public class OccasionWebDetail extends BBPersistentObject implements Serializable {
    public static enum CanViewGuestList {
        VIEW_ALL,
        VIEW_YESONLY,
        PRIVATE
    }

    private CanViewGuestList canViewGuestList = CanViewGuestList.VIEW_ALL;
    private boolean canSharePhoto;

    private List<String> extraWebLinks = new ArrayList<String>();

    private boolean promoteOnTwitter;
    private String twitterMsg;
    private boolean promoteOnFacebook;
    private String facebookMsg;

    public CanViewGuestList getCanViewGuestList() {
        return canViewGuestList;
    }

    public void setCanViewGuestList(CanViewGuestList canViewGuestList) {
        this.canViewGuestList = canViewGuestList;
    }

    public List<String> getExtraWebLinks() {
        return extraWebLinks;
    }

    public void setExtraWebLinks(List<String> webLinks) {
        this.extraWebLinks = webLinks;
    }

    public boolean isPromoteOnTwitter() {
        return promoteOnTwitter;
    }

    public void setPromoteOnTwitter(boolean promoteOnTwitter) {
        this.promoteOnTwitter = promoteOnTwitter;
    }

    public String getTwitterMsg() {
        return twitterMsg;
    }

    public void setTwitterMsg(String tiwtterMsg) {
        this.twitterMsg = tiwtterMsg;
    }

    public boolean isPromoteOnFacebook() {
        return promoteOnFacebook;
    }

    public void setPromoteOnFacebook(boolean promoteOnFacebook) {
        this.promoteOnFacebook = promoteOnFacebook;
    }

    public String getFacebookMsg() {
        return facebookMsg;
    }

    public void setFacebookMsg(String facebookMsg) {
        this.facebookMsg = facebookMsg;
    }

    public boolean isCanSharePhoto() {
        return canSharePhoto;
    }

    public void setCanSharePhoto(boolean canSharePhoto) {
        this.canSharePhoto = canSharePhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OccasionWebDetail that = (OccasionWebDetail) o;

        if (canSharePhoto != that.canSharePhoto) return false;
        if (promoteOnFacebook != that.promoteOnFacebook) return false;
        if (promoteOnTwitter != that.promoteOnTwitter) return false;
        if (canViewGuestList != that.canViewGuestList) return false;
        if (facebookMsg != null ? !facebookMsg.equals(that.facebookMsg) : that.facebookMsg != null) return false;
        if (twitterMsg != null ? !twitterMsg.equals(that.twitterMsg) : that.twitterMsg != null) return false;
        //noinspection RedundantIfStatement
        if (extraWebLinks != null ? !extraWebLinks.equals(that.extraWebLinks) : that.extraWebLinks != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (canViewGuestList != null ? canViewGuestList.hashCode() : 0);
        result = 31 * result + (canSharePhoto ? 1 : 0);
        result = 31 * result + (extraWebLinks != null ? extraWebLinks.hashCode() : 0);
        result = 31 * result + (promoteOnTwitter ? 1 : 0);
        result = 31 * result + (twitterMsg != null ? twitterMsg.hashCode() : 0);
        result = 31 * result + (promoteOnFacebook ? 1 : 0);
        result = 31 * result + (facebookMsg != null ? facebookMsg.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OccasionWebDetail{" +
                "canViewGuestList=" + canViewGuestList +
                ", photoSharing=" + canSharePhoto +
                ", webLinks=" + extraWebLinks +
                ", promoteOnTwitter=" + promoteOnTwitter +
                ", twtterMsg='" + twitterMsg + '\'' +
                ", promoteOnFacebook=" + promoteOnFacebook +
                ", facebookMsg='" + facebookMsg + '\'' +
                '}';
    }
}
