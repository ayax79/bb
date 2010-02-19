package com.blackbox.foundation.message;

import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.common.TwoBounds;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import org.junit.Before;
import org.junit.Test;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.HashMapCacheManager;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

/**
 * @author colin@blackboxrepublic.com
 */
public class PrePublicationUtilUnitTest {

    private ICacheManager<String, Collection<Message>> cacheManager;

    @Before
    public void initialize() {
        cacheManager = new HashMapCacheManager<String, Collection<Message>>();
    }

    @Test
    public void testApplyPrePublishedMessagesDoesNotCreateDuplicates() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        // should only add one since we published the same message x 3

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());
    }

    @Test
    public void testApplyPrePublishedMessagesDoesNotCreateDuplicatesWhenMessageWasAlreadyProcessed() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());

        // the order between these next 2 calls does not matter
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.bindMessageOntoStream(message, activityThreads);
        // should only add one since we pre-published the same message that was already on stream

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());

    }

    @Test
    public void testApplyPrePublishedMessagesHandlesMultimediaUpload() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, TwoBounds.boundLess(), cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());

    }

    @Test
    public void testApplyPrePublishedMessagesDoesNotApplyWhenNotBeginningOfBounds() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();
        TwoBounds twoBounds = new TwoBounds(new Bounds(), new Bounds());

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, twoBounds, cacheManager);
        assertEquals(0, activityThreads.size());

        // move to next page and have the application not stick (since, the ui asks for 10-20 (for example) not 0-20 so i don't have any context to *not* add them again
        // other than by assuming they have paged to next page faster than their post would need to be applied to (top!) of next page.
        twoBounds.next(10);
        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());
        PrePublicationUtil.prePublish(message, cacheManager);

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, twoBounds, cacheManager);
        assertEquals(0, activityThreadsIncludingPrePublishedMessages.size());

    }

}
