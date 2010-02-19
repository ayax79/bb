package com.blackbox.server.activity;

import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.common.TwoBounds;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.util.MessagesHelper;
import com.blackbox.util.RelationsHelper;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.test.annotation.NotTransactional;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.HashMapCacheManager;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static com.blackbox.foundation.util.CollectionHelper.sameSize;
import static com.blackbox.testingutils.UserHelper.createNamedUser;
import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class ActivityManagerIntegrationTest extends BaseIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ActivityManagerIntegrationTest.class);

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

    private ICacheManager<String, Collection<Message>> prePublishedMessageCache;

    private String bodyMessageBeginning = "what about bob? number: ";
    private RelationsHelper relationsHelper;
    private MessagesHelper messagesHelper;

    private User poster;
    private User viewer;

    @Before
    public void initialize() {
        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        prePublishedMessageCache = new HashMapCacheManager<String, Collection<Message>>();
        poster = createNamedUser("poster", userManager);
        viewer = createNamedUser("viewer", userManager);
        relationsHelper = new RelationsHelper(socialManager);
        messagesHelper = new MessagesHelper(messageManager, activityManager, prePublishedMessageCache);
    }

    @Ignore(value = "junit.framework.AssertionFailedError: where's our missing messages? expected:<5> but was:<1>")
    @Test
    //APP-215 Inconsistency in stream posts. user posts multiple posts to 'friends' and they only see one of the posts
    // and that post will 'cycle' amongst those posts.
    @NotTransactional
    // for some reason, if this test case is transactional, we never get any posts on fetch calls...
    public void testLoadFriendsActivityThreads() throws Exception {
        relationsHelper.createBidirectionalFriendship(poster, viewer);

        Collection<IActivityThread> messages = messagesHelper.fetchMessages(viewer, new TwoBounds(0, 10), NetworkTypeEnum.FRIENDS);
        assertTrue(messages.isEmpty());

        messagesHelper.publishMessage(poster, bodyMessageBeginning + 1, NetworkTypeEnum.FRIENDS);
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 2, NetworkTypeEnum.FRIENDS);

        // why the poster? the viewer always has zero here!
        messages = messagesHelper.fetchMessages(poster, new TwoBounds(0, 10), NetworkTypeEnum.FRIENDS);

        assertEquals("where's our missing messages?", 2, messages.size());

        messagesHelper.publishMessage(poster, bodyMessageBeginning + 3, NetworkTypeEnum.FRIENDS);
        Message parent = messagesHelper.publishMessage(poster, bodyMessageBeginning + 4, NetworkTypeEnum.FRIENDS);
        messagesHelper.publishChildMessage(poster, parent, bodyMessageBeginning + 5, NetworkTypeEnum.FRIENDS);

        messagesHelper.publishMessage(poster, bodyMessageBeginning + 6, NetworkTypeEnum.FRIENDS);

        assertEndStateIsCorrect(messagesHelper.fetchMessages(poster, new TwoBounds(0, 10), NetworkTypeEnum.FRIENDS));

        prePublishedMessageCache.flushAll();   // hang up your boots
        Thread.sleep(5000); // let all messages digest and the server will give them to us...
        assertEndStateIsCorrect(messagesHelper.fetchMessages(poster, new TwoBounds(0, 10), NetworkTypeEnum.FRIENDS));
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
        relationsHelper.createBidirectionalFriendship(poster, viewer);

        Collection<IActivityThread> messages = fetch10GlobalMessages(poster);
        int numberOfMessagesAtStart = messages.size();

        // todo: change this test to test for if the server is returning the message (then there will be 10 instead of 11)
        // or if the message it *only* in cache...
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 1, NetworkTypeEnum.WORLD);

        messages = fetch10GlobalMessages(poster);
        assertEquals("where's our message?", numberOfMessagesAtStart + 1, messages.size());

        // we are seeing one message come through then not others so, let's add some more....
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 2, NetworkTypeEnum.WORLD);
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 3, NetworkTypeEnum.WORLD);
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 4, NetworkTypeEnum.WORLD);
        messagesHelper.publishMessage(poster, bodyMessageBeginning + 5, NetworkTypeEnum.WORLD);

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

    private Collection<IActivityThread> fetch10GlobalMessages(User viewer) {
        return messagesHelper.fetchMessages(viewer, FilterHelper.everyoneFilter(), new TwoBounds(0, 10));
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
        safeDelete(poster);
        safeDelete(viewer);
    }

    // use when test is marked as not transactional!

    private void safeDelete(User user) {
        if (user != null) {
            try {
                template.delete("User.delete", user.getGuid());
            } catch (Exception e) {
                /* too bad, so sad */
            }
        }
    }

    @Ignore
    @Test
    /**
     * This test just builds out 2 listings: a paginated one and an 'all items' listing...
     */
    public void attemptToFindWholeInStream() throws Exception {
        Collection<IActivityThread> totalPaginatedListing = newArrayList();
        Collection<IActivityThread> paginatedMessageListing;
        TwoBounds bounds = new TwoBounds(new Bounds(-10, 10), new Bounds(-10, 10));
        int totalPaginatedMessageSize = 0;

        do {
            logger.debug("---------------------------------------------------------------------------");
            paginatedMessageListing = messagesHelper.fetchMessages(viewer, FilterHelper.everyoneFilter(), bounds.next(10));
            logger.debug("bounds = " + bounds);
            for (IActivityThread message : paginatedMessageListing) {
                logger.debug(++totalPaginatedMessageSize + "message.getParent().getPostDate() = " + format(message));
            }
            totalPaginatedListing.addAll(paginatedMessageListing);
        } while (!paginatedMessageListing.isEmpty());

        logger.debug("---------------------------------------------------------------------------");
        logger.debug("---------------------------------------------------------------------------");

        int i = 0;
        logger.debug("ActivityManagerIntegrationTest.testLoadGlobalActivityThreads.all at once");
        Collection<IActivityThread> wholeMessageListing = messagesHelper.fetchMessages(viewer, FilterHelper.everyoneFilter(), TwoBounds.boundLess());
        for (IActivityThread message : wholeMessageListing) {
            logger.debug(++i + "message.getParent().getPostDate() = " + format(message));
        }
        logger.debug("---------------------------------------------------------------------------");

        for (int loop = 0; loop < sameSize(totalPaginatedListing, wholeMessageListing); loop++) {
            IActivityThread one = (IActivityThread) CollectionUtils.get(totalPaginatedListing, loop);
            IActivityThread two = (IActivityThread) CollectionUtils.get(wholeMessageListing, loop);
            assertEquals(one, two);
        }
    }

    private String format(IActivityThread message) {
        DateTime date = message.getParent().getPostDate();
        try {
            return new SimpleDateFormat("d MMM yyyy HH:mm:ss").format(new Date(date.getMillis()));
        } catch (Exception e) {
            return "date? " + date;
        }
    }


}
