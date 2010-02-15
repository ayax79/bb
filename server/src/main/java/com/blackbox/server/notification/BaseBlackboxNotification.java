/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.notification;

import org.yestech.notify.template.ITemplateLanguage;
import org.yestech.notify.objectmodel.ISender;
import org.yestech.notify.client.INotificationBridge;
import org.yestech.notify.constant.MessageTypeEnum;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.blackbox.user.IUser;
import com.blackbox.server.util.RegistrationUtil;
import com.blackbox.server.social.INetworkDao;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public abstract class BaseBlackboxNotification implements IBlackboxNotification {
    final private static Logger logger = LoggerFactory.getLogger(BaseBlackboxNotification.class);
    
    private static final String verificationTemplateKey = "registration_key";
    private static final String realnameTemplateKey = "registration_real_name";
    private static final String presentationUrlTemplateKey = "presentation_url";

    @Resource(name = "networkDao")
    private INetworkDao networkDao;

    private ITemplateLanguage templateLanguage;
    private ISender sender;
    private String subject;
    private INotificationBridge notificationBridge;
    private MessageTypeEnum messageType = MessageTypeEnum.MULTIPART_EMAIL;
    private String presentationUrl;


    public INetworkDao getNetworkDao() {
        return networkDao;
    }

    public void setNetworkDao(INetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    public INotificationBridge getNotificationBridge()
    {
        return notificationBridge;
    }

    public String getPresentationUrl() {
        return presentationUrl;
    }

    @Required
    public void setPresentationUrl(String presentationUrl) {
        this.presentationUrl = presentationUrl;
    }

    @Required
    public void setNotificationBridge(INotificationBridge notificationBridge)
    {
        this.notificationBridge = notificationBridge;
    }

    public MessageTypeEnum getMessageType()
    {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType)
    {
        this.messageType = messageType;
    }

    public ITemplateLanguage getTemplateLanguage()
    {
        return templateLanguage;
    }

    @Required
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

    protected HashMap<String, Serializable> initializeTemplateData(IUser user)
    {
        HashMap<String, Serializable> templateData = new HashMap<String, Serializable>();
        templateData.put(verificationTemplateKey, RegistrationUtil.generateValidationHash(user));
        templateData.put(realnameTemplateKey, user.getName());
        templateData.put(presentationUrlTemplateKey, presentationUrl);
        return templateData;
    }


}
