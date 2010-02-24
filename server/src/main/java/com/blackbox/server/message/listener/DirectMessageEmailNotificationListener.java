package com.blackbox.server.message.listener;

import com.blackbox.foundation.message.Message;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.event.PublishMessageEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@ListenedEvents(PublishMessageEvent.class)
@AsyncListener
public class DirectMessageEmailNotificationListener extends BaseBlackboxListener<PublishMessageEvent, Object> {

    private static final Logger logger = LoggerFactory.getLogger(DirectMessageEmailNotificationListener.class);

    @Resource
    IUserManager userManager;

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Override
    public void handle(PublishMessageEvent event, ResultReference<Object> objectResultReference) {
        Message message = event.getType();
        if (message.getRecipientDepth() == NetworkTypeEnum.SUPER_DIRECT) {
            sendEmail(message);
        }
    }

    void sendEmail(Message message) {
        List<com.blackbox.foundation.activity.IRecipient> recipients = message.getRecipients();
        assert recipients != null && !recipients.isEmpty();
        com.blackbox.foundation.activity.IRecipient msgRecipient = recipients.get(0);
        final User recipientUser = userManager.loadUserByGuid(msgRecipient.getRecipient().getGuid());
        final User senderUser = userManager.loadUserByGuid(message.getArtifactMetaData().getArtifactOwner().getGuid());

        if (recipientUser == null || senderUser == null) {
            logger.warn(MessageFormat.format("Unable to send email when senderUser {0} or recipientUser {0} are unavailable", senderUser, recipientUser));
            return;
        }

        final String recipientName = recipientUser.getName();

        emailDelivery.send(new EmailDefinition() {
            {
                withRecipient(recipientName, recipientUser.getEmail());
                withSubject("%s sent you a BBR message.", senderUser.getUsername());
                withTextTemplate("/velocity/directMessageEmailNotification-text.vm");
                withHtmlTemplate("/velocity/directMessageEmailNotification-html.vm");
                withTemplateVariable("recipient_name", recipientName);
                withTemplateVariable("sender_username", senderUser.getUsername());
            }
        });

    }

}
