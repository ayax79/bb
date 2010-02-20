package com.blackbox.server.message;

import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.activity.IRecipient;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.MailboxRequest;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.util.CollectionHelper;
import com.blackbox.server.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class MessageManagerIntegrationTest extends BaseIntegrationTest {

    @Resource
    IMessageManager messageManager;
    private String userGuid;

    @Before
    public void initialize() {
        userGuid = "90bec0739979d5f5c552f02509c9e2879907eb42";
    }

    @Test
    public void testLoadInboxMailboxMessagesDoesNotLoadDeletedOrArchivedMessages() throws Exception {
        MailboxRequest mailboxRequest = new MailboxRequest();
        mailboxRequest.setUserGuid(userGuid);
        PaginationResults<IActivityThread<Message>> messages = messageManager.loadMailboxMessages(mailboxRequest);
        assertTrue("2 messages are need to complete this test case", messages.getTotalResults() > 1);
        long beginningMessagesSize = messages.getTotalResults();
        // mark first message deleted
        IActivityThread<Message> first = CollectionHelper.guaranteeFirst(messages.getResults());
        markStatus(IRecipient.RecipientStatus.DELETED, first.getParent().getGuid(), userGuid);
        // not not find it because it was deleted
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        assertEquals(beginningMessagesSize - 1, messages.getTotalResults());

        // mark first message archived
        first = CollectionHelper.guaranteeFirst(messages.getResults());
        markStatus(IRecipient.RecipientStatus.ARCHIVED, first.getParent().getGuid(), userGuid);
        // not not find it because it was archived
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        assertEquals(beginningMessagesSize - 2, messages.getTotalResults());
    }

    @Test
    public void testLoadInboxMailboxMessagesDoesNotLoadDraftsOrSentMessages() throws Exception {
        MailboxRequest mailboxRequest = new MailboxRequest();
        mailboxRequest.setUserGuid(userGuid);
        mailboxRequest.setFolder(MailboxRequest.MailboxFolder.ARCHIVED_FOLDER);
        PaginationResults<IActivityThread<Message>> messages = messageManager.loadMailboxMessages(mailboxRequest);
        assertNotNull(messages);
        mailboxRequest.setFolder(MailboxRequest.MailboxFolder.SENT_FOLDER);
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        assertNotNull(messages);
    }

    // copied from InboxActionBean...

    protected void markStatus(IRecipient.RecipientStatus status, String guid, String userGuid) {
        IActivityThread<Message> thread = messageManager.loadMessageThreadByParentGuid(guid);
        markStatus(status, thread.getParent(), userGuid);

        for (Message m : thread.getChildren()) {
            markStatus(status, m, userGuid);
        }
    }

    private void markStatus(IRecipient.RecipientStatus status, Message message, String userGuid) {

        IRecipient userRecipient = getUserRecipient(message, userGuid);
        if (userRecipient != null && userRecipient.getStatus() != status) {
            userRecipient.setStatus(status);
            messageManager.updateRecipient((MessageRecipient) userRecipient);
        }
    }

    public static IRecipient getUserRecipient(Message m, String userGuid) {
        List<IRecipient> recipients = m.getRecipients();

        if (recipients != null && !recipients.isEmpty()) {
            for (IRecipient recipient : recipients) {
                if (userGuid.equals(recipient.getRecipient().getGuid())) {
                    return recipient;
                }
            }
        }
        return null;
    }


}
