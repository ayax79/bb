package com.blackbox.message;

import com.blackbox.activity.IActivityThread;
import com.blackbox.user.User;
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

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        // should only add one since we published the same message x 3

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());
    }

    @Test
    public void testApplyPrePublishedMessagesDoesNotCreateDuplicatesWhenMessageWasAlreadyProcessed() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());

        // the order between these next 2 calls does not matter
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.bindMessageOntoStream(message, activityThreads);
        // should only add one since we pre-published the same message that was already on stream

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());

    }

    @Test
    public void testApplyPrePublishedMessagesHandlesMultimediaUpload() throws Exception {
        User user = User.createUser();
        Collection<IActivityThread> activityThreads = newArrayList();

        Collection<IActivityThread> activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(0, activityThreads.size());

        Message message = Message.createMessage();
        message.getArtifactMetaData().setArtifactOwner(user.getEntityReference());
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);
        PrePublicationUtil.prePublish(message, cacheManager);

        activityThreadsIncludingPrePublishedMessages = PrePublicationUtil.applyPrePublishedMessages(user, activityThreads, cacheManager);
        assertEquals(1, activityThreadsIncludingPrePublishedMessages.size());

    }

}
