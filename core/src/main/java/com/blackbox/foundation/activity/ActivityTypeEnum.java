/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.occasion.Occasion;

/**
 * Enum for the different possible types of Activity
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public enum ActivityTypeEnum {

    MESSAGE(Message.class),
    MEDIA(MediaMetaData.class),
    OCCASION(Occasion.class);

    private Class<? extends IActivity> activityClass;

    ActivityTypeEnum(Class<? extends IActivity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends IActivity> getActivityClass() {
        return activityClass;
    }

}
