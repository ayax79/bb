package com.blackbox.foundation.activity;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import org.joda.time.DateTime;

/**
 * @author A.J. Wright
 */
public interface IRecipient {
    boolean isRead();

    void setRead(boolean read);

    DateTime getReplyDate();

    void setReplyDate(DateTime replyDate);

    public static enum RecipientStatus {
        // don't change the order
        INBOX,
        ARCHIVED,
        DELETED
    }

    EntityReference getRecipient();

    RecipientStatus getStatus();

    void setStatus(RecipientStatus status);

    BaseEntity getRecipientObject();

    void setRecipientObject(BaseEntity baseEntity);

}
