package com.blackbox.server.message.listener;

import com.blackbox.foundation.activity.IRecipient;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.testingutils.UserHelper;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * was attempting to recreate issue where we get Hey $recipient_name, in the email and cannot with test below...
 *
 * @author colin@blackboxrepublic.com
 */
public class DirectMessageEmailNotificationListenerIntegrationTest extends BaseIntegrationTest {

    @Resource
    DirectMessageEmailNotificationListener listener;

    @Resource
    IUserManager userManager;

    @Ignore(value = "once i remember that really good mock email project, i'll plug it in here...")
    @Test
    public void testTemplateSubstitutionDoesNotWorkOnNewUser() {
        Message message = new Message();
        IRecipient messageRecipient = new MessageRecipient(UserHelper.createNamedUser("colin", userManager).toEntityReference());
        message.getArtifactMetaData().setArtifactOwner(UserFixture.sam.toUser().toEntityReference());
        List<IRecipient> recipients = newArrayList(messageRecipient);
        message.setRecipients(recipients);
        // there's no real hook to just to the template substitution so we will just send the message
        listener.sendEmail(message);
    }

    @Ignore(value = "once i remember that really good mock email project, i'll plug it in here...")
    @Test
    public void testTemplateSubstitutionDoesNotWorkOnExistingUser() {
        Message message = new Message();
        IRecipient messageRecipient = new MessageRecipient(UserFixture.aj.toUser().toEntityReference());
        message.getArtifactMetaData().setArtifactOwner(UserFixture.sam.toUser().toEntityReference());
        List<IRecipient> recipients = newArrayList(messageRecipient);
        message.setRecipients(recipients);
        // there's no real hook to just to the template substitution so we will just send the message
        listener.sendEmail(message);
    }

}
