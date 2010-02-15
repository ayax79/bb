/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message;

import com.blackbox.activity.ActivityReference;
import com.blackbox.activity.IRecipient;
import com.blackbox.message.MailboxRequest;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.util.Bounds;
import org.compass.core.CompassOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.blackbox.activity.IRecipient.RecipientStatus.ARCHIVED;
import static com.blackbox.activity.IRecipient.RecipientStatus.INBOX;
import static com.blackbox.message.MailboxRequest.MailboxFolder.*;
import static com.blackbox.message.MailboxRequest.ReadState;
import static com.blackbox.server.util.PersistenceUtil.insertOrUpdate;

/**
 *
 *
 */
@Repository("messageDao")
@SuppressWarnings({"unchecked"})
public class IbatisMessageDao implements IMessageDao {

    private static final Logger logger = LoggerFactory.getLogger(IbatisMessageDao.class);

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;

    @Resource
    CompassOperations compassTemplate;

    @Override
    @Transactional(readOnly = false)
    public void save(Message message) {
        insertOrUpdate(message.getArtifactMetaData(), template, "MessageMetaData.insert", "MessageMetaData.update");
        insertOrUpdate(message, template, "Message.insert", "Message.update");
        List<IRecipient> recipients = message.getArtifactMetaData().getRecipients();
        if (recipients != null) {
            for (IRecipient r : recipients) {
                MessageRecipient mr = (MessageRecipient) r;
                mr.setMessageMetaData(message.getArtifactMetaData());
                save(mr);
            }
        }
        compassTemplate.save(message);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(List<Message> messages) {
        for (Message message : messages) {
            save(message);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void save(MessageRecipient recipient) {
        insertOrUpdate(recipient, template, "MessageRecipient.insert", "MessageRecipient.update");
    }

    @Override
    public List<Message> loadMailboxMessages(final MailboxRequest request) {

        List<String> list0 = mailboxThreads(request);
        if (list0 != null && !list0.isEmpty()) {
            return template.selectList("Message.loadWithParent", list0);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Message> loadAllAssociatedMessages(final String guid, final NetworkTypeEnum... types) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("types", types);

        return template.selectList("Message.loadAllAssociatedMessages", params);
    }

    public List<String> mailboxThreads(MailboxRequest request) {
        MailboxRequest.MailboxFolder folder = request.getFolder();
        ReadState readState = request.getReadState();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("published", request.getFolder() != DRAFTS_FOLDER);
        params.put("recipientDepth", NetworkTypeEnum.SUPER_DIRECT);
        params.put("userGuid", request.getUserGuid());
        params.put("folder", folder);
        params.put("readState", readState);

        if (INBOX_FOLDER == folder) {
            params.put("status", INBOX);
        } else if (ARCHIVED_FOLDER == folder) {
            params.put("status", ARCHIVED);
        }

        params.put("bounds", new Bounds(request.getStartIndex(), request.getMaxResults()));

        return template.selectList("Message.loadRecipientMessages", params);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Message message) {
        template.delete("MessageRecipient.deleteByMetaDataGuid", message.getArtifactMetaData().getGuid());
        template.delete("Message.delete", message.getGuid());
        template.delete("MessageMetaData.delete", message.getArtifactMetaData().getGuid());
        compassTemplate.delete(message);
    }

    @Override
    public Message loadByGuid(String messageGuid) {
        return (Message) template.selectOne("Message.load", messageGuid);
    }

    @Override
    public List<Message> loadMessageWithParent(String... guid) {
        if (guid.length > 0) {
            return template.selectList("Message.loadWithParent", Arrays.asList(guid));
        } else {
            return Collections.emptyList();
        }
    }

    public List<Message> loadPublicParentMessages() {
        return template.selectList("Message.loadPublicParentMessages");
    }

    public void reindex() {
        List<Message> list = template.selectList("Message.loadAll");
        for (Message message : list) {
            logger.info("Reindexing message: " + message.getGuid());
            compassTemplate.save(message);
        }

    }

}
