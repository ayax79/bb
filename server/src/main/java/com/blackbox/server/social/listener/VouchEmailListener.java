package com.blackbox.server.social.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.social.event.VouchEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.social.Vouch;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author A.J. Wright
 */
@ListenedEvents(VouchEvent.class)
@AsyncListener
public class VouchEmailListener extends BaseBlackboxListener<VouchEvent,Object> {

    @Resource
    SimpleEmailDelivery emailDelivery;

    @Resource
    IUserManager userManager;

    @Override
    public void handle(VouchEvent vouchEvent, ResultReference<Object> objectResultReference) {
        Vouch vouch = vouchEvent.getType();
        final User recipient = userManager.loadUserByGuid(vouch.getTarget().getGuid());
        final User voucher = userManager.loadUserByGuid(vouch.getVoucher().getGuid());

        emailDelivery.send(new EmailDefinition() {
            {
                withRecipient(recipient.fullName(), recipient.getEmail());
                withSubject("%s vouched you! For realz?!", voucher.getUsername());
                withHtmlTemplate("/velocity/vouchEmail-html.vm");
                withTextTemplate("/velocity/vouchEmail-text.vm");
                withTemplateVariable("voucher_username", voucher.getUsername());
                withTemplateVariable("name", recipient.getName());
            }
        });
    }
}
