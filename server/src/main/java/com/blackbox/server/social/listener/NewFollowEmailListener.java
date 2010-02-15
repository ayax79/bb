package com.blackbox.server.social.listener;

import com.blackbox.server.message.event.PublishMessageEvent;
import com.blackbox.server.social.event.CreateRelationshipEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;
import com.blackbox.user.IUserManager;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

@ListenedEvents(CreateRelationshipEvent.class)
@AsyncListener
public class NewFollowEmailListener extends BaseBlackboxListener<CreateRelationshipEvent, Object> {

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
            assert senderUser != null;
            final String recipientName = recipientUser.getName();

            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(recipientUser.fullName(), recipientUser.getEmail());
                    withSubject("Boop! %s is following you around in the Republic!", senderUser.getUsername());
                    withTextTemplate("/velocity/newFollower-text.vm");
                    withHtmlTemplate("/velocity/newFollower-html.vm");
                    withTemplateVariable("recipient_name", recipientName);
                    withTemplateVariable("requesting_username", senderUser.getUsername());
                }
            });
        }
    }

}