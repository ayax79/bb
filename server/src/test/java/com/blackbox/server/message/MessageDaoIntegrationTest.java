package com.blackbox.server.message;

import com.blackbox.activity.IRecipient;
import com.blackbox.message.MailboxRequest;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.user.User;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static com.blackbox.message.MailboxRequest.MailboxFolder.*;
import static junit.framework.Assert.*;

public class MessageDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IMessageDao messageDao;

    @Resource
    IUserDao userDao;

    @Test
    public void testCrud() {

        User user = userDao.loadUserByGuid("1");

        Message m = Message.createMessage();
        m.setBody("sldkfjksldf");
        m.getArtifactMetaData().setArtifactOwner(user.toEntityReference());
        MessageRecipient r = new MessageRecipient(user.toEntityReference(), m.getGuid());
        r.setMessageMetaData(m.getArtifactMetaData());
        r.setStatus(IRecipient.RecipientStatus.INBOX);
        m.getRecipients().add(r);
        m.setRecipientDepth(NetworkTypeEnum.FOLLOWING);
        m.setSubject("sdlkjfdfs");
        m.setPostDate(new DateTime());

        messageDao.save(m);

        assertNotNull(messageDao.loadByGuid(m.getGuid()));

        m.setSubject("sdf829");
        messageDao.save(m);

        Message m2 = messageDao.loadByGuid(m.getGuid());
        assertNotNull(m2);
        assertEquals(m.getSubject(), m2.getSubject());

        messageDao.delete(m);
    }

    @Test
    public void recipientMessages() {
        MailboxRequest mr = new MailboxRequest();
        mr.setUserGuid("1");
        mr.setFolder(INBOX_FOLDER);
        mr.setReadState(MailboxRequest.ReadState.EITHER);

        List<Message> list = messageDao.loadMailboxMessages(mr);
        assertNotNull(list);
        assertFalse(list.isEmpty());

        mr.setFolder(SENT_FOLDER);
        list = messageDao.loadMailboxMessages(mr);
        assertNotNull(list);
        assertFalse(list.isEmpty());

        mr.setFolder(ARCHIVED_FOLDER);
        mr.setUserGuid(UserFixture.aj.getGuid());
        mr.setMaxResults(10);
        mr.setStartIndex(0);
        mr.setFilter(MailboxRequest.MailboxFilter.ALL);
        list = messageDao.loadMailboxMessages(mr);
        assertNotNull(list);

    }

    @Test
    public void testLoadMailboxMessages() {
        User aj = userDao.loadUserByUsername("aj");

        MailboxRequest mr = new MailboxRequest();
        mr.setUserGuid(aj.getGuid());

        List<Message> list = messageDao.loadMailboxMessages(mr);
        assertTrue(!list.isEmpty());

    }

    @Test
    public void loadPublicParentMessagesTest() {
        List<Message> list = messageDao.loadPublicParentMessages();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }


}
