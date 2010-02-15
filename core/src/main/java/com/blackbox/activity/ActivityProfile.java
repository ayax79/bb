/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class ActivityProfile implements Serializable {
    private String senderDisplayName;
    private String senderProfileImage;
    private String senderAvatarImage;
    private static final long serialVersionUID = 5651372629462533596L;

    public ActivityProfile() {
    }

    public ActivityProfile(String senderDisplayName, String senderProfileImage) {
        this.senderDisplayName = senderDisplayName;
        this.senderProfileImage = senderProfileImage;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public void setSenderProfileImage(String senderProfileImage) {
        this.senderProfileImage = senderProfileImage;
    }

    public String getSenderAvatarImage() {
        return senderAvatarImage;
    }

    public void setSenderAvatarImage(String senderAvatarImage) {
        this.senderAvatarImage = senderAvatarImage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityProfile that = (ActivityProfile) o;

        if (senderDisplayName != null ? !senderDisplayName.equals(that.senderDisplayName) : that.senderDisplayName != null)
            return false;
        //noinspection RedundantIfStatement
        if (senderProfileImage != null ? !senderProfileImage.equals(that.senderProfileImage) : that.senderProfileImage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = senderDisplayName != null ? senderDisplayName.hashCode() : 0;
        result = 31 * result + (senderProfileImage != null ? senderProfileImage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityProfile{" +
                "senderDisplayName='" + senderDisplayName + '\'' +
                ", senderProfileImage='" + senderProfileImage + '\'' +
                '}';
    }
}
