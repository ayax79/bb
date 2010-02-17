package com.blackbox.server.user.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.server.user.IUserDao;
import com.blackbox.server.user.event.ForgotPasswordEvent;
import com.blackbox.foundation.user.User;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(ForgotPasswordEvent.class)
@AsyncListener
public class SendResetPasswordEmailListener extends BaseBlackboxListener<ForgotPasswordEvent, Object> {

    @Resource(name = "userDao")
    private IUserDao userDao;

    @Resource
    private SimpleEmailDelivery emailDelivery;


    @Override
    public void handle(final ForgotPasswordEvent event, ResultReference<Object> reference) {

        final User user = event.getUser();
        emailDelivery.send(new EmailDefinition() {
            {
                withSubject("Your New BlackBox Republic Password");
                withTextTemplate("/velocity/forgotPassword-text.vm");
                withHtmlTemplate("/velocity/forgotPassword-html.vm");
                withRecipient(user.getName(), user.getEmail());
                withTemplateVariable("user_name", user.getName());
                withTemplateVariable("user_password", event.getTemporaryPassword());
            }
        });

    }
   
}