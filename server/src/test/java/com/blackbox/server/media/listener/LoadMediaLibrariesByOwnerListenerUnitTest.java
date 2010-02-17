package com.blackbox.server.media.listener;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.LoadMediaLibrariesByOwnerEvent;
import static com.blackbox.server.BBTestUtils.assertValidListener;
import com.blackbox.foundation.user.User;
import static com.google.common.collect.Lists.newArrayList;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;

import java.util.List;

/**
 *
 *
 */
@RunWith(JMock.class)
public class LoadMediaLibrariesByOwnerListenerUnitTest {

    protected Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle() {

        final EntityReference owner = User.createUser().toEntityReference();
        final IMediaDao mediaDao = mockery.mock(IMediaDao.class, "mediaDao");
        final MediaLibrary mediaLibrary = MediaLibrary.createMediaLibrary();

        mockery.checking(new Expectations() {
            {
                oneOf(mediaDao).loadMediaLibrariesByOwner(owner);
                will(returnValue(newArrayList(mediaLibrary)));
            }
        });

        LoadMediaLibrariesByOwnerListener listener = new LoadMediaLibrariesByOwnerListener();
        assertValidListener(listener);
        listener.setMediaDao(mediaDao);

        ResultReference<List<MediaLibrary>> ref = new ResultReference<List<MediaLibrary>>();
        LoadMediaLibrariesByOwnerEvent event = new LoadMediaLibrariesByOwnerEvent(owner);
        listener.handle(event, ref);

        List<MediaLibrary> result = ref.getResult();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mediaLibrary, result.get(0));
    }
}
