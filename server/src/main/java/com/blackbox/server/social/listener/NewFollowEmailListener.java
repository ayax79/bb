package com.blackbox.server.social.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.social.event.CreateRelationshipEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.IUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.text.MessageFormat;

@ListenedEvents(CreateRelationshipEvent.class)
@AsyncListener
public class NewFollowEmailListener extends BaseBlackboxListener<CreateRelationshipEvent, Object> {

    private static final Logger logger = LoggerFactory.getLogger(NewFollowEmailListener.class);

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Resource
    IUserManager userManager;


    @Override
    public void handle(CreateRelationshipEvent event, ResultReference<Object> objectResultReference) {
        Relationship r = event.getType();

        if (r.getWeight() == Relationship.RelationStatus.FOLLOW.getWeight()) {

            final User recipientUser = userManager.loadUserByGuid(r.getToEntity().getGuid());
            final User senderUser = userManager.loadUserByGuid(r.getFromEntity().getGuid());
            if (recipientUser == null || senderUser == null) {
                logger.warn(MessageFormat.format("Unable to send email when senderUser {0} or recipientUser {0} are unavailable", senderUser, recipientUser));
                return;
            }

            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(recipientUser.fullName(), recipientUser.getEmail());
                    withSubject("Boop! %s is following you around in the Republic!", senderUser.getUsername());
                    withTextTemplate("/velocity/newFollower-text.vm");
                    withHtmlTemplate("/velocity/newFollower-html.vm");
                    withTemplateVariable("recipient_name", recipientUser.getName());
                    withTemplateVariable("requesting_username", senderUser.getUsername());
                }
            });
        }
    }

}