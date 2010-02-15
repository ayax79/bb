package com.blackbox.server.gifting.listener;

import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.ResultReference;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.gifting.event.CreateGiftEvent;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.activity.IActivity;
import com.blackbox.activity.IVirtualGiftable;
import com.blackbox.activity.IRecipient;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author A.J. Wright
 */
@ListenedEvents(CreateGiftEvent.class)
@AsyncListener
public class NewGiftEmailListener extends BaseBlackboxListener<CreateGiftEvent, Object> {

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Resource
    IUserManager userManager;

    @Override
    public void handle(CreateGiftEvent event, ResultReference<Object> objectResultReference) {
        IActivity activity = event.getType();

        if (activity instanceof IVirtualGiftable && ((IVirtualGiftable) activity).isVirtualGift()) {

            final User sender = userManager.loadUserByGuid(activity.getSender().getGuid());
            List<IRecipient> iRecipients = activity.getRecipients();
            assert iRecipients != null;
            IRecipient iRecipient = iRecipients.get(0);
            assert iRecipient != null;
            final User recipient = userManager.loadUserByGuid(iRecipient.getRecipient().getGuid());

            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(recipient.getName(), recipient.getEmail());
                    withSubject("SURPRISE! %s just sent you a gift on BBR.", sender.getUsername());
                    withHtmlTemplate("/velocity/newGift-html.vm");
                    withTextTemplate("/velocity/newGift-text.vm");
                    withTemplateVariable("gifting_username", sender.getUsername());
                    withTemplateVariable("name", recipient.getName());
                }
            });

        }

    }
}
