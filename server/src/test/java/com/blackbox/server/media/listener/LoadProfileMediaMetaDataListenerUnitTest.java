package com.blackbox.server.media.listener;

import static com.blackbox.server.BBTestUtils.assertValidListener;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.LoadProfileMediaMetaDataEvent;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.user.User;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.yestech.event.ResultReference;

/**
 *
 *
 */
@RunWith(JMock.class)
public class LoadProfileMediaMetaDataListenerUnitTest {

    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle() {
        final IMediaDao mediaDao = mockery.mock(IMediaDao.class);
        final EntityReference entityReference = User.createUser().toEntityReference();
        final MediaMetaData metaData = MediaMetaData.createMediaMetaData();

        mockery.checking(new Expectations() {
            {
                oneOf(mediaDao).loadProfileMediaMetaDataByOwner(entityReference);
                will(returnValue(metaData));
            }
        });

        LoadProfileMediaMetaDataListener listener = new LoadProfileMediaMetaDataListener();
        listener.setMediaDao(mediaDao);
        assertValidListener(listener);

        ResultReference<MediaMetaData> ref = new ResultReference<MediaMetaData>();
        LoadProfileMediaMetaDataEvent event = new LoadProfileMediaMetaDataEvent(entityReference);
        listener.handle(event, ref);
        MediaMetaData result = ref.getResult();
        assertNotNull(result);
        assertEquals(metaData, result);
    }
}
