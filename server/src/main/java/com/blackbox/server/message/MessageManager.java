/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message;

import com.blackbox.BaseEntity;
import com.blackbox.EntityReference;
import com.blackbox.EntityType;
import com.blackbox.IEntityManager;
import com.blackbox.activity.ActivityProfile;
import com.blackbox.activity.ActivityThread;
import com.blackbox.activity.IActivityThread;
import com.blackbox.activity.IRecipient;
import com.blackbox.bookmark.Bookmark;
import com.blackbox.media.AvatarImage;
import com.blackbox.media.IMediaManager;
import com.blackbox.message.IMessageManager;
import com.blackbox.message.MailboxRequest;
import com.blackbox.message.Message;
import com.blackbox.message.MessageRecipient;
import com.blackbox.server.bookmark.IBookmarkDao;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.event.DeleteMessageEvent;
import com.blackbox.server.message.event.PublishMessageEvent;
import com.blackbox.server.social.INetworkDao;
import com.blackbox.server.user.IUserDao;
import com.blackbox.social.Relationship;
import com.blackbox.user.PaginationResults;
import com.blackbox.user.User;
import com.blackbox.util.PaginationUtil;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.publish.client.IPublishBridge;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.blackbox.Utils.applyGuid;
import static com.blackbox.activity.AscendingActivityComparator.getAscendingActivityComparator;
import static com.blackbox.message.MailboxRequest.MailboxFilter;
import static com.blackbox.server.activity.ActivityUtil.createActivityThreadList;
import static com.blackbox.server.activity.ActivityUtil.createActivityThreadListTyped;
import static com.blackbox.social.Relationship.RelationStatus;
import static com.google.common.collect.Lists.transform;
import static java.util.Collections.sort;


@Service("messageManager")
public class MessageManager extends BaseServiceContainer implements IMessageManager {

    @Resource
    IMediaDao mediaDao;
    @Resource
    IMessageDao messageDao;
    @Resource
    IUserDao userDao;
    @Resource
    INetworkDao networkDao;
    @Resource
    IBookmarkDao bookmarkDao;
    @Resource
    IMediaManager mediaManager;
    @Resource
    IEntityManager entityManager;
    @Resource(name = "messagePublishBridge")
    IPublishBridge bridge;

    @Override
    public Message loadMessageByGuid(String messageGuid) {
        return messageDao.loadByGuid(messageGuid);
    }

    @Override
    public Message publish(Message message) {
        // If the guid hasn't been set go ahead and set it, so that it can
        // be returned back to the user
        if (message.getGuid() == null) {
            applyGuid(message);
        }
        bridge.publish(message);
        getEventMulticaster().process(new PublishMessageEvent(message));
        return message;
    }

    public Message save(Message message) {
        messageDao.save(message);
        return message;
    }

    @Override
    public void deleteMessage(String messageGuid, String userDeletingGuid) {
        Message message = messageDao.loadByGuid(messageGuid);
        if (message == null) {
            throw new IllegalArgumentException("No message with the specified guid exists : " + messageGuid);
        }

        User user = userDao.loadUserByGuid(userDeletingGuid);
        if (user == null) {
            throw new IllegalArgumentException("No User with object with the specified guid exists : " + userDeletingGuid);
        }

        if (user.getType() == User.UserType.BLACKBOX_EMPLOYEE ||
                user.getGuid().equals(message.getArtifactMetaData().getArtifactOwner().getGuid())) {

            DeleteMessageEvent event = new DeleteMessageEvent(message);
            getEventMulticaster().process(event);
        } else {
            throw new UnauthorizedException("Message cannot be deleted by user " + user.getUsername());
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public ActivityThread<Message> loadMessageThreadByParentGuid(String parentGuid) {
        List<Message> messages = messageDao.loadMessageWithParent(parentGuid);
        if (messages != null && !messages.isEmpty()) {
            sort(messages, getAscendingActivityComparator());
            List<IActivityThread> activityThreadList = createActivityThreadList(messages);
            assert activityThreadList.size() == 1;
            return (ActivityThread<Message>) activityThreadList.get(0);
        }
        return null;
    }

    @Override
    public List<Message> loadRecentPublicParentMessages() {
        List<Message> list = messageDao.loadPublicParentMessages();
        List<Message> filtered = new ArrayList<Message>(list.size());
        for (Message message : list) {
            if (message.getArtifactMetaData().getArtifactOwner() != null) {
                User u = userDao.loadUserByGuid(message.getArtifactMetaData().getArtifactOwner().getGuid());
                if (u != null) {
                    AvatarImage image = mediaManager.loadAvatarImageFor(EntityType.USER, u.getGuid(), null);
                    if (image != null) {
                        ActivityProfile profile = new ActivityProfile();
                        profile.setSenderDisplayName(u.getUsername());
                        profile.setSenderAvatarImage(image.getImageLink());
                        message.getArtifactMetaData().setSenderProfile(profile);

                        // We only want to add people that have a valid avatar 
                        filtered.add(message);
                    }
                }
            }
        }
        return filtered;
    }


    @Override
    public PaginationResults<IActivityThread<Message>> loadMailboxMessages(MailboxRequest request) {
        List<Message> messages = messageDao.loadMailboxMessages(request);

        // Apply any filters
        MailboxRequest.MailboxFilter filter = request.getFilter();
        if (filter != null && filter != MailboxFilter.ALL) {


            Predicate<Message> predicate = null;

            if (filter == MailboxFilter.FOLLOWING) {
                predicate = buildRelationPredicate(request, RelationStatus.FOLLOW, RelationStatus.FRIEND);

            } else if (filter == MailboxFilter.FOLLWERS) {

                final List<Relationship> relationships = networkDao.loadRelationshipsByToEntityGuid(request.getUserGuid());
                final List<String> guids = transform(relationships, new Function<Relationship, String>() {
                    @Override
                    public String apply(Relationship from) {
                        return from.getFromEntity().getGuid();
                    }
                });

                predicate = buildMessageGuidPredicate(guids);

            } else if (filter == MailboxFilter.FRIENDS) {
                predicate = buildRelationPredicate(request, RelationStatus.FRIEND, RelationStatus.MAX_STATUS);

            } else if (filter == MailboxFilter.WISHED) {

                final List<Bookmark> bookmarks = bookmarkDao.loadByUserGuidAndToEntityType(request.getUserGuid(), EntityType.USER);
                final List<String> guids = transform(bookmarks, new Function<Bookmark, String>() {
                    @Override
                    public String apply(Bookmark from) {
                        return from.getTarget().getGuid();
                    }
                });

                predicate = buildMessageGuidPredicate(guids);
            }

            if (predicate != null) {
                Collection<Message> list = Collections2.filter(messages, predicate);
                messages = new ArrayList<Message>(list);
            }
        }
        sort(messages, getAscendingActivityComparator());
        List<IActivityThread<Message>> threads = createActivityThreadListTyped(messages);
        sort(threads, InboxComparator.<Message>getInboxComparator());

        // yeah this is all a bit evil now, because i am pulling every possible thread then handling
        // some of them back. This will have to do for now, out of time!
        int total = threads.size();
        threads = PaginationUtil.subList(threads, request.getStartIndex(), request.getMaxResults());

        populateMessageTransientsListFromThreads(threads);

        // collect the thread count
        PaginationResults<IActivityThread<Message>> results = new PaginationResults<IActivityThread<Message>>();
        results.setTotalResults(total);
        results.setNumResults(threads.size());
        results.setStartIndex(request.getStartIndex());
        results.setResults(threads);


        return results;
    }

    protected Predicate<Message> buildRelationPredicate(MailboxRequest request,
                                                        Relationship.RelationStatus start,
                                                        Relationship.RelationStatus stop) {
        List<Relationship> relationships = networkDao.loadRelationshipsByFromEntityGuidAndWeightRange(
                request.getUserGuid(), start.getWeight(), stop.getWeight());

        // get an relationships of strings
        final List<String> guids = transform(relationships, new Function<Relationship, String>() {

            @Override
            public String apply(Relationship r) {
                return r.getToEntity().getGuid();
            }
        });

        return buildMessageGuidPredicate(guids);
    }

    protected Predicate<Message> buildMessageGuidPredicate(final List<String> guids) {
        return new Predicate<Message>() {
            @Override
            public boolean apply(Message input) {
                for (IRecipient recipient : input.getRecipients()) {
                    if (guids.contains(recipient.getRecipient().getGuid())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public void updateRecipient(MessageRecipient recipient) {
        messageDao.save(recipient);
    }

    protected void populateMessageTransientFromThread(IActivityThread<Message> thread) {
        if (thread == null) return;
        populateMessageTransients(thread.getParent());
        populateMessageTransientsList(thread.getChildren());
    }

    protected void populateMessageTransientsList(Collection<Message> messages) {
        if (messages != null && !messages.isEmpty()) {
            for (Message message : messages) {
                populateMessageTransients(message);
            }
        }
    }

    protected void populateMessageTransients(Message message) {
        if (message == null) return;

        EntityReference artifactOwner = message.getArtifactMetaData().getArtifactOwner();
        if (artifactOwner != null) {
            BaseEntity entity = (BaseEntity) entityManager.loadEntity(artifactOwner);
            message.getArtifactMetaData().setArtifactOwnerObject(entity);
        }

        List<IRecipient> recipients = message.getRecipients();
        populateRecipientTransientsList(recipients);
    }

    protected void populateRecipientTransientsList(List<IRecipient> recipients) {
        if (recipients != null && !recipients.isEmpty()) {
            for (IRecipient recipient : recipients) {
                populateRecipientTransients(recipient);
            }
        }
    }

    protected void populateRecipientTransients(IRecipient recipient) {
        if (recipient == null) return;

        EntityReference entityReference = recipient.getRecipient();
        if (entityReference != null) {
            BaseEntity entity = entityManager.loadEntity(entityReference);
            recipient.setRecipientObject(entity);
        }
    }

    protected void populateMessageTransientsListFromThreads(List<IActivityThread<Message>> threads) {
        if (threads != null && !threads.isEmpty()) {
            for (IActivityThread<Message> thread : threads) {
                populateMessageTransientFromThread(thread);

            }
        }
    }


}
