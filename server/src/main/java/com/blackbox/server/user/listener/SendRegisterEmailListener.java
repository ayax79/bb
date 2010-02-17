/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.user.listener;

import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.foundation.user.IUser;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;

import javax.annotation.Resource;

/**
 * @author $Author: $
 * @version $Revision: $
 */
@ListenedEvents(RegisterUserEvent.class)
@AsyncListener
public class SendRegisterEmailListener extends BaseBlackboxListener<RegisterUserEvent, Object>
{
    @Resource(name = "remoteDelivery")
    SimpleEmailDelivery emailDelivery;

    @Override
    public void handle(RegisterUserEvent event, ResultReference<Object> result)
    {
        final IUser user = event.getType();
        emailDelivery.send(new EmailDefinition() {
            {
                withSubject("You have joined the republic");
                withRecipient(user.getName(), user.getEmail());
                withHtmlTemplate("/velocity/welcomeEmail-html.vm");
                withTextTemplate("/velocity/welcomeEmail-text.vm");
                withTemplateVariable("firstname", user.getName());
                withTemplateVariable("username", user.getUsername());

            }
        });
    }

}
