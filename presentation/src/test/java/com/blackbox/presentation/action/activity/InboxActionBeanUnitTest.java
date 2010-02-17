package com.blackbox.presentation.action.activity;

import com.blackbox.foundation.activity.ActivityThread;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import static com.blackbox.presentation.TestUtil.buildRoundTrip;

import com.blackbox.presentation.extension.MockBlackBoxContext;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.activity.IRecipient;

import static com.blackbox.foundation.Utils.array;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.List;

/**
 * @author A.J. Wright
 */
@RunWith(MockitoJUnitRunner.class)
public class InboxActionBeanUnitTest {

    @Mock IMessageManager messageManager;
    InboxActionBean action;
    MockBlackBoxContext context;

    @Before
    public void setup() {
        action = new InboxActionBean();
        context = spy(new MockBlackBoxContext());
        action.setContext(context);
        action.messageManager = messageManager;
    }

    @Test
    public void buildReplyRecipientsTest() {
        User user1 = User.createUser();
        User user2 = User.createUser();

        Message m1 = Message.createMessage();
        m1.getArtifactMetaData().setArtifactOwner(user1.toEntityReference());
        m1.getRecipients().add(new MessageRecipient(user2.toEntityReference(), m1.getGuid()));

        Message m2 = Message.createMessage();
        when(context.getUser()).thenReturn(user2);
        action.buildReplyRecipients(new ActivityThread<Message>(m1), m2);
        List<IRecipient> recipients = m2.getRecipients();
        assertNotNull(recipients);
        assertEquals(1, recipients.size());
        assertEquals(user1.getGuid(), recipients.get(0).getRecipient().getGuid());
    }


}
