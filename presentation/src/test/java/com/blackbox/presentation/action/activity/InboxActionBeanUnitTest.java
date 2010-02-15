package com.blackbox.presentation.action.activity;

import com.blackbox.activity.ActivityThread;
import com.blackbox.message.IMessageManager;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import static com.blackbox.presentation.TestUtil.buildRoundTrip;
import com.blackbox.presentation.session.UserSessionService;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.presentation.extension.DefaultBlackBoxContext;
import com.blackbox.presentation.extension.MockBlackBoxContext;
import com.blackbox.user.User;
import com.blackbox.user.IUserManager;
import com.blackbox.social.ISocialManager;
import com.blackbox.activity.IRecipient;
import static com.blackbox.activity.IRecipient.RecipientStatus.DELETED;
import static com.blackbox.activity.IRecipient.RecipientStatus.ARCHIVED;
import static com.blackbox.Utils.array;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.validation.ValidationErrors;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
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
