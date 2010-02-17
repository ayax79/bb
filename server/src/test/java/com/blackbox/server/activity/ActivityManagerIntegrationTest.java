package com.blackbox.server.activity;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.activity.ActivityFactory;
import com.blackbox.foundation.activity.ActivityRequest;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.PrePublicationUtil;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.test.annotation.NotTransactional;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.HashMapCacheManager;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

import static com.blackbox.testingutils.UserHelper.createNamedUser;
import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class ActivityManagerIntegrationTest extends BaseIntegrationTest {

    @Resource
    private IUserManager userManager;

    @Resource
    private ISocialManager socialManager;

    @Resource
    private IActivityManager activityManager;

    @Resource
    IMessageManager messageManager;

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionOperations template;

    ICacheManager<String, Collection<Message>> prePublishedMessageCache;

    private String bodyMessageBeginning = "what about bob? number: ";

    private User poster;
    private User viewer;

    @Before
    public void initialize() {
        prePublishedMessageCache = new HashMapCacheManager<String, Collection<Message>>();
        poster = createNamedUser("poster", userManager);
        viewer = createNamedUser("viewer", userManager);
    }

    @Ignore
    // this fails because we already have that on
    @Test
    //APP-215 Inconsistency in stream posts. user posts multiple posts to 'friends' and they only see one of the posts
    // and that post will 'cycle' amongst those posts.
    @NotTransactional
    // for some reason, if this test case is transactional, we never get any posts on fetch calls...
    public void testLoadFriendsActivityThreads() throws Exception {
        createBidirectionalFriendship(poster, viewer);

        Collection<IActivityThread> messages = fetchMessages(viewer, NetworkTypeEnum.FRIENDS);
        assertTrue(messages.isEmpty());

        publishMessage(poster, 1, NetworkTypeEnum.FRIENDS);
        publishMessage(poster, 2, NetworkTypeEnum.FRIENDS);

        // why the poster? the viewer always has zero here!
        messages = fetchMessages(poster, NetworkTypeEnum.FRIENDS);

        assertEquals("where's our missing messages?", 2, messages.size());

        publishMessage(poster, 3, NetworkTypeEnum.FRIENDS);
        Message parent = publishMessage(poster, 4, NetworkTypeEnum.FRIENDS);
        publishChildMessage(poster, parent, 5, NetworkTypeEnum.FRIENDS);

        publishMessage(poster, 6, NetworkTypeEnum.FRIENDS);

        assertEndStateIsCorrect(fetchMessages(poster, NetworkTypeEnum.FRIENDS));

        prePublishedMessageCache.flushAll();   // hang up your boots
        Thread.sleep(5000); // let all messages digest and the server will give them to us...
        assertEndStateIsCorrect(fetchMessages(poster, NetworkTypeEnum.FRIENDS));
    }

    private void assertEndStateIsCorrect(Collection<IActivityThread> messages) {
        assertEquals("where's our missing messages?", 5, messages.size());
        int lastMessageOrdinal = 0;
        for (IActivityThread message : messages) {
            for (Object object : message.getChildren()) {
                Message childMessage = (Message) object;
                assertTrue(childMessage.getBody().startsWith(bodyMessageBeginning));
                assertEquals(NetworkTypeEnum.FRIENDS, childMessage.getRecipientDepth());
                int messageNumber = Integer.parseInt(StringUtils.substringAfter(childMessage.getBody(), bodyMessageBeginning).trim());
                if (messageNumber == 5) {
                    assertNotNull(childMessage.getParentActivity());
                }
                assertTrue(messageNumber > lastMessageOrdinal);
                lastMessageOrdinal++;
            }
        }
    }

    @Test
    //APP-215 Inconsistency in stream posts. user posts multiple posts to 'friends' and they only see one of the posts
    // and that post will 'cycle' amongst those posts.
    @NotTransactional
    // for some reason, if this test case is transactional, we never get any posts on fetch calls...
    @Ignore
    public void testLoadGlobalActivityThreads() throws Exception {
        poster = createNamedUser("poster", userManager);
        viewer = createNamedUser("viewer", userManager);
        createBidirectionalFriendship(poster, viewer);

        Collection<IActivityThread> messages = fetch10GlobalMessages(poster);
        int numberOfMessagesAtStart = messages.size();

        // todo: change this test to test for if the server is returning the message (then there will be 10 instead of 11)
        // or if the message it *only* in cache...
        publishMessage(poster, 1, NetworkTypeEnum.WORLD);

        messages = fetch10GlobalMessages(poster);
        assertEquals("where's our message?", numberOfMessagesAtStart + 1, messages.size());

        // we are seeing one message come through then not others so, let's add some more....
        publishMessage(poster, 2, NetworkTypeEnum.WORLD);
        publishMessage(poster, 3, NetworkTypeEnum.WORLD);
        publishMessage(poster, 4, NetworkTypeEnum.WORLD);
        publishMessage(poster, 5, NetworkTypeEnum.WORLD);

        messages = fetch10GlobalMessages(poster);
        assertEquals("where's our messages?", numberOfMessagesAtStart + 5, messages.size());
        for (IActivityThread activityThread : messages) {
            for (Object o : activityThread.getChildren()) {
                if (o instanceof Message) {
                    Message message = (Message) o;
                    if (poster.getGuid().equals(message.getSender().getGuid())) {
                        assertTrue(message.getBody().startsWith(bodyMessageBeginning));
                        assertEquals(NetworkTypeEnum.WORLD, message.getRecipientDepth());
                    }
                }
            }
        }

    }

    private void createBidirectionalFriendship(User one, User two) {
        socialManager.relate(Relationship.createRelationship(two.toEntityReference(), one.toEntityReference(), Relationship.RelationStatus.FRIEND_PENDING));
        socialManager.relate(Relationship.createRelationship(one.toEntityReference(), two.toEntityReference(), Relationship.RelationStatus.FRIEND_PENDING));
        socialManager.acceptRequest(one.getGuid(), two.getGuid());
        socialManager.acceptRequest(two.getGuid(), one.getGuid());
        assertEquals(1, socialManager.loadRelationshipNetwork(one.getGuid()).getFriends().size());
        assertEquals(1, socialManager.loadRelationshipNetwork(two.getGuid()).getFriends().size());
    }

    private Message publishMessage(User poster, int i, NetworkTypeEnum depth) throws InterruptedException {
        Message message = ActivityFactory.createMessage();
        message.setBody(bodyMessageBeginning + i);
        message.setOwnerGuid(poster.getGuid());
        message.setPostDate(new DateTime());
        message.setRecipientDepth(depth);
        message.setOwnerType(EntityType.USER.ordinal());
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        PrePublicationUtil.prePublish(message, prePublishedMessageCache);
        return messageManager.publish(message);
    }

    private void publishChildMessage(User poster, Message parentMessage, int i, NetworkTypeEnum depth) throws InterruptedException {
        Message message = ActivityFactory.createMessage();
        message.setParentGuid(parentMessage.getGuid());
        message.setBody(bodyMessageBeginning + i);
        message.setOwnerGuid(poster.getGuid());
        message.setPostDate(new DateTime());
        message.setRecipientDepth(depth);
        message.setOwnerType(EntityType.USER.ordinal());
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        PrePublicationUtil.prePublish(message, prePublishedMessageCache);
        messageManager.publish(message);
    }

    private Collection<IActivityThread> fetchMessages(User viewer, NetworkTypeEnum... breadth) {
        return fetchMessages(viewer, Arrays.asList(breadth));
    }

    private Collection<IActivityThread> fetchMessages(User viewer, Collection<NetworkTypeEnum> breadth) {
        Collection<IActivityThread> serverActivitiesThread = activityManager.loadActivityThreads(new ActivityRequest(viewer.getEntityReference(), newArrayList(breadth), new Bounds(0, 10)));
        // this call to ActivityUtil emulates what happens in presentation layer which we do not have access to from here...
        return PrePublicationUtil.applyPrePublishedMessages(viewer, serverActivitiesThread, prePublishedMessageCache);
    }

    private Collection<IActivityThread> fetch10GlobalMessages(User viewer) {
        return fetchMessages(viewer, FilterHelper.everyoneFilter());
    }

    @Test
    //APP-215 Inconsistency in stream posts. user posts multiple posts to 'friends' and they only see one of the posts
    // and that post will 'cycle' amongst those posts.
    public void testLoadFollowerActivityThreads() throws Exception {
    }

    @Test
    public void testLoadPublicActivityThreads() throws Exception {
    }

    @Test
    public void testLoadActivityThreadByParentGuid() throws Exception {
    }

    @Test
    public void testLoadAssociatedActivity() throws Exception {
    }

    @Test
    public void testLoadAssociatedActivityFilterNetworkTypes() throws Exception {
    }

    @Test
    public void testLoadLastActivity() throws Exception {
    }

    @Test
    public void testPublish() throws Exception {
    }

    @Test
    public void testLoadChildrenActivityByParent() throws Exception {
    }

    @After
    public void deleteUsers() {
        if (poster != null) {
            template.delete("User.delete", poster.getGuid());
        }
        if (viewer != null) {
            template.delete("User.delete", viewer.getGuid());
        }
    }


}
