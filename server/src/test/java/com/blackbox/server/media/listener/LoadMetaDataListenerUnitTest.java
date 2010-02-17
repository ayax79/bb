/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.listener;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.LoadContentMetaDataEvent;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;

/**
 *
 *
 */
@RunWith(JMock.class)
public class LoadMetaDataListenerUnitTest
{
    Mockery mockery = new JUnit4Mockery();


    @Test
    public void testHandle()
    {

        final MediaMetaData cmd = MediaMetaData.createMediaMetaData();
        cmd.setFileName("sdlkfj");

        final IMediaDao mediaDao = mockery.mock(IMediaDao.class, "mediaDao");

        LoadMetaDataListener listener = new LoadMetaDataListener();
        listener.setMediaDao(mediaDao);
        mockery.checking(new Expectations()
        {
            {
                oneOf(mediaDao).loadMediaMetaDataByGuid(cmd.getGuid());
                will(returnValue(cmd));
            }
        });


        ResultReference<MediaMetaData> ref = new ResultReference<MediaMetaData>();
        listener.handle(new LoadContentMetaDataEvent(cmd.getGuid()), ref);

        MediaMetaData metaData = ref.getResult();
        assertNotNull(metaData);
        assertEquals(cmd, metaData);
    }
}
