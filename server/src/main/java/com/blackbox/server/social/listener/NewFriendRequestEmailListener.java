package com.blackbox.server.social.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.event.PublishMessageEvent;
import com.blackbox.server.social.event.CreateRelationshipEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

@ListenedEvents(CreateRelationshipEvent.class)
@AsyncListener
public class NewFriendRequestEmailListener extends BaseBlackboxListener<CreateRelationshipEvent, Object> {

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Resource
    IUserManager userManager;


    @Override
    public void handle(CreateRelationshipEvent event, ResultReference<Object> objectResultReference) {
        Relationship r = event.getType();

        if (r.getWeight() == Relationship.RelationStatus.FRIEND_PENDING.getWeight()) {

            final User recipientUser = userManager.loadUserByGuid(r.getToEntity().getGuid());
            final User senderUser = userManager.loadUserByGuid(r.getFromEntity().getGuid());
            assert senderUser != null;
            final String recipientName = recipientUser.getName();

            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(recipientUser.fullName(), recipientUser.getEmail());
                    withSubject("Hey there! %s wants to be your friend in the Republic!", senderUser.getUsername());
                    withTextTemplate("/velocity/newFriendRequest-text.vm");
                    withHtmlTemplate("/velocity/newFriendRequest-html.vm");
                    withTemplateVariable("recipient_name", recipientName);
                    withTemplateVariable("requesting_username", senderUser.getUsername());
                }
            });
        }
    }

}