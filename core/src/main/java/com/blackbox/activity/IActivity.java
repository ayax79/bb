/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import com.blackbox.EntityReference;
import com.blackbox.IDated;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaRecipient;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.occasion.Occasion;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.AnyTypeAdapter;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlSeeAlso({MediaMetaData.class, Message.class, Occasion.class, MessageRecipient.class, MediaRecipient.class, Activity.class})
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
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
