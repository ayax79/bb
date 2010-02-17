package com.blackbox.server.activity;

import com.blackbox.foundation.activity.ActivityRequest;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.orm.ibatis3.SqlSessionOperations;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author colin@blackboxrepublic.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityManagerUnitTest {

    @Mock
    private SqlSessionOperations template;

    private ActivityManager activityManager;

    private User user;
    private ArrayList<MediaMetaData> mediaMetaDataList = newArrayList();
    private ArrayList<Message> messages = newArrayList();

    @Before
    public void initialize() throws Exception {
        user = new User("ActivityManagerUnitTest");
        activityManager = new ActivityManager();
        activityManager.activityStreamDao = new IbatisActivityStreamDao();
//        ResourceApplicator.injectByType(activityManager.activityStreamDao, template);
    }

    @Ignore
    @Test
    public void testLoadActivityThreadsNothingToSee() throws Exception {
        when(template.<MediaMetaData>selectList("Activity.loadFriendMedias")).thenReturn(mediaMetaDataList);
        when(template.<Message>selectList("Activity.loadFriendMessages")).thenReturn(messages);
        Collection<IActivityThread> activityThreads = activityManager.loadActivityThreads(new ActivityRequest(user.getEntityReference(), FilterHelper.everyoneFilter(), new Bounds(0, 10)));
        assertEquals(0, activityThreads.size());
//        verify(activityManager.loadActivityThreads());

    }

    @Ignore
    @Test
    public void testLoadActivityThreads() throws Exception {
        when(template.<MediaMetaData>selectList("Activity.loadFriendMedias")).thenReturn(mediaMetaDataList);
        when(template.<Message>selectList("Activity.loadFriendMessages")).thenReturn(messages);
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
}
