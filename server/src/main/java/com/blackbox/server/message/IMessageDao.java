/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.message.MailboxRequest;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 *
 *
 */
public interface IMessageDao {

    @Transactional(readOnly = false)
    void save(Message message);

    @Transactional(readOnly = false)
    void save(List<Message> messages);

    @Transactional(readOnly = false)
    void delete(Message message);

    Message loadByGuid(String messageGuid);

    @Transactional(readOnly = false)
    void save(MessageRecipient recipient);

    List<Message> loadMailboxMessages(MailboxRequest request);

    List<Message> loadMessageWithParent(String... guid);

    List<Message> loadAllAssociatedMessages(String guid, NetworkTypeEnum... types);

    List<Message> loadPublicParentMessages();

    void reindex();
}
