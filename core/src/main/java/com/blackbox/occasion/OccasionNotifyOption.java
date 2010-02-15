package com.blackbox.occasion;


import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 */
@XmlRootElement
public class OccasionNotifyOption implements Serializable {

    private boolean reminder_sendViaEmail;
    private boolean reminder_sendToBbxInbox;
    private boolean reminder_send7DaysBeforeWORsvp; // without rsvp
    private boolean reminder_send1DayBeforeWRsvp; // without rsvp
    private static final long serialVersionUID = -7487634353465902040L;

    public static enum RSVPNotifyOption {
        EVERY_RSVP,
        EVERY_DAY,
        NO_NOTIFICATION
    }

    private RSVPNotifyOption rsvpNotifyOption = RSVPNotifyOption.NO_NOTIFICATION;
    private int guest_bringFriendNbr;
    private int guest_maxNbr;
    private boolean guest_canForward;

    private boolean thank_notesSendViaEmail;
    private boolean thank_notesSendViaBbx;
    private String thank_customNotes;

    public int getGuestBringFriendNbr() {
        return guest_bringFriendNbr;
    }

    public void setGuestBringFriendNbr(int guest_bringFriendNbr) {
        this.guest_bringFriendNbr = guest_bringFriendNbr;
    }

    public boolean isGuestCanForward() {
        return guest_canForward;
    }

    public void setGuestCanForward(boolean guest_canForward) {
        this.guest_canForward = guest_canForward;
    }

    public int getGuestMaxNbr() {
        return guest_maxNbr;
    }

    public void setGuestMaxNbr(int guest_maxNbr) {
        this.guest_maxNbr = guest_maxNbr;
    }

    public boolean isReminderSend1DayBeforeWRsvp() {
        return reminder_send1DayBeforeWRsvp;
    }

    public void setReminderSend1DayBeforeWRsvp(boolean reminder_send1DayBeforeWRsvp) {
        this.reminder_send1DayBeforeWRsvp = reminder_send1DayBeforeWRsvp;
    }

    public boolean isReminderSend7DaysBeforeWORsvp() {
        return reminder_send7DaysBeforeWORsvp;
    }

    public void setReminderSend7DaysBeforeWORsvp(boolean reminder_send7DaysBeforeWORsvp) {
        this.reminder_send7DaysBeforeWORsvp = reminder_send7DaysBeforeWORsvp;
    }

    public boolean isReminderSendToBbxInbox() {
        return reminder_sendToBbxInbox;
    }

    public void setReminderSendToBbxInbox(boolean reminder_sendToBbxInbox) {
        this.reminder_sendToBbxInbox = reminder_sendToBbxInbox;
    }

    public boolean isReminderSendViaEmail() {
        return reminder_sendViaEmail;
    }

    public void setReminderSendViaEmail(boolean reminder_sendViaEmail) {
        this.reminder_sendViaEmail = reminder_sendViaEmail;
    }

    public boolean isThankNotesSendViaBbx() {
        return thank_notesSendViaBbx;
    }

    public void setThankNotesSendViaBbx(boolean thank_notesSendViaBbx) {
        this.thank_notesSendViaBbx = thank_notesSendViaBbx;
    }

    public String getThankCustomNotes() {
        return thank_customNotes;
    }

    public void setThankCustomNotes(String thank_customNotes) {
        this.thank_customNotes = thank_customNotes;
    }

    public boolean isThankNotesSendViaEmail() {
        return thank_notesSendViaEmail;
    }

    public void setThankNotesSendViaEmail(boolean thank_notesSendViaEmail) {
        this.thank_notesSendViaEmail = thank_notesSendViaEmail;
    }
}
