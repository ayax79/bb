/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.message;

import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivityThread;
import com.blackbox.user.PaginationResults;

import java.util.List;

/**
 * Used for acquiring and creating messages for users.
 * <p/>
 * <p/>
 * Note that all messages will be returned in a flat view (children and parents together and unless you call a method that returns a {@link com.blackbox.activity.ActivityThread} object.
 */
public interface IMessageManager {

    Message publish(Message message);

    void deleteMessage(String messageGuid, String userDeletingGuid);

    PaginationResults<IActivityThread<Message>> loadMailboxMessages(MailboxRequest request);

    void updateRecipient(MessageRecipient recipient);

    ActivityThread<Message> loadMessageThreadByParentGuid(String parentGuid);

    Message loadMessageByGuid(String messageGuid);

    Message save(Message message);

    /**
     * Returns up to 25 of the latest public messages from the system.
     *
     * @return
     */
    List<Message> loadRecentPublicParentMessages();
}
