/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.media.publisher;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.jmock.integration.junit4.JMock;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.yestech.publish.objectmodel.IFileArtifactMetaData;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.DefaultFileArtifact;
import org.yestech.publish.publisher.IPublisher;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.HashMap;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.activity.ActivityFactory;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@RunWith(JMock.class)
public class MultimediaAdapterPublisherUnitTest {
    Mockery context = new Mockery();
    private MultimediaAdapterPublisher publisher;

    @Before
    public void setUp() {
        publisher = new MultimediaAdapterPublisher();
    }

    @Test
    public void testImagePublish() {
        final MediaMetaData metaData = ActivityFactory.createMedia();
        metaData.setArtifactType(ArtifactType.IMAGE);
//        final IMediaDao iMediaDao = context.mock(IMediaDao.class, "dao");
        final IPublisher adapteePublisher = context.mock(IPublisher.class, "publisher");
        String message = "message";
        final ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes());
        final DefaultFileArtifact<MediaMetaData, Long> artifact = new DefaultFileArtifact<MediaMetaData, Long>();
        artifact.setStream(stream);
        artifact.setArtifactMetaData(metaData);
        context.checking(new Expectations() {
            {
//                oneOf(iMediaDao).save(with(aNonNull(MediaMetaData.class)));
                oneOf(adapteePublisher).publish(artifact);
            }
        });
        Map<ArtifactType, IPublisher> adaptees = new HashMap<ArtifactType, IPublisher>();
        adaptees.put(ArtifactType.IMAGE, adapteePublisher);
        publisher.setAdaptees(adaptees);
        publisher.publish(artifact);
    }

    @Test
    public void testVideoPublish() {
        final MediaMetaData metaData = ActivityFactory.createMedia();
        metaData.setArtifactType(ArtifactType.VIDEO);
//        final IMediaDao iMediaDao = context.mock(IMediaDao.class, "dao");
        final IPublisher adapteePublisher = context.mock(IPublisher.class, "publisher");
        String message = "message";
        final ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes());
        final DefaultFileArtifact<MediaMetaData, Long> artifact = new DefaultFileArtifact<MediaMetaData, Long>();
        artifact.setStream(stream);
        artifact.setArtifactMetaData(metaData);

        context.checking(new Expectations() {
            {
                oneOf(adapteePublisher).publish(artifact);
//            oneOf(iMediaDao).save(with(aNonNull(MediaMetaData.class)));
            }
        });
        Map<ArtifactType, IPublisher> adaptees = new HashMap<ArtifactType, IPublisher>();
        adaptees.put(ArtifactType.VIDEO, adapteePublisher);
        publisher.setAdaptees(adaptees);
        publisher.publish(artifact);
    }

    @Test(expected = RuntimeException.class)
    public void testInvaildPublish() {
        final IFileArtifactMetaData metaData = context.mock(IFileArtifactMetaData.class, " metaData");
        String message = "message";
        ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes());
        final DefaultFileArtifact<IFileArtifactMetaData, Long> artifact = new DefaultFileArtifact<IFileArtifactMetaData, Long>();
        artifact.setStream(stream);
        artifact.setArtifactMetaData(metaData);

        context.checking(new Expectations() {
            {
                oneOf(metaData).getArtifactType();
                will(returnValue(ArtifactType.TEXT));
                oneOf(metaData).getArtifactType();
                will(returnValue(ArtifactType.TEXT));
            }
        });
        Map<ArtifactType, IPublisher> adaptees = new HashMap<ArtifactType, IPublisher>();
        publisher.setAdaptees(adaptees);
        publisher.publish(artifact);
    }

    @Test
    public void testAudioPublish() {
        final MediaMetaData metaData = ActivityFactory.createMedia();
        metaData.setArtifactType(ArtifactType.AUDIO);
//        final IMediaDao iMediaDao = context.mock(IMediaDao.class, "dao");
        final IPublisher adapteePublisher = context.mock(IPublisher.class, "publisher");
        String message = "message";
        final ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes());
        final DefaultFileArtifact<MediaMetaData, Long> artifact = new DefaultFileArtifact<MediaMetaData, Long>();
        artifact.setStream(stream);
        artifact.setArtifactMetaData(metaData);

        context.checking(new Expectations() {
            {
                oneOf(adapteePublisher).publish(artifact);
//            oneOf(iMediaDao).save(with(aNonNull(MediaMetaData.class)));
            }
        });
        Map<ArtifactType, IPublisher> adaptees = new HashMap<ArtifactType, IPublisher>();
        adaptees.put(ArtifactType.AUDIO, adapteePublisher);
        publisher.setAdaptees(adaptees);
        publisher.publish(artifact);
    }
}
