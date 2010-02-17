/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.message.MailboxRequest;
import static com.blackbox.foundation.message.MailboxRequest.MailboxFilter.WISHED;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.server.bookmark.IBookmarkDao;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.user.User;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@RunWith(MockitoJUnitRunner.class)
public class MessageManagerUnitTest {

    @Mock IMessageDao messageDao;
    @Mock IBookmarkDao bookmarkDao;
    @Mock
    IEntityManager entityManager;

    @Test
    public void loadMailboxMessagesWishedNoResults() {

        MailboxRequest request = new MailboxRequest();
        request.setUserGuid("329sadflkasdfjl");
        request.setFilter(WISHED);


        Message m0 = Message.createMessage();
        m0.getArtifactMetaData().setArtifactOwner(User.createUser().toEntityReference());

        Message m1 = Message.createMessage();
        m1.getArtifactMetaData().setArtifactOwner(User.createUser().toEntityReference());

        Message m2 = Message.createMessage();
        m2.getArtifactMetaData().setArtifactOwner(User.createUser().toEntityReference());

        MessageManager messageManager = new MessageManager();
        messageManager.messageDao = messageDao;
        messageManager.bookmarkDao = bookmarkDao;

        when(messageDao.loadMailboxMessages(request)).thenReturn(newArrayList(m0, m1, m2));
        when(bookmarkDao.loadByUserGuidAndToEntityType(request.getUserGuid(), EntityType.USER)).thenReturn(Lists.<Bookmark>newArrayList());

        PaginationResults<IActivityThread<Message>> results = messageManager.loadMailboxMessages(request);
        assertNotNull(results);
        assertEquals(0, results.getResults().size());
    }


    @Test
    public void testPopulateMessageTransients() {

        Message message = new Message();
        User owner = User.createUser();
        message.getArtifactMetaData().setArtifactOwner(owner.toEntityReference());
        when(entityManager.loadEntity(owner.toEntityReference())).thenReturn(owner);

        User recipient = User.createUser();
        message.getRecipients().add(new MessageRecipient(recipient.toEntityReference(), owner.getGuid()));
        when(entityManager.loadEntity(recipient.toEntityReference())).thenReturn(recipient);


        MessageManager messageManager = new MessageManager();
        messageManager.entityManager = entityManager;

        messageManager.populateMessageTransients(message);
        assertEquals(message.getArtifactMetaData().getArtifactOwnerObject(), owner);
        assertEquals(message.getRecipients().get(0).getRecipientObject(), recipient);

        verify(entityManager, times(1)).loadEntity(owner.toEntityReference());
        verify(entityManager, times(1)).loadEntity(recipient.toEntityReference());
    }


}
