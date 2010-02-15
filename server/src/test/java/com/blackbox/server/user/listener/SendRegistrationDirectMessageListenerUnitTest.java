package com.blackbox.server.user.listener;

import com.blackbox.activity.IRecipient;
import com.blackbox.message.IMessageManager;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.server.user.event.RegisterUserEvent;
import com.blackbox.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SendRegistrationDirectMessageListenerUnitTest {

    @Mock
    IMessageManager messageManager;


    @Test
    public void testHandle() throws Exception {
        SendRegistrationDirectMessageListener listener = new SendRegistrationDirectMessageListener();
        listener.messageManager = messageManager;
        User user = User.createUser();
        user.setUsername("foobop");
        user.setFirstname("John");

        listener.handle(new RegisterUserEvent(user, null), null);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageManager).publish(captor.capture());


        Message result = captor.getValue();
        assertTrue(result.getBody().contains(user.getUsername()));
        assertTrue(result.getBody().contains(user.getFirstname()));
        assertFalse(result.getBody().contains("$"));

        List<IRecipient> iRecipients = result.getRecipients();
        assertNotNull(iRecipients);
        assertEquals(1, iRecipients.size());
        MessageRecipient messageRecipient = (MessageRecipient) iRecipients.get(0);
        assertEquals(user.toEntityReference(), messageRecipient.getRecipient());
        assertEquals(result.getGuid(), messageRecipient.getMessageGuid());
    }


}
