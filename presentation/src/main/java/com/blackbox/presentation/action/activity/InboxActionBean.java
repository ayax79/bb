package com.blackbox.presentation.action.activity;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.notification.Notifications;
import com.blackbox.foundation.activity.*;

import static com.blackbox.foundation.activity.IRecipient.RecipientStatus.DELETED;
import static com.blackbox.foundation.activity.IRecipient.RecipientStatus.ARCHIVED;

import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.message.MailboxRequest;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.CommaStringConverter;
import com.blackbox.presentation.action.util.JspFunctions;

import static com.blackbox.foundation.social.NetworkTypeEnum.SUPER_DIRECT;

import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.PaginationResults;
import com.google.common.collect.Lists;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.joda.time.DateTime;

import java.util.*;

/**
 * @author A.J. Wright
 */
@SuppressWarnings({"UnusedDeclaration"})
public class InboxActionBean extends BaseBlackBoxActionBean {


    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("messageManager")
    IMessageManager messageManager;

    @SpringBean("socialManager")
    ISocialManager socialManager;

    @Validate(required = true, on = "publish")
    private String subject;

    @Validate(required = true, on = "publish")
    private String body;

    @Validate(required = true, on = "publish", converter = CommaStringConverter.class)
    private String[] recipientGuid;

    @Validate(required = true, on = "read")
    private String parentGuid;

    @Validate(required = true, on = "delete", converter = CommaStringConverter.class)
    private String[] messageGuid;

    private List<IRecipient> recipients;

    private PaginationResults<IActivityThread<Message>> messages;

    private Message message;

    private String userIdentifier;

    private List<User> recipientUsers;

    private Notifications notifications;

    private ActivityThread<Message> thread;

    private Map<String, User> userGuidMap = new UserGuidMap();

    @ValidateNestedProperties({
            @Validate(field = "folder", required = true, on = "load")
    })
    private MailboxRequest mailboxRequest = new MailboxRequest();

    @DefaultHandler
    @DontValidate
    public Resolution begin() {
        mailboxRequest.setUserGuid(getCurrentUser().getGuid());
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        notifications = socialManager.loadLimitNotifications(getCurrentUser().getGuid(), 2);
        return new ForwardResolution("/messages.jsp");
    }

    public Resolution load() {
        mailboxRequest.setUserGuid(getCurrentUser().getGuid());
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        return new ForwardResolution("/ajax/messaging/mailbox.jspf");
    }

    public Resolution read() {
        thread = messageManager.loadMessageThreadByParentGuid(parentGuid);

        // find unread messages in thread and mark them as read
        markReadUnread(thread, true);

        return new ForwardResolution("/ajax/messaging/message.jspf");
    }


    @ValidationMethod(on = "publish", when = ValidationState.NO_ERRORS)
    public void determineRecipient() {
        message = Message.createMessage();
        recipients = new ArrayList<IRecipient>();
        recipientUsers = new ArrayList<User>();
        for (String guid : recipientGuid) {

            User u = null;

            // ignore at usernames
            if (!guid.startsWith("@")) {
                u = userManager.loadUserByGuid(guid);
            }

            // maybe it's a username
            if (u == null) {
                if (guid.startsWith("@")) {
                    guid = guid.replace("@", "");
                }
                u = userManager.loadUserByUsername(guid);
            }

            if (u == null) {
                getContext().getValidationErrors().add("recipientGuid", new SimpleError("User does not exist"));
                return;
            }
            recipients.add(new MessageRecipient(u.toEntityReference(), message.getGuid()));
            recipientUsers.add(u);
        }
    }

    public Resolution compose() {
        recipientUsers = new ArrayList<User>();
        if (userIdentifier != null && !userIdentifier.isEmpty()) {
            User u = userManager.loadUserByGuid(userIdentifier);
            recipientUsers.add(u);
        }
        return new ForwardResolution("/ajax/messaging/compose.jspf");
    }

    public Resolution reply() {
        thread = messageManager.loadMessageThreadByParentGuid(parentGuid);
        Message parent = thread.getParent();
        Message last = JspFunctions.latestActivity(thread);
        IRecipient userRecipient = getUserRecipient(last, getCurrentUser());
        if (userRecipient != null) {
            userRecipient.setReplyDate(new DateTime());
        }
        else {
            // Then you must be replying twice
            List<IRecipient> recipients = last.getRecipients();
            if (recipients != null && !recipients.isEmpty()) {
                userRecipient = recipients.get(0);
                if (userRecipient != null) {
                    userRecipient.setReplyDate(new DateTime());
                }

            }
        }
        messageManager.updateRecipient((MessageRecipient) userRecipient);
        message = Message.createMessage();
        buildReplyRecipients(thread, message);
        thread.addChild(message);
        handlePublish(message);
        body = null;
        return new ForwardResolution("/ajax/messaging/message.jspf");
    }

    public Resolution publish() {
        message = Message.createMessage();
        message.getArtifactMetaData().setRecipients(recipients);
        handlePublish(message);
        return new ForwardResolution("/ajax/messaging/send-response.jspf");
    }

    public Resolution delete() {
        markStatus(DELETED);
        mailboxRequest.setUserGuid(getCurrentUser().getGuid());
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        return new ForwardResolution("/ajax/messaging/mailbox.jspf");
    }

    public Resolution markUnread() {
        // The messageGuids passed in is for a parent message for each thread.
        // we must then iterate through the thread and mark the current user's messages as
        // unread.

        for (String guid : messageGuid) {
            ActivityThread<Message> thread1 = messageManager.loadMessageThreadByParentGuid(guid);
            markReadUnread(thread1, false);
        }

        mailboxRequest.setUserGuid(getCurrentUser().getGuid());
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        return new ForwardResolution("/ajax/messaging/mailbox.jspf");
    }

    public Resolution archive() {
        markStatus(ARCHIVED);
        mailboxRequest.setUserGuid(getCurrentUser().getGuid());
        messages = messageManager.loadMailboxMessages(mailboxRequest);
        return new ForwardResolution("/ajax/messaging/mailbox.jspf");
    }

    public Resolution draft() {
        message = Message.createMessage();
        message.setPublished(false);
        handlePublish(message);
        return new ForwardResolution("/ajax/messaging/send-response.jspf");
    }

    /**
     * Grabs the {@link com.blackbox.foundation.activity.IRecipient} that correctly matches a specific user.
     *
     * @param m The message to find a recipient for.
     * @param u The user object that will match the recipient.
     * @return The IRecipient object for the user
     */
    public static IRecipient getUserRecipient(Message m, User u) {
        String userGuid = u.getGuid();
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

    private void markReadUnread(ActivityThread<Message> thread, boolean flag) {
        markReadUnread(thread.getParent(), flag);
        if (thread.getChildren() != null && !thread.getChildren().isEmpty()) {
            for (Message m : thread.getChildren()) {
                markReadUnread(m, flag);
            }
        }
    }


    protected void markStatus(IRecipient.RecipientStatus status) {
        for (String mg : messageGuid) {
            IActivityThread<Message> thread = messageManager.loadMessageThreadByParentGuid(mg);
            markStatus(status, thread.getParent());

            for (Message m : thread.getChildren()) {
                markStatus(status, m);
            }
        }
    }

    private void markStatus(IRecipient.RecipientStatus status, Message m) {

        IRecipient userRecipient = getUserRecipient(m, getCurrentUser());
        if (userRecipient != null && userRecipient.getStatus() != status) {
            userRecipient.setStatus(status);
            messageManager.updateRecipient((MessageRecipient) userRecipient);
        }
    }


    protected void markReadUnread(Message m, boolean flag) {
        IRecipient userRecipient = getUserRecipient(m, getCurrentUser());
        if (userRecipient != null && userRecipient.isRead() != flag) {
            userRecipient.setRead(flag);
            messageManager.updateRecipient((MessageRecipient) userRecipient);
        }
    }

    protected void buildReplyRecipients(ActivityThread<Message> thread, Message reply) {

        String currentUserGuid = getCurrentUser().getGuid();
        String primaryUserGuid = thread.getParent().getArtifactMetaData().getArtifactOwner().getGuid();
        String secondaryUserGuid = thread.getParent().getRecipients().get(0).getRecipient().getGuid();

        // Basically This should always set the recipient as the other user.
        String toGuid;
        if (!currentUserGuid.equals(primaryUserGuid)) {
            toGuid = primaryUserGuid;
        }
        else {
            toGuid = secondaryUserGuid;
        }

        reply.setRecipients(Lists.<IRecipient>newArrayList(
                new MessageRecipient(new EntityReference(EntityType.USER, toGuid), reply.getGuid())));
    }

    protected void handlePublish(Message message) {
        message.setRecipientDepth(SUPER_DIRECT);
        message.setBody(body);
        message.setSubject(subject);
        message.getArtifactMetaData().setArtifactOwner(getCurrentUser().toEntityReference());
        message.getArtifactMetaData().setArtifactOwnerObject(getCurrentUser());

        if (isNotBlank(parentGuid)) {
            ActivityReference reference = new ActivityReference();
            reference.setActivityType(ActivityTypeEnum.MESSAGE);
            reference.setGuid(parentGuid);
            reference.setOwnerType(EntityType.MESSAGE);
            message.setParentActivity(reference);
        }
        assert !message.getRecipients().isEmpty();
        assert !getCurrentUser().getGuid().equals(message.getRecipients().get(0).getRecipient().getGuid());
        messageManager.publish(message);
    }

    public static boolean isRead(IActivityThread<Message> thread, User user) {
        SortedSet<Message> messages1 = thread.flatten();
        Iterator<Message> messageIterator = messages1.iterator();
        Message msg = messageIterator.next(); // there has to be at least one.
        IRecipient iRecipient = getUserRecipient(msg, user);

        // if the last recipient wasn't you find the last one that was you.
        // This will fix situations where you have marked a thread as unread, but
        // you were the last person to reply.
        while(iRecipient == null && messageIterator.hasNext()) {
            msg = messageIterator.next();
            iRecipient = getUserRecipient(msg, user);
        }
        return iRecipient == null || iRecipient.isRead();
    }

    public static boolean isReply(IActivityThread<Message> thread, User user) {
        Message msg = JspFunctions.latestActivity((ActivityThread<Message>) thread);
        IRecipient userRecipient = getUserRecipient(msg, user);
        return userRecipient != null && userRecipient.getReplyDate() != null;
    }

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.messages;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public PaginationResults<IActivityThread<Message>> getMessages() {
        return messages;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getRecipientGuid() {
        return recipientGuid;
    }

    public void setRecipientGuid(String[] recipientGuid) {
        this.recipientGuid = recipientGuid;
    }

    public List<IRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<IRecipient> recipients) {
        this.recipients = recipients;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public String[] getMessageGuid() {
        return messageGuid;
    }

    public void setMessageGuid(String[] messageGuid) {
        this.messageGuid = messageGuid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<User> getRecipientUsers() {
        return recipientUsers;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public IActivityThread<Message> getThread() {
        return thread;
    }

    public MailboxRequest getMailboxRequest() {
        return mailboxRequest;
    }

    public void setMailboxRequest(MailboxRequest mailboxRequest) {
        this.mailboxRequest = mailboxRequest;
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

    public Map<String, User> getUserGuidMap() {
        return userGuidMap;
    }

    public class UserGuidMap extends AbstractMap<String, User> {
        @Override
        public Set<Entry<String, User>> entrySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(Object key) {
            return userManager.loadUserByGuid((String) key);
        }
    }

}
