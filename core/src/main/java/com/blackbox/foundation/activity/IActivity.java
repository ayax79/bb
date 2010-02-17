/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.IDated;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.social.NetworkTypeEnum;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlSeeAlso({MediaMetaData.class, Message.class, Occasion.class, MessageRecipient.class, com.blackbox.foundation.media.MediaRecipient.class, Activity.class})
@XmlJavaTypeAdapter(com.blackbox.foundation.util.AnyTypeAdapter.class)
public interface IActivity extends Serializable, IDated {

    String getGuid();

    ActivityTypeEnum getActivityType();

    DateTime getPostDate();

    String getFormattedPostDate();

    void setPostDate(DateTime postDate);

    List<IRecipient> getRecipients();

    NetworkTypeEnum getRecipientDepth();

    EntityReference getSender();

    ActivityReference getParentActivity();

    ActivityProfile getSenderProfile();

    void setSenderProfile(ActivityProfile senderProfile);
}
