/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.server.occasion.listener;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.server.occasion.event.OccasionEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.yestech.event.ResultReference;
import org.yestech.notify.client.INotificationBridge;
import org.yestech.notify.constant.MessageTypeEnum;
import org.yestech.notify.objectmodel.ISender;
import org.yestech.notify.template.ITemplateLanguage;

/**
 *
 * @author Jack
 */
public abstract class OccasionAbstractEmailListener extends BaseBlackboxListener<OccasionEvent, Occasion> {
    protected String presentationUrl;

    @Required
    public void setPresentationUrl(String presentationUrl) {
        this.presentationUrl = presentationUrl;
    }

    public String getOccasionUrl(Occasion occasion) {
        return presentationUrl + "/event/show/" + occasion.getEventUrl();
    }

}
