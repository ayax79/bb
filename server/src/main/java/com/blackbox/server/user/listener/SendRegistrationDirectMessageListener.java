package com.blackbox.server.user.listener;

import com.blackbox.EntityReference;
import com.blackbox.message.IMessageManager;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.server.util.VelocityUtil;
import com.blackbox.user.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;

import static com.blackbox.EntityType.USER;
import static com.blackbox.social.NetworkTypeEnum.SUPER_DIRECT;

@AsyncListener
@ListenedEvents(RegisterUserEvent.class)
public class SendRegistrationDirectMessageListener extends BaseBlackboxListener<RegisterUserEvent, Object> {

    private static final Logger logger = LoggerFactory.getLogger(SendRegistrationDirectMessageListener.class);

    @Resource
    IMessageManager messageManager;


    public void handle(RegisterUserEvent registerUserEvent, ResultReference<Object> objectResultReference) {
        logger.info("Sending Registration direct message");

        IUser user = registerUserEvent.getType();

        Message message = Message.createMessage();
        message.setRecipientDepth(SUPER_DIRECT);
        message.setBody(buildBody(user));
        message.setSubject("You have joined the republic");
        message.getArtifactMetaData().setArtifactOwner(new EntityReference(USER, "1"));
        message.getRecipients().add(new MessageRecipient(user.toEntityReference(), message.getGuid()));

        messageManager.publish(message);
    }

    protected String buildBody(IUser user) {
        return VelocityUtil.transform("/velocity/welcome-dm.vm",
                initializeTemplateData(user));
    }

    protected HashMap<String, Serializable> initializeTemplateData(IUser user) {
        HashMap<String, Serializable> templateData = new HashMap<String, Serializable>();
        templateData.put("firstname", user.getName());
        templateData.put("username", user.getUsername());
        return templateData;
    }

}