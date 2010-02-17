/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.occasion;

import com.blackbox.foundation.user.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.yestech.notify.client.INotificationBridge;
import org.yestech.notify.client.INotificationBuilder;
import org.yestech.notify.client.NotificationBuilder;
import org.yestech.notify.constant.MessageTypeEnum;
import org.yestech.notify.factory.ObjectFactory;
import org.yestech.notify.objectmodel.INotification;
import org.yestech.notify.objectmodel.IRecipient;
import org.yestech.notify.objectmodel.ISender;
import org.yestech.notify.template.ITemplateLanguage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SendOccasionInv 
{
    final private static Logger logger = LoggerFactory.getLogger(SendOccasionInv.class);

    private static final String event_url_key = "event_url";
    private static final String password_phrase_key = "password_phrase";
    private ITemplateLanguage templateLanguage;
    private ISender sender;
    private String subject;
    private INotificationBridge notificationBridge;
    private MessageTypeEnum messageType = MessageTypeEnum.MULTIPART_EMAIL;
    private String event_url;
    private String password_phrase;
    private List<String> emailList;

    public void setMessageType(MessageTypeEnum messageType)
    {
        this.messageType = messageType;
    }

    public INotificationBridge getNotificationBridge()
    {
        return notificationBridge;
    }

    @Required
    public void setNotificationBridge(INotificationBridge notificationBridge)
    {
        this.notificationBridge = notificationBridge;
    }

    public ITemplateLanguage getTemplateLanguage()
    {
        return templateLanguage;
    }

    public void setTemplateLanguage(ITemplateLanguage templateLanguage)
    {
        this.templateLanguage = templateLanguage;
    }

    public ISender getSender()
    {
        return sender;
    }

    @Required
    public void setSender(ISender sender)
    {
        this.sender = sender;
    }

    public String getSubject()
    {
        return subject;
    }

    @Required
    public void setSubject(String subject)
    {
        this.subject = subject;
    }


    public void handle()
    {

    	for (String email : emailList) {
            IRecipient recipient = ObjectFactory.createRecipient();
            recipient.setEmailAddress(email);
            recipient.setDisplayName(email);
            INotificationBuilder builder = NotificationBuilder.getBuilder(templateLanguage);
            builder.setMessageType(messageType).setSubject(subject).setSender(sender);
            builder.addRecipient(recipient);
            HashMap<String, Serializable> templateData = initializeTemplateData();
            builder.setTemplateData(templateData);
            INotification notification = builder.createNotification();
            if (logger.isInfoEnabled()) {
                logger.info(notification.toString());
            }
            notificationBridge.sendNotification(notification);
    	}
    }

    protected HashMap<String, Serializable> initializeTemplateData()
    {
        HashMap<String, Serializable> templateData = new HashMap<String, Serializable>();
        templateData.put(event_url_key, event_url);
        templateData.put(password_phrase_key, password_phrase);
        return templateData;
    }

	public void setEventUrl(String event_url) {
		this.event_url = event_url;
	}

	public void setPasswordPhrase(String password_phrase) {
		this.password_phrase = password_phrase;
	}

	public List<String> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}
}
