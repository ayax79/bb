package com.blackbox.server.activity;

import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.common.ActivitySenderOwnerPredicate;
import com.blackbox.common.ActivityThreadsMessageOwnerPredicate;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Ignore;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.util.CollectionUtils;
import com.blackbox.util.MessagesHelper;
import com.blackbox.util.RelationsHelper;
import com.google.common.collect.Collections2;
import org.junit.Before;
import org.junit.Test;
import org.yestech.cache.impl.HashMapCacheManager;

import javax.annotation.Resource;
import java.util.Collection;

import static com.blackbox.testingutils.UserHelper.createNamedUser;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class ActivityStreamIntegrationTest extends BaseIntegrationTest {

    @Resource
    private IUserManager userManager;

    @Resource
    private ISocialManager socialManager;

    @Resource
    private IActivityManager activityManager;

    @Resource
    private IMessageManager messageManager;


    private User jim;
    private User sally;
    private RelationsHelper relationsHelper;
    private MessagesHelper messagesHelper;
    private Collection<IActivityThread> activities;

    @Before
    public void initialize() {
        jim = createNamedUser("jim", userManager);
        sally = createNamedUser("sally", userManager);
        relationsHelper = new RelationsHelper(socialManager);
        messagesHelper = new MessagesHelper(messageManager, activityManager, new HashMapCacheManager<String, Collection<Message>>());
    }

    //APP-258: Filtering in the stream (friends, followers, etc) is not working correctly. Please fix.
    //    So, Jim creates a post for "Friends Only"
    //Sally follows Jim, but is not his friend.
    //Sally *should not be able to* see Jim's "Friends Only" post in the stream

    @Test
    public void testLoadFollowingActivityThreadsWhenFollowedPublishedMessage() throws Exception {
        relationsHelper.createFollowing(sally, jim);
        assertCleanStart(sally);

        messagesHelper.publishMessage(jim, "dear friends, happy happy, love jim", NetworkTypeEnum.FRIENDS);
        Thread.sleep(5000);

        activities = messagesHelper.fetchFriendsMessages(sally);
        assertEquals("unexpected message found! sally has no relationship to jim", 0, activities.size());

        activities = messagesHelper.fetchFollowingMessages(sally);
        assertEquals("unexpected message found! sally has no relationship to jim", 0, activities.size());

        relationsHelper.createFriendship(sally, jim);
        activities = messagesHelper.fetchFriendsMessages(sally);
        assertEquals("the upgrading of the sally to friend should have allowed the finding of the friendly published message", 1, activities.size());
    }

    // APP-189 Donkey kick doesn?'?t work as expected
    //    Removing posts from the stream is going to have to be an AJ thing and I'm guessing will be a lot more difficult. I'll pass this to AJ and let him chime in...

    @Test
    public void donkeyKickingSomeoneHidesTheirPostsFromMyStream() throws Exception {
        assertCleanStart(sally);

        messagesHelper.publishMessage(jim, "dear friends, happy 1, love jim", NetworkTypeEnum.FRIENDS);
        messagesHelper.publishMessage(jim, "dear all members, happy 2, love jim", NetworkTypeEnum.ALL_MEMBERS);
        Message parentMessage = messagesHelper.publishMessage(sally, "dear all members, happy 3, love sally", NetworkTypeEnum.ALL_MEMBERS);
        messagesHelper.publishChildMessage(jim, parentMessage, "dear all members, happy 3.1, love jim", NetworkTypeEnum.ALL_MEMBERS);
        messagesHelper.publishChildMessage(sally, parentMessage, "dear all members, happy 3.1, love sally", NetworkTypeEnum.ALL_MEMBERS);
        Thread.sleep(5000);

        relationsHelper.createFriendship(sally, jim);

        activities = messagesHelper.fetchFriendsMessages(sally);
        assertEquals(2, activities.size());

        activities = messagesHelper.fetchAllMessages(sally);
        int numberOfJimPostings = Collections2.filter(activities, new ActivityThreadsMessageOwnerPredicate(jim.toEntityReference())).size();
        assertEquals("unable to find jim's posting to all members", 2, numberOfJimPostings);

        socialManager.ignore(new Ignore(sally.toEntityReference(), jim.toEntityReference(), Ignore.IgnoreType.HARD));
        assertFalse(relationsHelper.isFriends(sally, jim));

        activities = messagesHelper.fetchFriendsMessages(sally);
        assertEquals(1, activities.size()); // she gets her own top level activity back
        numberOfJimPostings = Collections2.filter(activities, new ActivityThreadsMessageOwnerPredicate(jim.toEntityReference())).size();
        assertEquals("able to find jim's posting to friends [we should not after ignore]", 0, numberOfJimPostings);

        activities = messagesHelper.fetchAllMessages(sally);
        assertFalse(activities.isEmpty());
        numberOfJimPostings = Collections2.filter(activities, new ActivityThreadsMessageOwnerPredicate(jim.toEntityReference())).size();
        assertEquals("able to find jim's posting to all members [we should not after ignore]", 0, numberOfJimPostings);

        // now find that one message sally made (and jim responded to) and make sure jim's response is not in there...
        Collection<IActivityThread> sallyParent = Collections2.filter(activities, new ActivityThreadsMessageOwnerPredicate(sally.toEntityReference()));
        assertEquals(1, sallyParent.size());

        numberOfJimPostings = Collections2.filter(CollectionUtils.guaranteeOne(sallyParent).getChildren(), new ActivitySenderOwnerPredicate(jim.toEntityReference())).size();
        assertEquals("able to find jim's posting to all members [we should not after ignore]", 0, numberOfJimPostings);
    }

    private void assertCleanStart(User user) {
        Collection<IActivityThread> messages = messagesHelper.fetchFriendsMessages(user);
        assertTrue(messages.isEmpty());
        messages = messagesHelper.fetchFollowingMessages(user);
        assertTrue(messages.isEmpty());
    }


}